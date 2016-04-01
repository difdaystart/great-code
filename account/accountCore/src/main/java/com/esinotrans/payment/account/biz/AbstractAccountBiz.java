/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.biz;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.esinotrans.payment.account.dao.TransactionFlowDao;
import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.entity.TransactionFlow;
import com.esinotrans.payment.account.exception.AccountException;

/**
 * 抽象服务类
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-16 下午5:09:03
 * @version 1.0
 */
public abstract class AbstractAccountBiz {

	private TransactionFlowDao transactionFlowDao;

	/**
	 * 保存交易流水
	 * 
	 * @param accountNo
	 *            账号
	 * @param param
	 *            基本的参数对象 必须含有 流水号，发起方。
	 * @param operationType
	 */
	protected Long saveTransactionFlow(String accountNo, ContextParam param,
			String tradeType) {
		TransactionFlow flow = TransactionFlow.newInstance(param.getFlowID(), accountNo, param
				.getInitiator(), tradeType, new Date(), param
				.getDescription());
		try{
			transactionFlowDao.saveTransactionFlow(flow);
		}catch(DuplicateKeyException ex){
			throw AccountException.FLOW_ID_IS_EXIST.newInstance(
					"流水号{0}已经存在，该请求是重复请求", param.getFlowID());
		}
		return flow.getId();
	}
	
	/**
	 * 验证发生金额是否为负
	 * @param param
	 * @throws AccountException
	 */
	protected void validateAmountIsNegtive(BigDecimal amount) throws AccountException{
		if(amount.compareTo(BigDecimal.ZERO) < 0)
			throw AccountException.INVALID_BALANCE.newInstance("发生金额不能为负", new Object[]{});
	}
	
	@Autowired
	public void setTransactionFlowDao(TransactionFlowDao transactionFlowDao) {
		this.transactionFlowDao = transactionFlowDao;
	}
}
