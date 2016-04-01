/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 帐户快照表
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:07:35
 * @version 1.0
 */
public class AccountSnapshot implements Serializable{

	private static final long serialVersionUID = -6719356673881880984L;

	private Long id;
	
	/**
	 * 账户编号
	 */
	private String accountNo;
	
	/** 
	 * 账户余额
	 */
	private BigDecimal balance;
	
	/**
	 * 快照日期
	 */
	private Date snapDate;
	
	public AccountSnapshot(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Date getSnapDate() {
		return snapDate;
	}

	public void setSnapDate(Date snapDate) {
		this.snapDate = snapDate;
	}
}
