/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.utils;

/**
 * @company YeePay
 * @author 王伟
 * @since 2010-8-24
 * @version 1.0
 */
public class NoConditionFieldFilter implements IFieldFilter {

	@Override
	public boolean filter(Object paramObject) {
		return false;
	}

}
