/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esinotrans.payment.account.dao.AccountDao;
import com.esinotrans.payment.account.dao.AccountHistoryDao;
import com.esinotrans.payment.account.dao.AccountSnapshotDao;
import com.esinotrans.payment.account.dto.TransactionCommandParam;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.AccountHistory;
import com.esinotrans.payment.account.entity.AccountSnapshot;
import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.service.AccountTransactionService;
import com.esinotrans.payment.account.service.QuotaLimitChangeRuleProcessor;
import com.esinotranse.payment.utils.CheckUtils;
import com.esinotranse.payment.utils.DateUtils;
import com.esinotranse.payment.utils.MathUtils;
import com.esinotranse.payment.utils.common.encrypt.HmacSign;
import com.esinotranse.payment.utils.json.JSONUtils;

/**
 * 账户交易服务 实现类
 * 
 * @company YeePay
 * @author 王伟
 * @since 2010-8-31
 * @version 1.0
 */
public class AccountTransactionServiceImpl implements
		AccountTransactionService {

	private AccountDao accountDao;						//账户实体DAO
	
	private QuotaLimitChangeRuleProcessor quotaLimitChangeRuleProcessor;	//额度限制变更规则处理器

	private AccountHistoryDao accountHistoryDao;		//账户历史Dao
	
	private AccountSnapshotDao accountSnapshotDao;					// 帐户快照表 DAO
	
	private Map<String,String> cacheKeyMap = new HashMap<String,String>();	//缓存密钥
	
	private String currentKeyVersion;							//当前key版本
	
	private static final String SEPARATE = ":";
	
	/**
	 * 存入
	 */
	@Override
	public void credit(TransactionCommandParam command) throws AccountException {
		credit(command,command.getTradeType());
	}
	
	/**
	 * 存入
	 */
	@Override
	public void credit(TransactionCommandParam command,String tradeType) throws AccountException{
		Account account = queryAccountByAccountNo(command.getReceiverAccountNo());
		validateCredit(account);
		doCredit(account,command,tradeType);
	}
	
	/**
	 * 扣款
	 */
	@Override
	public void debit(TransactionCommandParam command) throws AccountException {
		Account account = queryAccountByAccountNo(command.getPayerAccountNo());
		validateDebit(account, command.getAmount());
		doDebit(account,command);
	}
	
	/**
	 * 调账
	 */
	@Override
	public void adjusting(TransactionCommandParam command) {
		if (FundChangeDirectionEnum.INCREMENT == command.getDirection()){			//存入
			CheckUtils.valueIsNull(command, "receiverAccountNo");
			Account account = queryAccountByAccountNo(command.getReceiverAccountNo());
			doCredit(account,command);
		}else if(FundChangeDirectionEnum.DECREASE == command.getDirection()){		//扣款
			CheckUtils.valueIsNull(command, "payerAccountNo");
			Account account = queryAccountByAccountNo(command.getPayerAccountNo());
			if (!account.availableBalanceIsEnough(command.getAmount()))
				throw AccountException.AVAILABLE_BALANCE_ISNOT_ENOUGH.newInstance(
						"账户：{0} 可用余额不足. 可用余额为：{1}， 需要扣除的额度为：{2}", account
								.getAccountNo(), account.getAvailableBalance(), command.getAmount());
			doDebit(account,command);
		}
	}
	
	/**
	 * 对于同一账户，进行批量存入
	 * @param params 交易命令参数集合
	 */
	@Override
	public void batchCreditForSameAccount(List<TransactionCommandParam> params) throws AccountException{
		TransactionCommandParam command = params.get(0);
		Account account = queryAccountByAccountNo(command.getReceiverAccountNo());
		BigDecimal balance =  MathUtils.round(account.getBalance());				//当前余额
		validateCredit(account);
		BigDecimal totalAmount = BigDecimal.ZERO;		//总发生额
		for(TransactionCommandParam element : params){
			totalAmount = totalAmount.add(element.getAmount());
		}
		account.credit(totalAmount);
		account.setBalanceSign(generateBalanceSign( MathUtils.round(account.getBalance())));
		quotaLimitChangeRuleProcessor.processQuotaLimitChangeRule(account, command.getTradeType(),totalAmount);
		account.checkQuotaLimit();
		this.saveAccountHistoryList(params, account, FundChangeDirectionEnum.INCREMENT,balance);
		
		//保存帐户快照
		saveAccountSnapshot(account.getAccountNo(),balance,account.getSnapshotDate());
		
		Map<String, Object> condition = createCondition(account);
		condition.put("accountHistorySerial", Long.valueOf(account.getAccountHistorySerial() + params.size()));
		accountDao.updateAccountBalance(condition);
		
	}
	
	/**
	 * 生成余额的签名
	 * @param amount
	 * @return
	 */
	@Override
	public String generateBalanceSign(BigDecimal amount){
		if(amount == null)
			return null;
		String signKeyValue = findSignKey(this.currentKeyVersion);
		String signValue = HmacSign.signToBase64(amount.toString(), signKeyValue);
		return this.currentKeyVersion +SEPARATE + signValue;
		
	}
	
	/**
	 * 进行存入操作
	 * @param account
	 * @param command
	 */
	private void doCredit(Account account,TransactionCommandParam command){
		doCredit(account,command,command.getTradeType());
	}
	
	/**
	 * 进行存入操作
	 * @param account
	 * 			帐户
	 * @param command
	 * 			交易命令参数
	 * @param tradeType
	 * 			变更额度限制的交易类型
	 */
	private void doCredit(Account account,TransactionCommandParam command,String tradeType){
		BigDecimal srcBalance = MathUtils.round(account.getBalance());
		validateBalanceSign(srcBalance,account.getBalanceSign());
		account.credit(command.getAmount());
		account.setBalanceSign(generateBalanceSign( MathUtils.round(account.getBalance())));
		afterTransaction(account, command, FundChangeDirectionEnum.INCREMENT,srcBalance,tradeType);
		accountDao.updateAccountBalance(createCondition(account));
	}
	
	/**
	 * 进行扣款操作
	 * @param account
	 * @param command
	 */
	private void doDebit(Account account,TransactionCommandParam command){
		BigDecimal srcBalance =  MathUtils.round(account.getBalance());
		validateBalanceSign(srcBalance,account.getBalanceSign());
		account.debit(command.getAmount());
		account.setBalanceSign(generateBalanceSign( MathUtils.round(account.getBalance())));
		afterTransaction(account, command, FundChangeDirectionEnum.DECREASE,srcBalance,command.getTradeType());
		accountDao.updateAccountBalance(createCondition(account));
	}

	/**
	 * 存入扣款都处理。修改额度、创建账户历史
	 * @param account
	 * @param command
	 * @param direction
	 * 			资金方向
	 * @param srcBalance
	 * 			源余额
	 * @param tradeType
	 * 			变更额度限制的交易类型
	 * @throws AccountException
	 */
	private void afterTransaction(Account account,
			TransactionCommandParam command, FundChangeDirectionEnum direction,BigDecimal srcBalance,String tradeType)
			throws AccountException {
		quotaLimitChangeRuleProcessor.processQuotaLimitChangeRule(account, tradeType,command.getAmount());
		account.checkQuotaLimit();
		
		//保存账户历史
		saveAccountHistory(command, account, direction);
		
		//保存帐户快照
		saveAccountSnapshot(account.getAccountNo(),srcBalance,account.getSnapshotDate());
	}
	
	/**
	 * 保存帐户快照
	 * @param accountNo
	 * @param balance
	 */
	private void saveAccountSnapshot(String accountNo,BigDecimal balance,Date snapshotDate){
		Date currentDate = DateUtils.getDayStart(new Date());
		if(snapshotDate == null || !currentDate.equals(snapshotDate)){
			AccountSnapshot snapshot = new AccountSnapshot();
			snapshot.setAccountNo(accountNo);
			snapshot.setBalance(balance);
			snapshot.setSnapDate(DateUtils.getDayStart(new Date()));
			accountSnapshotDao.add(snapshot);
		}
	}

	private Map<String, Object> createCondition(Account account) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("accountNo", account.getAccountNo());
		condition.put("balance",  MathUtils.round(account.getBalance()));
		condition.put("availableWithdrawQuota", account
				.getAvailableWithdrawQuota());
		condition.put("availableRechargeRefundQuota", account.getAvailableRechargeRefundQuota());
		condition.put("frozenQuota", account.getFrozenQuota());
		condition.put("accountHistorySerial", Long.valueOf(account
				.getAccountHistorySerial() + 1));
		condition.put("version", account.getVersion());
		condition.put("balanceSign", account.getBalanceSign());
		condition.put("snapshotDate", account.getSnapshotDate());
		return condition;
	}

	/**
	 * 保存账户历史
	 * 
	 * @param command
	 * @param account
	 * @param direction
	 */
	private void saveAccountHistory(TransactionCommandParam command,
			Account account, FundChangeDirectionEnum direction) {
		String summaryContent = command.getSummary() == null?"":JSONUtils.toJsonString(command.getSummary());
		String changeDesc = "可提现额度为：" + account.getAvailableWithdrawQuota()
				+ " ,可充退额度为："+account.getAvailableRechargeRefundQuota();
		AccountHistory accountHistory = AccountHistory.newInstance(command
				.getFlowID(), account.getAccountNo(), direction, command
				.getTradeType(), command.getAmount(), MathUtils.round(account.getBalance()), Long
				.valueOf(account.getAccountHistorySerial() + 1), new Date(),
				summaryContent,command.getUserRemark(),changeDesc);
		accountHistoryDao.saveAccountHistory(accountHistory);
	}
	
	/**
	 * 保存账户历史 集合
	 * 
	 * @param command
	 * @param account
	 * @param direction
	 */
	private void saveAccountHistoryList(List<TransactionCommandParam> commands,
			Account account, FundChangeDirectionEnum direction,BigDecimal balance){
		List<AccountHistory> accountHistorys = new ArrayList<AccountHistory>();
		Date currentDate = new Date();
		for(int i = 0,size = commands.size(); i < size; i++){
			TransactionCommandParam command = commands.get(i);
			balance = balance.add(command.getAmount());
			String summaryContent = command.getSummary() == null?"":JSONUtils.toJsonString(command);
			String changeDesc = "可提现额度为：" + account.getAvailableWithdrawQuota()
					+ " ,可充退额度为："+account.getAvailableRechargeRefundQuota();
			AccountHistory accountHistory = AccountHistory.newInstance(command
					.getFlowID(), account.getAccountNo(), direction, command
					.getTradeType(), command.getAmount(),balance, Long
					.valueOf(account.getAccountHistorySerial() + (i+1)), currentDate,
					summaryContent,command.getUserRemark(),changeDesc);
			accountHistorys.add(accountHistory);
		}
		accountHistoryDao.saveAccountHistoryList(accountHistorys);
	}

	/**
	 * 验证账户是否存在，如果存在，返回账户，不存在，抛出账户不存在异常
	 * 
	 * @param account
	 * @throws AccountException
	 */
	private Account queryAccountByAccountNo(String accountNo)
			throws AccountException {
		Account account = accountDao.queryAccountByAccountNo(accountNo);
		if (account == null)
			throw AccountException.ACCOUNT_ISNOT_EXIST.newInstance(
					"账号：{0}对应的账户不存在", new Object[] { accountNo });
		return account;
	}

	/**
	 * 验证存入交易逻辑
	 */
	protected void validateCredit(Account account) throws AccountException {
		if (!account.validateCreditAccountStatus())
			throw AccountException.ACCOUNT_STATUS_IS_NOT_VALID.newInstance(
					"账户：{0} 状态不合法", account.getAccountNo());
	}

	/**
	 * 验证扣款交易逻辑
	 */
	protected void validateDebit(Account account, BigDecimal amount)
			throws AccountException {
		if (!account.validateDebitAccountStatus())
			throw AccountException.ACCOUNT_STATUS_IS_NOT_VALID.newInstance(
					"账户：{0} 状态不合法", account.getAccountNo());
		if (!account.availableBalanceIsEnough(amount))
			throw AccountException.AVAILABLE_BALANCE_ISNOT_ENOUGH.newInstance(
					"账户：{0} 可用余额不足. 可用余额为：{1}， 需要扣除的额度为：{2}", account
							.getAccountNo(), MathUtils.round(account
							.getAvailableBalance()), MathUtils.round(amount));
	}
	
	/**
	 * 验证余额的签名
	 * @param amount
	 * @param balanceSign
	 */
	private void validateBalanceSign(BigDecimal amount,String balanceSign){
		if(balanceSign == null || !balanceSign.contains(SEPARATE))
			throw AccountException.ACCOUNT_BALANCE_DISTORT.newInstance("账户余额被篡改");
		int index = balanceSign.indexOf(SEPARATE);
		String version = balanceSign.substring(0,index);
		String signValue = balanceSign.substring(index+1, balanceSign.length());
		String signKeyValue = findSignKey(version);
		String newBalanceSign = HmacSign.signToBase64(amount.toString(), signKeyValue);
		if(!signValue.equals(newBalanceSign))
			throw AccountException.ACCOUNT_BALANCE_DISTORT.newInstance("账户余额被篡改");
	}
	
	/**
	 * 查找当前key版本对应的key值
	 * @return
	 */
	private String findSignKey(String version){
		if(version == null)
			throw new RuntimeException(" key version must not null");
		String signKeyValue = cacheKeyMap.get(version); 
		if(signKeyValue == null)
			throw new RuntimeException("version:["+version+"] opposite sign key value is null");
		return signKeyValue;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setQuotaLimitChangeRuleProcessor(
			QuotaLimitChangeRuleProcessor quotaLimitChangeRuleProcessor) {
		this.quotaLimitChangeRuleProcessor = quotaLimitChangeRuleProcessor;
	}

	public void setAccountHistoryDao(AccountHistoryDao accountHistoryDao) {
		this.accountHistoryDao = accountHistoryDao;
	}

	public void setCacheKeyMap(Map<String, String> cacheKeyMap) {
		this.cacheKeyMap = cacheKeyMap;
	}

	public void setCurrentKeyVersion(String currentKeyVersion) {
		this.currentKeyVersion = currentKeyVersion;
	}

	public void setAccountSnapshotDao(AccountSnapshotDao accountSnapshotDao) {
		this.accountSnapshotDao = accountSnapshotDao;
	}
}
