/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.numbergenerator;

import java.util.Map;

/**
 * 号码生成器
 * @author 王伟
 *
 */
public interface NumberGenerator {

	/**
	 * 生成号码
	 * @param   params
	 * @return String
	 */
	public String generateNumber(Map<String, Object> params);
}
