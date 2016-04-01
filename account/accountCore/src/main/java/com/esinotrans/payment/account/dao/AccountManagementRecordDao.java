/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import com.esinotrans.payment.account.entity.AccountManagementRecord;

/**
 * 账户管理记录Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:58:50
 * @version 1.0
 */
public interface AccountManagementRecordDao {

	/**
	 * 保存 账户管理历史
	 * @param accountManagementRecord
	 */
	public void saveAccountManagementRecord(AccountManagementRecord accountManagementRecord);
}
