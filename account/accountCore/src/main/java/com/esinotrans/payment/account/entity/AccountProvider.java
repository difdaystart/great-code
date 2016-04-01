/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;

/**
 * 账户提供方
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:05:18
 * @version 1.0
 */
public class AccountProvider implements Serializable{
	
	private static final long serialVersionUID = 890168036297909574L;

	private Long id ;
	
	/**客户ID*/
	private String customerID;
	
	/**提供方名称*/
	private String providerName;
	
	/**提供方编码*/
	private String providerCode ;
	
	/**描述*/
	private String description ;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getProviderCode() {
		return providerCode;
	}
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
