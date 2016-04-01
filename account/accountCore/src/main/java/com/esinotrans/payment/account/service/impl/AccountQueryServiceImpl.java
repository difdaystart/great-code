/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.esinotrans.payment.account.dao.AccountDao;
import com.esinotrans.payment.account.dao.AccountHistoryDao;
import com.esinotrans.payment.account.dao.AccountProviderDao;
import com.esinotrans.payment.account.dao.FundFrozenUnFrozenRecordDao;
import com.esinotrans.payment.account.dto.AccountHistoryParam;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.AccountHistory;
import com.esinotrans.payment.account.entity.AccountProvider;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenRecord;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.service.AccountQueryService;
import com.esinotranse.payment.utils.BeanUtils;
import com.esinotranse.payment.utils.CheckUtils;

/**
 * 账户查询服务 实现类
 * @company YeePay
 * @author 王伟
 * @since 2010-8-31
 * @version 1.0
 */
public class AccountQueryServiceImpl implements
		AccountQueryService {

	private AccountDao accountDao;											//账户实体DAO
	
	private AccountHistoryDao accountHistoryDao;							//账户历史Dao
	
	private FundFrozenUnFrozenRecordDao fundFrozenUnFrozenRecordDao;		//资金冻结解冻记录Dao
	
	private AccountProviderDao accountProviderDao;							//账户提供方实体DAO

	@Override
	public Account getAccountByAccountNo(String accountNo) {
		Account account = accountDao.queryAccountByAccountNo(accountNo);
		if(account == null)
			throw AccountException.ACCOUNT_ISNOT_EXIST.newInstance("帐号为：{0}的帐户不存在", accountNo);
		return account;
	}

	@Override
	public List<Account> getAccountsByCustomerNo(String customerNo) {
		return accountDao.getAccountsByCustomerNo(customerNo);
	}

	@Override
	public List<AccountHistoryParam> getAccountHistory(String accountNo,
			Date startDate, Date endDate) {
		CheckUtils.valueIsNull(accountNo, "accountNo");
		List<AccountHistory> historys = accountHistoryDao
				.findAccountHistoryByAccountNo(accountNo, startDate, endDate);
		List<AccountHistoryParam> historyParams = new ArrayList<AccountHistoryParam>();
		if (historys != null) {
			for (AccountHistory history : historys) {
				AccountHistoryParam historyParam = new AccountHistoryParam();
				BeanUtils.copyProperties(history, historyParam);
				historyParam.setDescription(history.getUserRemark());
				historyParams.add(historyParam);
			}
		}
		return historyParams;

	}
	
	/**
	 * 获取此帐户的资金冻结解冻记录
	 */
	@Override
	public List<FundFrozenUnFrozenRecord> getFundFrozenRecords(String accountNo){
		return fundFrozenUnFrozenRecordDao.queryUnfrozenFundRecords(accountNo);
	}
	
	/**
	 * 查询账户提供方
	 * @param id
	 * @return
	 */
	@Override
	public AccountProvider queryAccountProvider(Long id){
		return accountProviderDao.queryAccountProviderById(id);
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setAccountHistoryDao(AccountHistoryDao accountHistoryDao) {
		this.accountHistoryDao = accountHistoryDao;
	}

	public void setFundFrozenUnFrozenRecordDao(
			FundFrozenUnFrozenRecordDao fundFrozenUnFrozenRecordDao) {
		this.fundFrozenUnFrozenRecordDao = fundFrozenUnFrozenRecordDao;
	}

	public void setAccountProviderDao(AccountProviderDao accountProviderDao) {
		this.accountProviderDao = accountProviderDao;
	}

}
