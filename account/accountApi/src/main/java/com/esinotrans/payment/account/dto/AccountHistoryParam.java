/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotranse.payment.utils.StringUtils;

/**
 * 账户历史参数
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午11:00:17
 * @version 1.0
 */
public final class AccountHistoryParam implements Serializable{
	
	private static final long serialVersionUID = 7924373562877696215L;
	
	private Long id;
	
	/**
	 * 交易流水号
	 */
	private String tradeFlowId;
	/**
	 * 账户
	 */
	private String accountNo;
	/**
	 * 资金变动方向 
	 */
	private FundChangeDirectionEnum direction;

	/**
	 * 交易类型
	 */
	private String tradeType;

	/**
	 * 发生额
	 */
	private BigDecimal amount;

	/**
	 * 顺序号
	 */
	private Long serialNum;

	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 描述
	 */
	private String description;
	
	public AccountHistoryParam(){
		
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String  toString(){
		StringBuffer  sb = new StringBuffer();
		sb.append("id: ");sb.append(id);
		sb.append(",tradeFlowId:");sb.append(StringUtils.defaultIfNull(tradeFlowId));
		sb.append(",accountNo :");sb.append(StringUtils.defaultIfNull(accountNo));
		sb.append(",direction :");sb.append(direction);
		sb.append(",tradeType :");sb.append(tradeType);
		sb.append(",amount :");sb.append(amount);
		sb.append(",serialNum :");sb.append(serialNum);
		sb.append(",createDate : ");sb.append(createDate);
		sb.append(",description :");sb.append(StringUtils.defaultIfNull(description));
		
		return sb.toString();
	}
	
}
