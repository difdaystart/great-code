/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.enums;

/** 
 * @company YeePay 
 * @author 王伟
 * @since 2010-9-13 
 * @version 1.0  
 */
public enum AccountTypeIndicatorEnum {
	
	PAYMENT_ACCOUNT("10"),
	RECEIVE_ACCOUNT("20-50"),
	CREDIT_ACCOUNT("11");
	
	public static final String PACKAGE = "com.esinotrans.payment.account.enums.";
	public static final String RECEIVE_ACCOUNT_SPLIT = "-";
	
	private String code;
	
	private AccountTypeIndicatorEnum(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
