/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.esinotranse.payment.utils.MathUtils;
import com.esinotranse.payment.utils.StringUtils;

/**
 * 创建账户参数
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午11:00:00
 * @version 1.0
 */
public final class CreateAccountParam implements Serializable {

	private static final long serialVersionUID = -1340510607498936164L;
	
	/**
	 * 账户类型
	 */
	private String accountType;
	
	/**
	 * 账户余额
	 */
	private BigDecimal balance = BigDecimal.ZERO;
	
	/**
	 * 客户编号
	 */
	private String customerNo;
	
	/**
	 * 帐户提供方编号
	 */
	private String accountProviderCode;

	public CreateAccountParam(){
	
	}
	
	public CreateAccountParam(String accountType, String customerNo,String accountProviderCode) {
		this.accountType = accountType;
		this.customerNo = customerNo;
		this.accountProviderCode = accountProviderCode;
	}
	
	public CreateAccountParam(String accountType, String customerNo, String accountProviderCode,BigDecimal balance){
		this.accountType = accountType;
		this.customerNo = customerNo;
		this.accountProviderCode = accountProviderCode;
		setBalance(balance);
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		if(balance == null)
			this.balance = BigDecimal.ZERO;
		else
			this.balance = MathUtils.round(balance);
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getAccountProviderCode() {
		return accountProviderCode;
	}

	public void setAccountProviderCode(String accountProviderCode) {
		this.accountProviderCode = accountProviderCode;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(",flowID:");
		sb.append(",accountType :");
		sb.append(StringUtils.defaultIfNull(accountType));
		sb.append(",balance :");
		sb.append(balance);
		sb.append(",customerID :");
		sb.append(customerNo);
		return sb.toString();
	}
}
