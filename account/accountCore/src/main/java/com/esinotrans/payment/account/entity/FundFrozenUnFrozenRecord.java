/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.esinotrans.payment.account.enums.AccountOperationEnum;
import com.esinotranse.payment.utils.MathUtils;

/**
 * 资金冻结解冻记录
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:07:53
 * @version 1.0
 */
public class FundFrozenUnFrozenRecord implements Serializable{

	private static final long serialVersionUID = -4816696585174813009L;

	private Long id;

	/** 流水号 */
	private String tradeFlowId;

	/** 账户 */
	private String accountNo;

	/** 发起方 */
	private String initiator;

	/** 创建时间 */
	private Date createDate;

	/** 交易类型 */
	private AccountOperationEnum operationType;

	/** 冻结金额 */
	private BigDecimal frozenAmount = BigDecimal.ZERO;

	/** 已解冻金额 */
	private BigDecimal unfrozenAmount = BigDecimal.ZERO;

	/** 授权码 */
	private String credential;

	/** 最后修改时间 */
	private Date lastModifyDate;

	/** 自动解冻时间 */
	private Date autoUnfrozenDate;

	/** 顺序号 */
	private Long serialNum;
	
	/** 版本号 */
	public Long version;

	/** 备注 */
	private String comment;

	public FundFrozenUnFrozenRecord() {

	}

	/**
	 * 创建冻结解冻记录
	 * @param tradeFlowId
	 * @param accountNo
	 * @param initiator
	 * @param operationType
	 * @param frozenAmount
	 * @param credential
	 * @param serialNum
	 * @param autoUnfrozenDate
	 * @param lastModifyDate
	 * @param comment
	 * @return
	 */
	public static FundFrozenUnFrozenRecord newIntance(String tradeFlowId,
			String accountNo, String initiator,
			AccountOperationEnum operationType, BigDecimal frozenAmount,
			String credential, Long serialNum, Date autoUnfrozenDate,
			Date lastModifyDate, String comment,Long version) {
		FundFrozenUnFrozenRecord record = new FundFrozenUnFrozenRecord();
		record.setTradeFlowId(tradeFlowId);
		record.setAccountNo(accountNo);
		record.setInitiator(initiator);
		record.setOperationType(operationType);
		record.setCredential(credential);
		record.setFrozenAmount(frozenAmount);
		record.setSerialNum(serialNum);
		record.setVersion(version);
		record.setAutoUnfrozenDate(autoUnfrozenDate);
		if(lastModifyDate == null)
			record.setLastModifyDate(new Date());
		else
			record.setLastModifyDate(lastModifyDate);
		record.setCreateDate(new Date());
		record.setComment(comment);
		return record;
	}
	
	/**
	 * 计算可解冻金额
	 * @return
	 */
	public BigDecimal caculateAvailableUnfrozenAmount(){
		return this.frozenAmount.subtract(this.unfrozenAmount);
	}
	
	/**
	 * 增加解冻金额
	 * @param amount
	 */
	public void increaseUnfrozenAmount(BigDecimal amount){
		this.unfrozenAmount = this.unfrozenAmount.add(amount);
	}
	
	/**
	 * 是否能够解冻资金
	 * @param amount
	 * @return
	 */
	public boolean canUnfrozenFund(BigDecimal amount){
		return this.frozenAmount.compareTo(this.unfrozenAmount.add(amount)) >= 0;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public AccountOperationEnum getOperationType() {
		return operationType;
	}

	public void setOperationType(AccountOperationEnum operationType) {
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

	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTradeFlowId() {
		return tradeFlowId;
	}

	public void setTradeFlowId(String tradeFlowId) {
		this.tradeFlowId = tradeFlowId;
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * 四舍五入冻结和已解冻金额
	 */
	public void roundFrozenUnFrozenAmount() {
		this.frozenAmount = MathUtils.round(this.frozenAmount);
		this.unfrozenAmount = MathUtils.round(this.unfrozenAmount);
	}
}
