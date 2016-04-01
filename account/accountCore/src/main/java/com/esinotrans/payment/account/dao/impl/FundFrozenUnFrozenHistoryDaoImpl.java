/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.FundFrozenUnFrozenHistoryDao;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenHistory;

/**
 * 
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:03:26
 * @version 1.0
 */
public class FundFrozenUnFrozenHistoryDaoImpl extends SqlSessionDaoSupport
		implements FundFrozenUnFrozenHistoryDao {

	/**
	 * 保存资金冻结解冻历史 
	 * @param fundFrozenUnFrozenHistory 资金冻结解冻历史
	 */
	@Override
	public void saveFundFrozenUnFrozenHistory(FundFrozenUnFrozenHistory fundFrozenUnFrozenHistory){
		super.getSqlSession().insert("insertFundFrozenHistory", fundFrozenUnFrozenHistory);
	}
}
