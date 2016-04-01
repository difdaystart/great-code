/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金冻结记录 result DTO
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午10:59:53
 * @version 1.0
 */
public class FundFrozenRecordResultDTO implements Serializable{
	
	private static final long serialVersionUID = 4589357554683962657L;

	/** 流水号 */
	private String tradeFlowId;

	/** 账户 */
	private String accountNo;

	/** 发起方 */
	private String initiator;

	/** 交易类型 */
	private String operationType;

	/** 冻结金额 */
	private BigDecimal frozenAmount;

	/** 已解冻金额 */
	private BigDecimal unfrozenAmount;

	/** 授权码 */
	private String credential;

	/** 最后修改时间 */
	private Date lastModifyDate;

	/** 自动解冻时间 */
	private Date autoUnfrozenDate;
	
	/** 创建时间 */
	private Date createDate;

	/** 备注 */
	private String comment;
	
	public FundFrozenRecordResultDTO(){
		
	}

	public String getTradeFlowId() {
		return tradeFlowId;
	}

	public void setTradeFlowId(String tradeFlowId) {
		this.tradeFlowId = tradeFlowId;
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

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public BigDecimal getUnfrozenAmount() {
		return unfrozenAmount;
	}

	public void setUnfrozenAmount(BigDecimal unfrozenAmount) {
		this.unfrozenAmount = unfrozenAmount;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public Date getAutoUnfrozenDate() {
		return autoUnfrozenDate;
	}

	public void setAutoUnfrozenDate(Date autoUnfrozenDate) {
		this.autoUnfrozenDate = autoUnfrozenDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
