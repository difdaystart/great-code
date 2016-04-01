/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.util.Date;

import com.esinotrans.payment.account.enums.AccountOperationEnum;

/**
 * 账户管理历史
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:05:12
 * @version 1.0
 */
public class AccountManagementRecord implements Serializable{

	private static final long serialVersionUID = 562092144833454496L;

	private Long id;

	/** 流水号 */
	private String tradeFlowId;

	/** 发起方 */
	private String initiator;

	/** 创建时间 */
	private Date createDate = new Date();

	/** 顺序号 */
	private Long serialNum;

	/** 操作类型 */
	private AccountOperationEnum operationType;

	/** 账号 */
	private String accountNo;

	/** 改变后的值 */
	private Object changedValue;

	public AccountManagementRecord() {

	}

	/**
	 * 创建账户管理记录
	 * @param tradeFlowId
	 * @param initiator
	 * @param accountNo
	 * @param serialNum
	 * @param operationType
	 * @param changedValue
	 * @param createDate
	 * @return
	 */
	public static AccountManagementRecord newInstance(String tradeFlowId,
			String initiator, String accountNo, Long serialNum,
			AccountOperationEnum operationType, String changedValue,
			Date createDate) {
		AccountManagementRecord record = new AccountManagementRecord();
		record.setTradeFlowId(tradeFlowId);
		record.setInitiator(initiator);
		record.setAccountNo(accountNo);
		record.setSerialNum(serialNum);
		record.setOperationType(operationType);
		record.setChangedValue(changedValue);
		if(createDate == null)
			record.setCreateDate(new Date());
		else
			record.setCreateDate(createDate);
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

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}

	public AccountOperationEnum getOperationType() {
		return operationType;
	}

	public void setOperationType(AccountOperationEnum operationType) {
		this.operationType = operationType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Object getChangedValue() {
		return changedValue;
	}

	public void setChangedValue(Object changedValue) {
		this.changedValue = changedValue;
	}

}
