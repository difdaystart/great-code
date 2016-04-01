/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotranse.payment.utils.MathUtils;

/**
 * 交易命令参数
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午10:57:08
 * @version 1.0
 */
public final class TransactionCommandParam extends ContextParam{

	private static final long serialVersionUID = 5141030346886486211L;

	/**
	 * 付款方 账号
	 */
	private String payerAccountNo;

	/**
	 * 收款方 账号
	 */
	private String receiverAccountNo;

	/**
	 * 发生额
	 */
	private BigDecimal amount = BigDecimal.ZERO;

	/**
	 * 交易类型
	 */
	private String tradeType;

	/**
	 * 资金方向
	 */
	private FundChangeDirectionEnum direction;
	
	/**
	 * 摘要信息
	 */
	private Map<String,Object> summary;
	
	/**
	 * 用户备注
	 */
	private String userRemark;
 
	public TransactionCommandParam(){
		
	}
	
	public TransactionCommandParam(String flowID, String initiator,
			String payerAccountNo, String receiverAccountNo, BigDecimal amount,
			String tradeType,Map<String,Object> summary) {
		super(flowID, initiator);
		this.payerAccountNo = payerAccountNo;
		this.receiverAccountNo = receiverAccountNo;
		this.setAmount(amount);
		this.tradeType = tradeType;
		this.summary = summary;
	}

	public String getPayerAccountNo() {
		return payerAccountNo;
	}

	public void setPayerAccountNo(String payerAccountNo) {
		this.payerAccountNo = payerAccountNo;
	}

	public String getReceiverAccountNo() {
		return receiverAccountNo;
	}

	public void setReceiverAccountNo(String receiverAccountNo) {
		this.receiverAccountNo = receiverAccountNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		if (amount == null)
			this.amount = BigDecimal.ZERO;
		else
			this.amount = MathUtils.round(amount);
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

	public Map<String, Object> getSummary() {
		return summary;
	}

	public void setSummary(Map<String, Object> summary) {
		this.summary = summary;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
}
