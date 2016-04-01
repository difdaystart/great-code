/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.esinotrans.payment.account.dao.AccountDao;
import com.esinotrans.payment.account.dao.AccountFrozenRecordDao;
import com.esinotrans.payment.account.dao.AccountManagementRecordDao;
import com.esinotrans.payment.account.dao.AccountProviderDao;
import com.esinotrans.payment.account.dao.AccountSnapshotDao;
import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.dto.CreateAccountParam;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.AccountFrozenRecord;
import com.esinotrans.payment.account.entity.AccountManagementRecord;
import com.esinotrans.payment.account.entity.AccountProvider;
import com.esinotrans.payment.account.entity.AccountSnapshot;
import com.esinotrans.payment.account.enums.AccountOperationEnum;
import com.esinotrans.payment.account.enums.AccountStatusEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.numbergenerator.NumberGenerator;
import com.esinotrans.payment.account.service.AccountManagementService;
import com.esinotrans.payment.account.service.AccountTransactionService;
import com.esinotrans.payment.common.enums.CurrencyEnum;
import com.esinotranse.payment.utils.CommonUtils;
import com.esinotranse.payment.utils.DateUtils;
import com.esinotranse.payment.utils.MathUtils;
import com.esinotranse.payment.utils.StringUtils;
import com.esinotranse.payment.utils.json.JSONUtils;

/**
 * 账户管理服务 实现类
 * 
 * @company YeePay
 * @author 王伟
 * @since 2010-8-30
 * @version 1.0
 */
public class AccountManagementServiceImpl implements
		AccountManagementService {
	
	private AccountDao accountDao;									//账户实体DAO

	private NumberGenerator accountNumberGenerator;					//号码生成器

	private AccountManagementRecordDao accountManagementRecordDao; // 账户管理记录DAO

	private AccountFrozenRecordDao accountFrozenRecordDao; 			// 账户冻结记录Dao

	private AccountProviderDao accountProviderDao;					// 账户提供方实体DAO
	
	private String accountSystemType;								//账户系统类型
	
	private AccountSnapshotDao accountSnapshotDao;					// 帐户快照表 DAO
	
	private AccountTransactionService accountTransactionService;

	@Override
	public String createAccount(ContextParam contextParam,
			CreateAccountParam createAccountParam) throws AccountException {
		this.validateBalanceIsNegative(createAccountParam);
		AccountProvider provider = queryAccountProvider(createAccountParam.getAccountProviderCode());
		Account account = doCreateAccount(createAccountParam, provider);
		accountDao.saveAccount(account);
		
		//保存帐户管理历史
		AccountManagementRecord record = AccountManagementRecord.newInstance(
				contextParam.getFlowID(), contextParam.getInitiator(),
				account.getAccountNo(), account.getManageSerial(),
				AccountOperationEnum.CREATE_ACCOUNT, "", new Date());
		accountManagementRecordDao.saveAccountManagementRecord(record);
		
		// 保存帐户快照
		saveAccountSnapshot(account.getAccountNo(),account.getBalance());
		return account.getAccountNo();
	}
	
	/**
	 * 创建帐户，为2代迁移之用
	 */
	public String createAccount(ContextParam contextParam,
			CreateAccountParam createAccountParam,String status,
			Date createDate,Long accountHistorySerial) throws AccountException{
		this.validateBalanceIsNegative(createAccountParam);
		AccountProvider provider = queryAccountProvider(createAccountParam.getAccountProviderCode());
		Account account = doCreateAccount(createAccountParam, provider,status,accountHistorySerial);
		accountDao.saveAccount(account);
		
		//保存帐户管理历史
		AccountManagementRecord record = AccountManagementRecord.newInstance(
				contextParam.getFlowID(), contextParam.getInitiator(),
				account.getAccountNo(), account.getManageSerial(),
				AccountOperationEnum.CREATE_ACCOUNT, "", createDate == null?new Date():createDate);
		accountManagementRecordDao.saveAccountManagementRecord(record);
		
		// 保存帐户快照
		saveAccountSnapshot(account.getAccountNo(),account.getBalance());
		return account.getAccountNo();
	}
	
	/**
	 * 冻结止收
	 * 
	 * @param accountNo
	 *            账号
	 * @param param
	 *            基本参数对象
	 * @param unfrozenDate
	 *            解冻时间 可以为空
	 */
	@Override
	public String forbidCredit(ContextParam param, String accountNo,
			Date unfrozenDate, String credential) throws AccountException {
		Account account = queryAccountByAccountNo(accountNo);

		// 保存或更新账户冻结记录
		String rtnCredential = saveOrUpdateAccountFrozenRecord(account,
				param.getFlowID(), accountNo, param.getInitiator(),
				unfrozenDate, AccountOperationEnum.FREEZE_CREDIT, credential);

		// 更新账户状态
		this.updateAccountStatus(account);

		//保存账户管理历史
		saveAccountManagementRecord(param,account,AccountStatusEnum.ACCOUNT_FREEZE_CREDIT,
				AccountOperationEnum.FREEZE_CREDIT,unfrozenDate);
		
		return rtnCredential;
	}
	
	/**
	 * 冻结止付
	 * 
	 * @param accountNo
	 *            账号
	 * @param param
	 *            基本参数对象
	 * @param unfrozenDate
	 *            解冻时间 可以为空
	 */
	@Override
	public String forbidDebit(ContextParam param, String accountNo,
			Date unfrozenDate, String credential) throws AccountException {
		Account account = queryAccountByAccountNo(accountNo);

		// 保存或更新账户冻结记录
		String rtnCredential = saveOrUpdateAccountFrozenRecord(account,
				param.getFlowID(), accountNo, param.getInitiator(),
				unfrozenDate, AccountOperationEnum.FREEZE_DEBIT, credential);

		// 更新账户状态
		this.updateAccountStatus(account);

		//保存账户管理历史
		saveAccountManagementRecord(param,account,AccountStatusEnum.ACCOUNT_FREEZE_DEBIT,
				AccountOperationEnum.FREEZE_DEBIT,unfrozenDate);
		return rtnCredential;
	}

	/**
	 * 冻结账户
	 * 
	 * @param accountNo
	 */
	@Override
	public String freezeAccount(ContextParam param, String accountNo,
			Date unfrozenDate, String credential) throws AccountException {
		// 查询账户
		Account account = queryAccountByAccountNo(accountNo);

		// 保存或更新账户冻结记录
		String rtnCredential = saveOrUpdateAccountFrozenRecord(account,
				param.getFlowID(), accountNo, param.getInitiator(),
				unfrozenDate, AccountOperationEnum.FREEZE_ACCOUNT, credential);

		// 更新账户状态
		this.updateAccountStatus(account);

		//保存账户管理历史
		saveAccountManagementRecord(param,account,AccountStatusEnum.ACCOUNT_FROZEN,
				AccountOperationEnum.FREEZE_ACCOUNT,unfrozenDate);
		return rtnCredential;
	}

	/**
	 * 解冻账户
	 */
	@Override
	public void unfreezeAccount(String accountNo, String credential,
			ContextParam param) throws AccountException {
		// 查询账户
		Account account = queryAccountByAccountNo(accountNo);
		AccountFrozenRecord record = accountFrozenRecordDao
				.queryAccountFrozenRecordByCredential(credential);
		if (record == null) {
			throw AccountException.ACCOUNT_FROZEN_RECORD_IS_NOT_EXIST
					.newInstance("授权码{0}对应的账户冻结记录不存在", new Object[] {
							accountNo, param.getInitiator() });
		}

		// 更新账户状态
		minusCalculateAccountStatusCode(account, record.getOperationType());
		this.updateAccountStatus(account);

		//保存账户管理历史
		saveAccountManagementRecord(param,account,account.getAccountStatus(),
				AccountOperationEnum.UNFREEZE_ACCOUNT,null);

		// 删除账户冻结记录
		accountFrozenRecordDao.deleteAccountFrozenRecord(record);
	}

	/**
	 * 注销账户
	 * 
	 * @param accountNo
	 */
	@Override
	public void removeAccount(String accountNo, ContextParam baseParam)
			throws AccountException {

		// 获取账户示例
		Account account = queryAccountByAccountNo(accountNo);

		BigDecimal balance = account.getBalance();
		if (BigDecimal.ZERO.compareTo(balance) != 0)
			throw AccountException.BALANCE_IS_NOT_ZERO.newInstance(
					"账号{0}的账户余额不为零", new Object[] { accountNo });

		// 注销账户
		account.cancelled();
		this.updateAccountStatus(account);

		//保存账户管理历史
		saveAccountManagementRecord(baseParam,account,AccountStatusEnum.ACCOUNT_CANCELLED,
				AccountOperationEnum.REMOVE_ACCOUNT,null);
	}

	/**
	 *  更改账户额度限制
	 */
	@Override
	public void updateAccountFrozenQuota(ContextParam param,
			Account account, BigDecimal frozenAmount, AccountOperationEnum operationType) {
		Map<String, Object> changedValueMap = new HashMap<String, Object>();
		changedValueMap.put("frozenQuota", frozenAmount);
		String changedValue = JSONUtils.toJsonString(changedValueMap);
		account.increaseManageSeralNum();
		AccountManagementRecord record = AccountManagementRecord.newInstance(
				param.getFlowID(), param.getInitiator(), account.getAccountNo(), account
						.getManageSerial(), operationType, changedValue, new Date());
		this.accountManagementRecordDao.saveAccountManagementRecord(record);
		accountDao.updateAccountQuota(account);
	}

	/**
	 * 自动解冻账户 for 定时器
	 * 
	 * @param currentDate
	 * @throws AccountException
	 */
	@Override
	public void autoProcessUnfreezeAccountForTimer(String accountNo,List<AccountFrozenRecord> frozenRecords)
			throws AccountException {
		Account account = queryAccountByAccountNo(accountNo);
		for(AccountFrozenRecord record : frozenRecords){
			minusCalculateAccountStatusCode(account,record.getOperationType());

			//保存账户管理历史
			ContextParam param = new ContextParam(CommonUtils.getUUID(),"timer");
			saveAccountManagementRecord(param,account,account.getAccountStatus(),
					AccountOperationEnum.UNFREEZE_ACCOUNT,null);
			
			// 删除账户冻结记录
			accountFrozenRecordDao.deleteAccountFrozenRecord(record);
		}
		
		// 更新账户状态
		this.updateAccountStatus(account);
	}
	
	@Override
	public List<AccountFrozenRecord> queryAccountFrozenRecordsByUnfrozenDate(
			Date date) {
		return accountFrozenRecordDao.queryAccountFrozenRecordsByUnfrozenDate(date);
	}
	
	/**
	 * 创建账户
	 * @param createAccountParam
	 * @param accountType
	 * @return
	 * @throws AccountException
	 */
	private Account doCreateAccount(CreateAccountParam createAccountParam,
			AccountProvider provider) throws AccountException {
		Account account = new Account();
		createAccountNo(createAccountParam, account);
		account.setAccountType(createAccountParam.getAccountType());
		account.setCurrencyEnum(CurrencyEnum.CNY);
		if(createAccountParam.getAccountType().equals("USD_ACCOUNT"))
			account.setCurrencyEnum(CurrencyEnum.USD);
		account.setAccountProviderId(provider.getId());
		account.setBalance(MathUtils.round(createAccountParam.getBalance()));
		String balanceSign = accountTransactionService.generateBalanceSign(account.getBalance());
		account.setBalanceSign(balanceSign);
		account.setCustomerNo(createAccountParam.getCustomerNo());
		account.setAccountHistorySerial(Long.valueOf(0));
		account.setManageSerial(Long.valueOf(0));
		account.setFrozenSerial(Long.valueOf(0));
		account.setVersion(Long.valueOf(0));
		account.setSnapshotDate(DateUtils.getDayStart(new Date()));
		account.activate();
		return account;
	}
	
	/**
	 * 创建账户
	 * @param createAccountParam
	 * @param accountType
	 * @return
	 * @throws AccountException
	 */
	private Account doCreateAccount(CreateAccountParam createAccountParam,
			AccountProvider provider,String status,Long accountHistorySerial) throws AccountException {
		Account account = new Account();
		createAccountNo(createAccountParam, account);
		account.setAccountType(createAccountParam.getAccountType());
		account.setCurrencyEnum(CurrencyEnum.CNY);
		account.setAccountProviderId(provider.getId());
		account.setBalance(MathUtils.round(createAccountParam.getBalance()));
		String balanceSign = accountTransactionService.generateBalanceSign(account.getBalance());
		account.setBalanceSign(balanceSign);
		account.setCustomerNo(createAccountParam.getCustomerNo());
		account.setAccountHistorySerial(accountHistorySerial == null?Long.valueOf(0):accountHistorySerial);
		account.setManageSerial(Long.valueOf(0));
		account.setFrozenSerial(Long.valueOf(0));
		account.setVersion(Long.valueOf(0));
		account.setSnapshotDate(DateUtils.getDayStart(new Date()));
		account.activate();
		if("ACCOUNT_FROZEN".equals(status))
			account.frozen();
		else if("ACCOUNT_FREEZE_DEBIT".equals(status))
			account.freezeDebit();
		else if("ACCOUNT_FREEZE_CREDIT".equals(status))
			account.freezeCredit();
		else if("ACCOUNT_CANCELLED".equals(status))
			account.cancelled();
		return account;
	}

	/**
	 * 验证余额是否为负
	 * 
	 * @param createAccountParam
	 * @throws AccountException
	 */
	private void validateBalanceIsNegative(CreateAccountParam createAccountParam)
			throws AccountException {
		if (createAccountParam.getBalance().compareTo(BigDecimal.ZERO) < 0)
			throw AccountException.INVALID_BALANCE.newInstance("余额不合法",new Object[] {});
	}

	/**
	 * 查询帐户提供方
	 * @param accountProviderCode
	 * @return
	 */
	private AccountProvider queryAccountProvider(String accountProviderCode){
		AccountProvider provider = accountProviderDao.queryAccountProvider(accountProviderCode);
		if(provider == null)
			throw AccountException.ACCOUNT_PROVIDER_NOT_EXIST.newInstance("编号为{0}的账户提供方不存在", accountProviderCode);
		return provider;
	}

	/**
	 * 创建账号并设置
	 * 
	 * @param createAccountParam
	 * @param account
	 */
	private void createAccountNo(CreateAccountParam createAccountParam,
			Account account) {
		if(accountSystemType == null){
			Map<String,String> mapProps = CommonUtils.loadProps("properties/account.properties");
			accountSystemType = mapProps.get("accountSystemType");
		}
			
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accountSystemType", accountSystemType);
		String accountNo = accountNumberGenerator.generateNumber(params);
		account.setAccountNo(accountNo);
	}
	
	/**
	 * 更新账户状态
	 * 
	 * @param account
	 */
	private void updateAccountStatus(Account account) {
		accountDao.updateAccountStatus(account);
	}

	/**
	 * 保存或更新账户冻结记录
	 * 
	 * @param tradeFlowId
	 * @param accountNo
	 * @param initiator
	 * @param operationType
	 * @throws AccountException
	 */
	private String saveOrUpdateAccountFrozenRecord(Account account,
			String tradeFlowId, String accountNo, String initiator,
			Date unfrozenDate, AccountOperationEnum operationType,
			String credential) throws AccountException {
		AccountFrozenRecord record = null;
		if (StringUtils.isEmpty(credential)) {
			// 根据账户操作类型(operationType),计算账户状态码（相加）
			addCalculateAccountStatusCode(account, operationType);
			record = AccountFrozenRecord.newInstance(tradeFlowId, accountNo,
					initiator, operationType, UUID.randomUUID().toString(),
					new Date(), unfrozenDate);
			accountFrozenRecordDao.saveAccountFrozenRecord(record);
			return record.getCredential();
		} else {
			record = queryAccountFrozenRecord(credential);

			// 根据原有账户冻结记录中的账户账户操作类型(operationType)，计算账户状态码(相减)
			minusCalculateAccountStatusCode(account, record.getOperationType());

			// 根据账户操作类型(operationType),计算账户状态码（相加）
			addCalculateAccountStatusCode(account, operationType);
			account.setManageSerial(account.getManageSerial() - 1);
			record.setOperationType(operationType);
			record.setAutoUnfrozenDate(unfrozenDate);
			record.setLastModifyDate(new Date());
			accountFrozenRecordDao.updateAccountFrozenRecord(record);
			return credential;
		}
	}

	/**
	 * 根据账户操作类型(operationType),计算账户状态码（相加）
	 * 
	 * @param account
	 * @param operationType
	 */
	private void addCalculateAccountStatusCode(Account account,
			AccountOperationEnum operationType) {
		if (operationType == AccountOperationEnum.FREEZE_ACCOUNT) {
			account.frozen();
		} else if (operationType == AccountOperationEnum.FREEZE_CREDIT) {
			account.freezeCredit();
		} else if (operationType == AccountOperationEnum.FREEZE_DEBIT) {
			account.freezeDebit();
		}
	}

	/**
	 * 根据账户操作类型(operationType),计算账户状态码（相减）
	 * 
	 * @param account
	 * @param operationType
	 */
	private void minusCalculateAccountStatusCode(Account account,
			AccountOperationEnum operationType) {
		if (AccountOperationEnum.FREEZE_ACCOUNT == operationType) {
			account.unfrozen();
		} else if (AccountOperationEnum.FREEZE_DEBIT == operationType) {
			account.unfreezeDebit();
		} else if (AccountOperationEnum.FREEZE_CREDIT == operationType) {
			account.unfreezeCredit();
		}
	}

	/**
	 * 验证账户是否存在，如果存在，返回账户，不存在，抛出账户不存在异常
	 * 
	 * @param account
	 * @throws AccountException
	 */
	private Account queryAccountByAccountNo(String accountNo)
			throws AccountException {
		Account account = accountDao.queryAccountByAccountNo(
				accountNo);
		if (account == null)
			throw AccountException.ACCOUNT_ISNOT_EXIST.newInstance(
					"账号{0}的账户不存在", new Object[] { accountNo });
		return account;
	}
	
	/**
	 * 查询账户冻结记录
	 * @param credential
	 * @return
	 */
	private AccountFrozenRecord queryAccountFrozenRecord(String credential){
		AccountFrozenRecord record = accountFrozenRecordDao.queryAccountFrozenRecordByCredential(credential);
		if(record == null)
			throw AccountException.ACCOUNT_FROZEN_RECORD_IS_NOT_EXIST.newInstance("授权码{0}对应的账户冻结记录不存在",
					new Object[] { credential });
		return record;
	}
	
	/**
	 * 保存账户管理历史
	 * @param param
	 * @param account
	 * @param status
	 * @param operationType
	 * @param unfrozenDate
	 */
	private void saveAccountManagementRecord(ContextParam param,Account account,AccountStatusEnum status,
			AccountOperationEnum operationType,Date unfrozenDate){
		
		Map<String, Object> changedValueMap = new HashMap<String, Object>();
		changedValueMap.put("accountStatus",status.name());
		if(unfrozenDate != null)
			changedValueMap.put("unfrozenDate", unfrozenDate);
		String changedValue = JSONUtils.toJsonString(changedValueMap);
		AccountManagementRecord manageRecord = AccountManagementRecord
				.newInstance(param.getFlowID(), param.getInitiator(),
						account.getAccountNo(), account.getManageSerial(),
						operationType, changedValue,new Date());
		this.accountManagementRecordDao
				.saveAccountManagementRecord(manageRecord);
	}
	
	/**
	 * 保存帐户快照
	 * @param accountNo
	 * @param balance
	 */
	private void saveAccountSnapshot(String accountNo,BigDecimal balance){
		AccountSnapshot snapshot = new AccountSnapshot();
		snapshot.setAccountNo(accountNo);
		snapshot.setBalance(balance);
		snapshot.setSnapDate(DateUtils.getDayStart(new Date()));
		accountSnapshotDao.add(snapshot);
	}
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setAccountNumberGenerator(NumberGenerator accountNumberGenerator) {
		this.accountNumberGenerator = accountNumberGenerator;
	}

	public void setAccountManagementRecordDao(
			AccountManagementRecordDao accountManagementRecordDao) {
		this.accountManagementRecordDao = accountManagementRecordDao;
	}

	public void setAccountFrozenRecordDao(
			AccountFrozenRecordDao accountFrozenRecordDao) {
		this.accountFrozenRecordDao = accountFrozenRecordDao;
	}

	public void setAccountProviderDao(AccountProviderDao accountProviderDao) {
		this.accountProviderDao = accountProviderDao;
	}

	public void setAccountSystemType(String accountSystemType) {
		this.accountSystemType = accountSystemType;
	}

	public void setAccountTransactionService(
			AccountTransactionService accountTransactionService) {
		this.accountTransactionService = accountTransactionService;
	}

	public void setAccountSnapshotDao(AccountSnapshotDao accountSnapshotDao) {
		this.accountSnapshotDao = accountSnapshotDao;
	}


}
