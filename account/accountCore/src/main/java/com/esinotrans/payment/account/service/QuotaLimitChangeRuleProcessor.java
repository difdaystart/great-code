/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service;

import java.math.BigDecimal;

import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.exception.AccountException;

/**
 * 额度限制变更规则处理器
 * 
 * @company YeePay
 * @author 王伟
 * @since 2010-8-29
 * @version 1.0
 */
public interface QuotaLimitChangeRuleProcessor {

	/**
	 * 处理额度限制变更规则
	 * @param account
	 * @param tradeType
	 * @param amount
	 * @throws AccountException
	 */
	public void processQuotaLimitChangeRule(Account account,
			String tradeType, BigDecimal amount)
			throws AccountException;
}
