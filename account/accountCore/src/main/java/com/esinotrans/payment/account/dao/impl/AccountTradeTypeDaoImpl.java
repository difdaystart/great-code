/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.AccountTradeTypeDao;
import com.esinotrans.payment.account.entity.AccountTradeType;

/**
 * 账务类型Dao实现
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:03:20
 * @version 1.0
 */
public class AccountTradeTypeDaoImpl extends SqlSessionDaoSupport implements
		AccountTradeTypeDao {

	@Override
	public AccountTradeType getAccountTradeType(Integer typeCode) {
		return (AccountTradeType)super.getSqlSession().selectOne("findAccountTradeTypeByCode", typeCode);
	}

	@Override
	public void saveAccountTradeType(AccountTradeType accountTradeType) {
		super.getSqlSession().insert("insertAccountTradeType", accountTradeType);
	}
}
