/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dto;

import java.io.Serializable;

import com.esinotranse.payment.utils.StringUtils;

/**
 * 账户基本参数
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午11:00:07
 * @version 1.0
 */
public class ContextParam implements Serializable{
	
	private static final long serialVersionUID = -7450222339005524789L;

	/**
	 * 流水号
	 */
	private String flowID;
	
	/**
	 * 发起方
	 */
	private String initiator;
	
	/**
	 * 描述
	 */
	private String description;
	
	public ContextParam(){
		
	}
	
	public ContextParam(String flowID, String initiator){
		this.flowID = flowID;
		this.initiator = initiator;
	}
	
	public String getFlowID() {
		return flowID;
	}
	public void setFlowID(String flowID) {
		this.flowID = flowID;
	}
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String  toString(){
		StringBuffer  sb = new StringBuffer();
		sb.append(",flowID:");sb.append(StringUtils.defaultIfNull(this.getFlowID()));
		sb.append(",initiator :");sb.append(StringUtils.defaultIfNull(this.getInitiator()));
		sb.append(",description :");sb.append(StringUtils.defaultIfNull(this.getDescription()));
		return sb.toString() ;
	}

}
