/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotranse.payment.utils.MathUtils;

/**
 * 账户历史
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:05:06
 * @version 1.0
 */
public class AccountHistory implements Serializable {

	private static final long serialVersionUID = -13393325339661385L;

	private Long id;

	/** 交易流水号 */
	private String tradeFlowId;

	/** 账户 */
	private String accountNo;

	/** 资金变动方向 */
	private FundChangeDirectionEnum direction;

	/** 交易类型 */
	private String tradeType;

	/** 发生额 */
	private BigDecimal amount = new BigDecimal(0.00);
	
	/** 当前余额 */
	private BigDecimal balance = new BigDecimal(0.00);

	/** 顺序号 */
	private Long serialNum;

	/** 创建日期 */
	private Date createDate;
	
	/** 
	 * 摘要信息
	 */
	private String summary;
	
	/**
	 * 用户备注
	 */
	private String userRemark;
	
	/**
	 * 系统备注
	 */
	private String systemRemark;

	/**
	 * 创建账户历史
	 * @param tradeFlowId
	 * @param accountNo
	 * @param direction
	 * @param tradeType
	 * @param amount
	 * @param serialNum
	 * @param createDate
	 * @param description
	 * @return
	 */
	public static AccountHistory newInstance(String tradeFlowId,
			String accountNo, FundChangeDirectionEnum direction,
			String tradeType, BigDecimal amount,BigDecimal balance, Long serialNum,
			Date createDate, String summary,String userRemark) {
		AccountHistory history = new AccountHistory();
		history.setTradeFlowId(tradeFlowId);
		history.setAccountNo(accountNo);
		history.setDirection(direction);
		history.setTradeType(tradeType);
		history.setAmount(amount);
		history.setBalance(balance);
		history.setSerialNum(serialNum);
		if(createDate == null)
			createDate = new Date();
		history.setCreateDate(createDate);
		if(summary != null && summary.length() > 200)
			summary = summary.substring(0,200);
		history.setSummary(summary);
		if(userRemark != null && userRemark.length() > 200)
			userRemark = userRemark.substring(0,200);
		history.setUserRemark(userRemark);
		return history;
	}
	
	/**
	 * 创建账户历史
	 * @param tradeFlowId
	 * @param accountNo
	 * @param direction
	 * @param tradeType
	 * @param amount
	 * @param serialNum
	 * @param createDate
	 * @param description
	 * @return
	 */
	public static AccountHistory newInstance(String tradeFlowId,
			String accountNo, FundChangeDirectionEnum direction,
			String tradeType, BigDecimal amount,BigDecimal balance, Long serialNum,
			Date createDate, String summary,String userRemark,String systemRemark) {
		AccountHistory history = new AccountHistory();
		history.setTradeFlowId(tradeFlowId);
		history.setAccountNo(accountNo);
		history.setDirection(direction);
		history.setTradeType(tradeType);
		history.setAmount(amount);
		history.setBalance(balance);
		history.setSerialNum(serialNum);
		if(createDate == null)
			createDate = new Date();
		history.setCreateDate(createDate);
		if(summary != null && summary.length() > 200)
			summary = summary.substring(0,200);
		history.setSummary(summary);
		if(userRemark != null && userRemark.length() > 200)
			userRemark = userRemark.substring(0,200);
		history.setUserRemark(userRemark);
		history.setSystemRemark(systemRemark);
		return history;
	}
	
	/**
	 * 四舍五入发生额
	 */
	public void roundAmount() {
		this.amount = MathUtils.round(this.amount);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FundChangeDirectionEnum getDirection() {
		return direction;
	}

	public void setDirection(FundChangeDirectionEnum direction) {
		this.direction = direction;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getTradeFlowId() {
		return tradeFlowId;
	}

	public void setTradeFlowId(String tradeFlowId) {
		this.tradeFlowId = tradeFlowId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public String getSystemRemark() {
		return systemRemark;
	}

	public void setSystemRemark(String systemRemark) {
		this.systemRemark = systemRemark;
	}

}
