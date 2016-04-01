/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.numbergenerator.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.esinotrans.payment.account.dao.SerialNumDao;
import com.esinotrans.payment.account.numbergenerator.NumberGenerator;
import com.esinotranse.payment.utils.StringUtils;

/**
 * 账号生成器
 * @author 王伟
 *
 */

public class AccountNumberGenerator implements NumberGenerator{

	private static final String Placeholder = "0";
	
	private static final int fillBitNum = 10;
	
	private SerialNumDao serialNumDao;

	@Override
	public String generateNumber(Map<String, Object> params) {
		String accountSystemType = (String)params.get("accountSystemType");
		Map<String,Object> serialMap = serialNumDao.getAccountSerialNum(accountSystemType);
		String target = StringUtils.fillBitsWithPlaceholder(Placeholder, (String)serialMap.get("serailNum"), fillBitNum);
		return (String)serialMap.get("prefixNum") + target;
	}
	
	public SerialNumDao getSerialNumDao() {
		return serialNumDao;
	}

	@Autowired
	public void setSerialNumDao(SerialNumDao serialNumDao) {
		this.serialNumDao = serialNumDao;
	}

}
