/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.esinotrans.payment.account.dto.AccountHistoryParam;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.AccountProvider;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenRecord;

/**
 * 账户查询服务
 * @company YeePay
 * @author 王伟
 * @since 2010-8-18
 * @version 1.0
 */
public interface AccountQueryService {

	/**
	 * 获得账户信息
	 * @param accountNo
	 * @return 账户
	 */
	public Account getAccountByAccountNo(String accountNo);
	
	/**
	 * 获得此客户拥有的所有账户
	 * @param customerID 客户ID
	 * @return 账户列表
	 */
	public List<Account> getAccountsByCustomerNo(String customerNo);
	
	/**
	 * 获得此账户历史记录
	 * @param accountNo
	 * @param startDate
	 * @param endDate
	 * @return 账户历史列表
	 */
	public List<AccountHistoryParam> getAccountHistory(String accountNo, Date startDate, Date endDate);
	
	/**
	 * 获取此帐户的资金冻结解冻记录
	 * @param accountNo
	 * 			账户号
	 * @return
	 */
	public List<FundFrozenUnFrozenRecord> getFundFrozenRecords(String accountNo);
	
	/**
	 * 查询账户提供方
	 * @param id
	 * @return
	 */
//	@Cache(name="queryAccountProvider", cacheRegion="account")
	public AccountProvider queryAccountProvider(Long id);
	
}
