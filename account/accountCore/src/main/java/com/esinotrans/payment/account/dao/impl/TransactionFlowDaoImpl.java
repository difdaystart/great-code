/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.TransactionFlowDao;
import com.esinotrans.payment.account.entity.TransactionFlow;
import com.esinotranse.payment.utils.StringUtils;

/**
 * 
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:04:41
 * @version 1.0
 */
public class TransactionFlowDaoImpl extends SqlSessionDaoSupport implements TransactionFlowDao {

	@Override
	public void saveTransactionFlow(TransactionFlow flow){
		super.getSqlSession().insert("insertTransactionFlow", flow);
	}
	
	/**
	 * 根据流水ID(flowId),判断该流水是否已存在
	 * @param flowId
	 * @return true:存在，false:不存在
	 */
	public boolean isExistRecordByFlowId(String flowId){
		String queryStr = (String)super.getSqlSession().selectOne("isExistRecordByFlowId", flowId);
		if(StringUtils.isEmpty(queryStr)){
			return false;
		}
		return true;
	}

}
