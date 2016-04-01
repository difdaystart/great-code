/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import com.esinotrans.payment.account.entity.FundFrozenUnFrozenHistory;

/**
 * 资金冻结解冻历史Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:59:15
 * @version 1.0
 */
public interface FundFrozenUnFrozenHistoryDao {
	
	/**
	 * 保存资金冻结解冻历史 
	 * @param fundFrozenUnFrozenHistory 资金冻结解冻历史
	 */
	public void saveFundFrozenUnFrozenHistory(FundFrozenUnFrozenHistory fundFrozenUnFrozenHistory);

}
