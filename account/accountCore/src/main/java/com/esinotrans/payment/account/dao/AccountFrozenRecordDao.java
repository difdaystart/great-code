/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import java.util.Date;
import java.util.List;

import com.esinotrans.payment.account.entity.AccountFrozenRecord;

/**
 *  账户冻结记录
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:58:37
 * @version 1.0
 */
public interface AccountFrozenRecordDao {
	
	/**
	 * 保存账户冻结记录
	 * @param record
	 */
	public void saveAccountFrozenRecord(AccountFrozenRecord record);
	
	/**
	 * 查询账户冻结记录
	 * @param condition
	 * @return
	 */
	public AccountFrozenRecord queryAccountFrozenRecord(String accountNo,String initiator);
	
	/**
	 * 查询账户冻结记录
	 * @param condition
	 * @return
	 */
	public List<AccountFrozenRecord> queryAccountFrozenRecord(String accountNo);
	
	/**
	 * 根据授权码，查询账户冻结记录
	 * @param condition
	 * @return
	 */
	public AccountFrozenRecord queryAccountFrozenRecordByCredential(String credential);
	
	/**
	 * 更改账户冻结记录
	 * @param condition
	 */
	public void updateAccountFrozenRecord(AccountFrozenRecord record);
	
	/**
	 * 删除账户冻结记录
	 * @param conditon
	 */
	public void deleteAccountFrozenRecord(AccountFrozenRecord record);
	
	/**
	 * 根据自动解冻时间，查询账户冻结记录
	 * @param date
	 * @return
	 */
	public List<AccountFrozenRecord> queryAccountFrozenRecordsByUnfrozenDate(Date date);
	
	

}
