/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.AccountDao;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.enums.AccountStatusEnum;
import com.esinotrans.payment.account.exceptions.OptimisticLockingException;

/**
 * 付款账户管理DAO
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:01:58
 * @version 1.0
 */
public class AccountDaoImpl extends SqlSessionDaoSupport implements
		AccountDao {
	
	/**
	 * 根据账户编号(accountNo)，获取账户信息
	 * 
	 * @param accountNo
	 *            账户编号
	 * @return
	 */
	@Override
	public Account queryAccountByAccountNo(String accountNo) {
		return this.queryAccountByAccountNo(accountNo,true);
	}

	/**
	 * 根据账户编号(accountNo)，获取账户信息
	 * 
	 * @param accountNo
	 *            账户编号
	 * @param isPessimist
	 * 			是否添加悲观锁 true:添加,false:不添加
	 * @return
	 */
	@Override
	public Account queryAccountByAccountNo(String accountNo, boolean isPessimist) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accountNo", accountNo);
		params.put("accountStatus", AccountStatusEnum.ACCOUNT_CANCELLED);
		params.put("isPessimist", isPessimist);
		return (Account) super.getSqlSession().selectOne("selectAccountByAccountNo", params);
	}

	@Override
	public void saveAccount(Account account) {
		super.getSqlSession().insert("insertAccount", account);
	}

	/**
	 * 存入/支出时是更新账户余额,如果参数中带有额度信息，一并更新
	 * 
	 * @param condition
	 */
	@Override
	public void updateAccountBalance(Map<String,Object> condition){
		int row = super.getSqlSession().update("updateAccountBalance", condition);
		if (row == 0) {
			throw new OptimisticLockingException("乐观锁异常");
		}
	}

	/**
	 * 更改账户状态
	 * @param account
	 * @throws CommonException
	 */
	public void updateAccountStatus(Account account){
		int row = super.getSqlSession().update("updateAccountStatus", account);
		if (row == 0) {
			throw new OptimisticLockingException("乐观锁异常");
		}
	}

	@Override
	public List<Account> getAccountsByCustomerNo(String customerNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerNo", customerNo);
		params.put("accountStatus", AccountStatusEnum.ACCOUNT_CANCELLED);
		return super.getSqlSession().selectList("selectAccountByCustomsNo", params);
	}

	/**
	 * 更改账户额度限制
	 * 
	 * @param condition
	 */
	public void updateAccountQuota(Account account){
		int row = super.getSqlSession().update("updateAccountQuota", account);
		if (row == 0) {
			throw new OptimisticLockingException("乐观锁异常");
		}
	}
	
}
