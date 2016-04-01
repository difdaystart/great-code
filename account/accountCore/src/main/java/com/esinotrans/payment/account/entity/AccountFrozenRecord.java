/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.util.Date;

import com.esinotrans.payment.account.enums.AccountOperationEnum;

/**
 * 账户冻结记录
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:05:01
 * @version 1.0
 */
public class AccountFrozenRecord implements Serializable {

	private static final long serialVersionUID = 9140177706661193361L;

	private Long id;

	/** 流水号 */
	private String tradeFlowId;

	/** 账户 */
	private String accountNo;

	/** 发起方 */
	private String initiator;

	/** 账户操作类型 */
	private AccountOperationEnum operationType;
	
	/** 授权码 */
	private String credential;

	/** 最后修改时间 */
	private Date lastModifyDate;

	/** 自动解冻时间 */
	private Date autoUnfrozenDate;
	
	/**
	 * 冻结原因
	 */
	private String reason;
	
	/**
	 * 描述
	 */
	private String describe;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	public AccountFrozenRecord() {

	}

	/**
	 * @param tradeFlowId2
	 * @param accountNo2
	 * @param initiator2
	 * @param operationType2
	 * @param string
	 * @param date
	 * @param unfrozenDate
	 * @return
	 */
	public static AccountFrozenRecord newInstance(String tradeFlowId,
			String accountNo, String initiator,
			AccountOperationEnum operationType, String credential, Date lastModifyDate,
			Date autoUnfrozenDate) {
		return AccountFrozenRecord.newInstance(tradeFlowId, accountNo, initiator, operationType, credential, lastModifyDate, autoUnfrozenDate, null, null);
	}

	/**
	 * 创建账户冻结记录
	 * @param tradeFlowId
	 * @param accountNo
	 * @param initiator
	 * @param operationType
	 * @param lastModifyDate
	 * @param autoUnfrozenDate
	 * @return
	 */
	public static AccountFrozenRecord newInstance(String tradeFlowId,
			String accountNo, String initiator,AccountOperationEnum operationType, 
			String credential,Date lastModifyDate, Date autoUnfrozenDate,String reason,String describe) {
		AccountFrozenRecord record = new AccountFrozenRecord();
		record.setTradeFlowId(tradeFlowId);
		record.setAccountNo(accountNo);
		record.setInitiator(initiator);
		record.setOperationType(operationType);
		record.setCredential(credential);
		if(lastModifyDate == null)
			record.setLastModifyDate(new Date());
		else
			record.setLastModifyDate(lastModifyDate);
		record.setAutoUnfrozenDate(autoUnfrozenDate);
		record.setReason(reason);
		record.setDescribe(describe);
		record.setCreateDate(new Date());
		return record;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public AccountOperationEnum getOperationType() {
		return operationType;
	}

	public void setOperationType(AccountOperationEnum operationType) {
		this.operationType = operationType;
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

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
