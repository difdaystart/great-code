/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.AccountFrozenRecordDao;
import com.esinotrans.payment.account.entity.AccountFrozenRecord;

/**
 * 账户冻结记录DAO
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:02:09
 * @version 1.0
 */
public class AccountFrozenRecordDaoImpl extends SqlSessionDaoSupport
		implements AccountFrozenRecordDao {

	/**
	 * 删除账户冻结记录
	 * @param conditon
	 */
	@Override
	public void deleteAccountFrozenRecord(AccountFrozenRecord record) {
		this.getSqlSession().delete("AccountFrozenRecord.deleteAccountFrozenRecord",record);
	}

	/**
	 * 查询账户冻结记录
	 * @param condition
	 * @return
	 */
	public AccountFrozenRecord queryAccountFrozenRecord(String accountNo,String initiator){
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("accountNo", accountNo);
		condition.put("initiator", initiator);
		return (AccountFrozenRecord)super.getSqlSession().selectOne("queryAccountFrozenRecord", condition);
	}
	
	/**
	 * 根据授权码，查询账户冻结记录
	 * @param condition
	 * @return
	 */
	public AccountFrozenRecord queryAccountFrozenRecordByCredential(String credential){
		return (AccountFrozenRecord)super.getSqlSession().selectOne("queryAccountFrozenRecordByCredential", credential);
	}

	/**
	 * 保存账户冻结记录
	 * @param record
	 */
	@Override
	public void saveAccountFrozenRecord(AccountFrozenRecord record) {
		super.getSqlSession().insert("insertAccountFrozenRecord", record);

	}

	/**
	 * 更改账户冻结记录
	 * @param condition
	 */
	@Override
	public void updateAccountFrozenRecord(AccountFrozenRecord record) {
		super.getSqlSession().update("updateAccountFrozenRecord", record);

	}
	
	/**
	 * 根据自动解冻时间，查询账户冻结记录
	 * @param date
	 * @return
	 */
	@Override
	public List<AccountFrozenRecord> queryAccountFrozenRecordsByUnfrozenDate(Date date){
		return super.getSqlSession().selectList("queryAccountFrozenRecordsByUnfrozenDate", date);
	}

	@Override
	public List<AccountFrozenRecord> queryAccountFrozenRecord(String accountNo) {
		return super.getSqlSession().selectList("queryAccountFrozenRecordByAccountNo", accountNo);
	}

}
