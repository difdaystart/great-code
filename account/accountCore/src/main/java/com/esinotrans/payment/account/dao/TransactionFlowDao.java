/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import com.esinotrans.payment.account.entity.TransactionFlow;


/**
 * 交易流水Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:01:52
 * @version 1.0
 */
public interface TransactionFlowDao {
  
	
	/**
	 * 保存交易流水
	 * @param flow 流水对象
	 */
	public void saveTransactionFlow(TransactionFlow flow);
	
	/**
	 * 根据流水ID(flowId),判断该流水是否已存在
	 * @param flowId 流水号
	 * @return true:存在，false:不存在
	 */
	public boolean isExistRecordByFlowId(String flowId);
}
