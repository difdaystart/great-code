/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import com.esinotrans.payment.account.entity.AccountTradeType;

/** 
 * 账务类型Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:59:09
 * @version 1.0
 */
public interface AccountTradeTypeDao {

	/**
	 * 保存账务类型
	 * @param accountTradeType
	 */
	public void saveAccountTradeType(AccountTradeType accountTradeType);
	
	/**
	 * 根据账务类型编码查询账务类型
	 * @param typeCode
	 * @return
	 */
	public AccountTradeType getAccountTradeType(Integer typeCode);
}
