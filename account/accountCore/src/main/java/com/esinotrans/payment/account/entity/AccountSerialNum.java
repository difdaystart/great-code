/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;

/**
 * 账号顺序号
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:07:30
 * @version 1.0
 */
public class AccountSerialNum implements Serializable{

	private static final long serialVersionUID = 295600142147436253L;

	private Long id;
	
	/**
	 * 流水号
	 */
	private Long flowId;
	
	/**
	 * 前缀数字
	 */
	private String prefixNum;
	
	/**
	 * 账户系统类型
	 */
	private String accountSystemType;
	
	public AccountSerialNum(){
		
	}
	
	/**
	 * 增加流水号
	 */
	public void increaseSerialNumByOne(){
		flowId = flowId.longValue() + 1;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public String getPrefixNum() {
		return prefixNum;
	}

	public void setPrefixNum(String prefixNum) {
		this.prefixNum = prefixNum;
	}

	public String getAccountSystemType() {
		return accountSystemType;
	}

	public void setAccountSystemType(String accountSystemType) {
		this.accountSystemType = accountSystemType;
	}
}
