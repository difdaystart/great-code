/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;

import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotrans.payment.account.enums.QuotaTypeEnum;

/**
 * 额度限制变更规则
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:08:41
 * @version 1.0
 */
public class QuotaLimitChangeRule implements Serializable{

	private static final long serialVersionUID = -8286203664162662768L;

	private Long id;
	
	/** 交易类型 */
	private String tradeType;
	
	/** 变更方向 */
	private FundChangeDirectionEnum changeDirection;
	
	/** 额度类型 */
	private QuotaTypeEnum quotaType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public FundChangeDirectionEnum getChangeDirection() {
		return changeDirection;
	}
	public void setChangeDirection(FundChangeDirectionEnum changeDirection) {
		this.changeDirection = changeDirection;
	}
	public QuotaTypeEnum getQuotaType() {
		return quotaType;
	}
	public void setQuotaType(QuotaTypeEnum quotaType) {
		this.quotaType = quotaType;
	}
}
