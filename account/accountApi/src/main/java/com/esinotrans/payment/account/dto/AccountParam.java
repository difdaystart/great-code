/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 账户参数
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午11:00:12
 * @version 1.0
 */
public final class AccountParam implements Serializable {
  
	private static final long serialVersionUID = -6749308710177014353L;

	/**
	 * 账号
	 */
	private String accountNo;
	
	/** 客户 */
	private String customerNo;
	
	/**账户类型 */
	private String accountType;
	
	/** 币种名称*/
	private String currency;
	
	/** 账户提供方编号*/
	private String accountProviderCode;
	
	/**
	 * 帐户提供方名称
	 */
	private String accountProviderName;
	
	/** 账户状态编码 */
	private Integer accountStatusCode;
	
	/** 账户状态名称 */
	private String accountStatusName;

	/** 账户余额 */
	private BigDecimal balance;

	/** 交易密码 */
	private String transactionPassword;

	/** 可提现额度 */
	private BigDecimal availableWithdrawQuota = BigDecimal.ZERO;

	/** 冻结额度 */
	private BigDecimal frozenQuota = BigDecimal.ZERO;

	public AccountParam(){
		
	}
	
	/**
	 * 判断帐户状态是否是冻结状态
	 * @return
	 */
	public boolean isFrozen(){
		if("ACCOUNT_FROZEN".equals(accountStatusName))
			return true;
		return false;
	}
	
	/**
	 * 判断帐户状态是否是冻结止付状态
	 * @return
	 */
	public boolean isFrozenDebit(){
		if("ACCOUNT_FREEZE_DEBIT".equals(accountStatusName))
			return true;
		return false;
	}
	
	/**
	 * 判断帐户状态是否是冻结止收状态
	 * @return
	 */
	public boolean isFrozenCredit(){
		if("ACCOUNT_FREEZE_CREDIT".equals(accountStatusName))
			return true;
		return false;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccountProviderCode() {
		return accountProviderCode;
	}

	public void setAccountProviderCode(String accountProviderCode) {
		this.accountProviderCode = accountProviderCode;
	}

	public String getAccountProviderName() {
		return accountProviderName;
	}

	public void setAccountProviderName(String accountProviderName) {
		this.accountProviderName = accountProviderName;
	}

	public Integer getAccountStatusCode() {
		return accountStatusCode;
	}

	public void setAccountStatusCode(Integer accountStatusCode) {
		this.accountStatusCode = accountStatusCode;
	}

	public String getAccountStatusName() {
		return accountStatusName;
	}

	public void setAccountStatusName(String accountStatusName) {
		this.accountStatusName = accountStatusName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getTransactionPassword() {
		return transactionPassword;
	}

	public void setTransactionPassword(String transactionPassword) {
		this.transactionPassword = transactionPassword;
	}

	public BigDecimal getAvailableWithdrawQuota() {
		return availableWithdrawQuota;
	}

	public void setAvailableWithdrawQuota(BigDecimal availableWithdrawQuota) {
		this.availableWithdrawQuota = availableWithdrawQuota;
	}

	public BigDecimal getFrozenQuota() {
		return frozenQuota;
	}

	public void setFrozenQuota(BigDecimal frozenQuota) {
		this.frozenQuota = frozenQuota;
	}
}
