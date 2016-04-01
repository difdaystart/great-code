/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import java.util.List;
import java.util.Map;

import com.esinotrans.payment.account.entity.Account;

/**
 * 账户Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:58:31
 * @version 1.0
 */
public interface AccountDao {
	
	
	/**
	 * 存入/支出时是更新账户余额,如果参数中带有额度信息，一并更新
	 * 
	 * @param condition
	 */
	public void updateAccountBalance(Map<String,Object> condition);
	
	/**
	 * 根据账户编号(accountNo)，获取账户信息
	 * @param accountNo  账户号
	 * @return Account
	 */
	public Account queryAccountByAccountNo(String accountNo);
	
	/**
	 * 根据账户编号(accountNo)，获取账户信息
	 * 
	 * @param accountNo
	 *            账户编号
	 * @param isPessimist
	 * 			是否添加悲观锁 true:添加,false:不添加
	 * @return
	 */
	public Account queryAccountByAccountNo(String accountNo, boolean isPessimist);
	
	/**
	 * 保存账户
	 * @param account
	 *
	 */
	public void saveAccount(Account account);
	
	/**
	 *  根据客户ID 来获取 账户列表
	 * @param customerID
	 * @return 账户列表
	 */
	public List<Account> getAccountsByCustomerNo(String customerNo);
	
	/**
	 * 更改账户状态
	 * @param account
	 * @throws CommonException
	 */
	public void updateAccountStatus(Account account);
	
	/**
	 * 更改账户额度限制
	 * @param condition (map对象 必需含有 账号，额度信息)
	 */
	public void updateAccountQuota(Account account);
	
}
