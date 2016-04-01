/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.esinotrans.payment.account.enums.FrozenUnFrozenEnum;
import com.esinotranse.payment.utils.MathUtils;

/**
 * 资金冻结解冻历史
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:07:46
 * @version 1.0
 */
public class FundFrozenUnFrozenHistory implements Serializable{

	private static final long serialVersionUID = 1763187643423553867L;

	private Long id;
	
	/** 账户编号 */
	private String accountNo;
	
	/** 资金冻结解冻记录ID */
	private Long frozenUnFrozenRecordId;
	
	/** 冻结/解冻操作 */
	private FrozenUnFrozenEnum operationType;
	
	/** 冻结或解冻的额度 */
	private BigDecimal amount = BigDecimal.ZERO;
	
	/** 创建时间 */
	private Date createDate;

	public FundFrozenUnFrozenHistory() {

	}

	/**
	 * 创建冻结解冻历史
	 * @param accountNo
	 * @param frozenUnFrozenRecordId
	 * @param operationType
	 * @param amount
	 * @param createDate
	 * @return
	 */
	public static FundFrozenUnFrozenHistory newInstance(String accountNo,
			Long frozenUnFrozenRecordId, FrozenUnFrozenEnum operationType,
			BigDecimal amount, Date createDate) {
		FundFrozenUnFrozenHistory history = new FundFrozenUnFrozenHistory();
		history.setAccountNo(accountNo);
		history.setFrozenUnFrozenRecordId(frozenUnFrozenRecordId);
		history.setOperationType(operationType);
		history.setAmount(amount);
		if(createDate == null)
			history.setCreateDate(new Date());
		else
			history.setCreateDate(createDate);
		return history;
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

	public Long getFrozenUnFrozenRecordId() {
		return frozenUnFrozenRecordId;
	}

	public void setFrozenUnFrozenRecordId(Long frozenUnFrozenRecordId) {
		this.frozenUnFrozenRecordId = frozenUnFrozenRecordId;
	}

	public FrozenUnFrozenEnum getOperationType() {
		return operationType;
	}

	public void setOperationType(FrozenUnFrozenEnum operationType) {
		this.operationType = operationType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 四舍五入冻结或解冻金额
	 */
	public void roundFrozenUnFrozenAmount() {
		this.amount = MathUtils.round(this.amount);
	}
}
