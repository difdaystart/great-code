/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service;

/**
 * 定时器业务服务
 * @company YeePay
 * @author xingwei.bi
 * @since 2010-9-7
 * @version 1.0
 */
public interface SchedualBussinessService {
	
	/**
	 * 自动解冻资金
	 */
	public void autoUnfrozenFund();
}
