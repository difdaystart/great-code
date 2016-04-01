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
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.esinotrans.payment.account.dao.AccountDao;
import com.esinotrans.payment.account.dao.AccountHistoryDao;
import com.esinotrans.payment.account.dto.TransactionCommandParam;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.AccountHistory;
import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.lock.ILock;
import com.esinotrans.payment.account.lock.LockManager;
import com.esinotrans.payment.account.service.AccountTransactionService;
import com.esinotrans.payment.account.service.QuotaLimitChangeRuleProcessor;
import com.esinotranse.payment.utils.MathUtils;
import com.esinotranse.payment.utils.json.JSONUtils;

/**
 * 账户交易服务 抽象类
 * 
 * @company YeePay
 * @author xingwei.bi
 * @since 2010-10-29
 * @version 1.0
 */
public abstract class AbstractAccountTransactionMemoryLockService implements
		AccountTransactionService {

	@Autowired
	private QuotaLimitChangeRuleProcessor processor;

	private AccountHistoryDao accountHistoryDao;
	
	private Map<String, Account> cachedAccount = new WeakHashMap<String, Account>();

	/**
	 * 存入
	 */
	public void credit(TransactionCommandParam command) throws AccountException {
		Account account = null;
		ILock lock = LockManager.getAccountLock(command.getReceiverAccountNo());
		try {
			lock.lock();
			account = validateAccountExist(command.getReceiverAccountNo());
			validateCredit(account);
			account.credit(command.getAmount());
			afterTransaction(account, command,
					FundChangeDirectionEnum.INCREMENT);
			getAccountDao().updateAccountBalance(createCondition(account));
		} finally {
			lock.unlock();
		}
		saveAccountHistory(command, account, FundChangeDirectionEnum.INCREMENT);
	}

	/**
	 * 支出
	 */
	public void debit(TransactionCommandParam command) throws AccountException {
		Account account = null;
		ILock lock = LockManager.getAccountLock(command.getReceiverAccountNo());
		try {
			lock.lock();
			account = validateAccountExist(command.getPayerAccountNo());
			validateDebit(account, command.getAmount());
			account.debit(command.getAmount());
			afterTransaction(account, command, FundChangeDirectionEnum.DECREASE);
			getAccountDao().updateAccountBalance(createCondition(account));
		} finally {
			lock.unlock();
		}
		saveAccountHistory(command, account, FundChangeDirectionEnum.DECREASE);
	}

	/**
	 * 对于同一账户，进行批量存入
	 * 
	 * @param params
	 *            交易命令参数集合
	 */
	public void batchCreditForSameAccount(List<TransactionCommandParam> params)
			throws AccountException {
		TransactionCommandParam command = (TransactionCommandParam) params
				.get(0);
		BigDecimal totalAmount = BigDecimal.valueOf(0.0D);
		for (TransactionCommandParam element : params) {
			totalAmount = totalAmount.add(element.getAmount());
		}
		Account account = null;
		BigDecimal balance = new BigDecimal(0.00);
		ILock lock = LockManager.getAccountLock(command.getReceiverAccountNo());
		try {
			lock.lock();
			account = validateAccountExist(command.getReceiverAccountNo());
			validateCredit(account);
			balance = account.getBalance();				//当前余额
			account.credit(totalAmount);
			
			this.processor.processQuotaLimitChangeRule(account,
					command.getTradeType(), totalAmount);
			account.roundQuotaAndBalance();
			Map<String, Object> condition = createCondition(account);
			condition.put("accountHistorySerial", Long.valueOf(account
					.getAccountHistorySerial().longValue()
					+ params.size()));
			getAccountDao().updateAccountBalance(condition);
		} finally {
			lock.unlock();
		}
		saveAccountHistoryList(params, account,
				FundChangeDirectionEnum.INCREMENT,balance);
	}

	/**
	 * 存入扣款都处理。修改额度、创建账户历史
	 * 
	 * @param account
	 * @param command
	 * @param direction
	 * @throws AccountException
	 */
	private void afterTransaction(Account account,
			TransactionCommandParam command, FundChangeDirectionEnum direction)
			throws AccountException {
		this.processor.processQuotaLimitChangeRule(account,
				command.getTradeType(), command.getAmount());
		account.roundQuotaAndBalance();
	}

	private Map<String, Object> createCondition(Account account) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("accountNo", account.getAccountNo());
		condition.put("balance", account.getBalance());
		condition.put("availableWithdrawQuota", account
				.getAvailableWithdrawQuota());
		condition.put("frozenQuota", account.getFrozenQuota());
		condition.put("accountHistorySerial", Long.valueOf(account
				.getAccountHistorySerial().longValue() + 1L));
//		condition.put("version", account.getVersion());
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
		String summaryContent = JSONUtils.toJsonString(command.getSummary());
		AccountHistory accountHistory = AccountHistory.newInstance(command
				.getFlowID(), account.getAccountNo(), direction, command
				.getTradeType(), command.getAmount(),account.getBalance(), Long
				.valueOf(account.getAccountHistorySerial().longValue() + 1L),
				new Date(), summaryContent,command.getUserRemark());
		accountHistory.roundAmount();
		this.accountHistoryDao.saveAccountHistory(accountHistory);
	}

	/**
	 * 保存账户历史 集合
	 * 
	 * @param command
	 * @param account
	 * @param direction
	 */
	private void saveAccountHistoryList(List<TransactionCommandParam> commands,
			Account account, FundChangeDirectionEnum direction,BigDecimal balance) {
		List<AccountHistory> accountHistorys = new ArrayList<AccountHistory>();
		Date currentDate = new Date();
		int i = 0;
		for (int size = commands.size(); i < size; ++i) {
			TransactionCommandParam command = (TransactionCommandParam) commands
					.get(i);
			balance = balance.add(command.getAmount());
			String summaryContent = JSONUtils.toJsonString(command.getSummary());
			AccountHistory accountHistory = AccountHistory.newInstance(command
					.getFlowID(), account.getAccountNo(), direction, command
					.getTradeType(), command.getAmount(),balance, Long
					.valueOf(account.getAccountHistorySerial().longValue() + i
							+ 1), currentDate, summaryContent,command.getUserRemark());
			accountHistory.roundAmount();
			accountHistorys.add(accountHistory);
		}
		this.accountHistoryDao.saveAccountHistoryList(accountHistorys);
	}

	private Account validateAccountExist(String accountNo)
			throws AccountException {
		Account account = null;
		account = cachedAccount.get(accountNo);
		if (account == null) {
			account = getAccountDao().queryAccountByAccountNo(accountNo);
			if (account == null)
				throw AccountException.ACCOUNT_ISNOT_EXIST.newInstance(
						"账号：{0}对应的账户不存在", new Object[] { accountNo });
			cachedAccount.put(accountNo, account);
		}
		return account;
	}

	protected void validateCredit(Account account) throws AccountException {
		if (!(account.validateCreditAccountStatus()))
			throw AccountException.ACCOUNT_STATUS_IS_NOT_VALID.newInstance(
					"账户：{0} 状态不合法", new Object[] { account.getAccountNo() });
	}

	protected void validateDebit(Account account, BigDecimal amount)
			throws AccountException {
		if (!(account.validateDebitAccountStatus()))
			throw AccountException.ACCOUNT_STATUS_IS_NOT_VALID.newInstance(
					"账户：{0} 状态不合法", new Object[] { account.getAccountNo() });
		if (!(account.availableBalanceIsEnough(amount)))
			throw AccountException.AVAILABLE_BALANCE_ISNOT_ENOUGH.newInstance(
					"账户：{0} 可用余额不足. 可用余额为：{1}， 需要扣除的额度为：{2}", new Object[] {
							account.getAccountNo(),
							MathUtils.round(account.getAvailableBalance()),
							MathUtils.round(amount) });
	}

	public QuotaLimitChangeRuleProcessor getProcessor() {
		return this.processor;
	}

	public void setProcessor(QuotaLimitChangeRuleProcessor processor) {
		this.processor = processor;
	}

	public AccountHistoryDao getAccountHistoryDao() {
		return this.accountHistoryDao;
	}

	@Autowired
	public void setAccountHistoryDao(AccountHistoryDao accountHistoryDao) {
		this.accountHistoryDao = accountHistoryDao;
	}

	public abstract AccountDao getAccountDao();

	public abstract void setAccountDao(AccountDao paramIAccountDao);
}