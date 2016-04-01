/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 交易流水
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:19:30
 * @version 1.0
 */
public class TransactionFlow implements Serializable{

	private static final long serialVersionUID = -6992980793235237646L;

	private Long id;

	/** 流水号 */
	private String flowNum;

	/** 账号 */
	private String accountNo;

	/** 发起方 */
	private String initiator;

	/** 交易类型 */
	private String tradeType;

	/** 创建时间 */
	private Date createDate = new Date();

	/** 描述 */
	private String description;

	public TransactionFlow() {

	}

	/**
	 * 创建交易流水
	 * @param flowNum
	 * @param accountNo
	 * @param initiator
	 * @param operationType
	 * @param createDate
	 * @param description
	 * @return
	 */
	public static TransactionFlow newInstance(String flowNum, String accountNo,
			String initiator, String tradeType, Date createDate,
			String description) {
		TransactionFlow flow = new TransactionFlow();
		flow.setFlowNum(flowNum);
		flow.setAccountNo(accountNo);
		flow.setInitiator(initiator);
		flow.setTradeType(tradeType);
		if(createDate == null)
			flow.setCreateDate(new Date());
		else
			flow.setCreateDate(createDate);
		flow.setDescription(description);
		return flow;
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

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getFlowNum() {
		return flowNum;
	}

	public void setFlowNum(String flowNum) {
		this.flowNum = flowNum;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
