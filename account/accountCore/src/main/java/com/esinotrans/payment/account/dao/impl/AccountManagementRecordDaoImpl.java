/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.AccountManagementRecordDao;
import com.esinotrans.payment.account.entity.AccountManagementRecord;

/**
 * 
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:02:36
 * @version 1.0
 */
public class AccountManagementRecordDaoImpl extends SqlSessionDaoSupport
		implements AccountManagementRecordDao {

	/**
	 * 保存 账户管理历史
	 * @param accountManagementRecord
	 * @throws DAOException
	 */
	public void saveAccountManagementRecord(AccountManagementRecord accountManagementRecord){
		super.getSqlSession().insert("insertAccountManagementRecord", accountManagementRecord);
	}
	
}
