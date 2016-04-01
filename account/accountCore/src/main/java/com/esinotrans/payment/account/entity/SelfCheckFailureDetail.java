/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;

/**
 * 自检失败明细表
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:08:50
 * @version 1.0
 */
public class SelfCheckFailureDetail implements Serializable{

	private static final long serialVersionUID = -2732245104722593422L;

	private Long id;
	
	/**
	 * 帐户编号
	 */
	private String accountNo;
	
	/**
	 * 自检任务号
	 */
	private String taskNo;
	
	/**
	 * 前一条帐户历史的摘要信息
	 */
	private String eveAccountHistoryDesc;
	
	/**
	 * 帐户历史的摘要信息
	 */
	private String accountHistoryDesc;
	
	/**
	 * 描述信息
	 */
	private String describe;
	
	public SelfCheckFailureDetail(){
		
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

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getEveAccountHistoryDesc() {
		return eveAccountHistoryDesc;
	}

	public void setEveAccountHistoryDesc(String eveAccountHistoryDesc) {
		this.eveAccountHistoryDesc = eveAccountHistoryDesc;
	}

	public String getAccountHistoryDesc() {
		return accountHistoryDesc;
	}

	public void setAccountHistoryDesc(String accountHistoryDesc) {
		this.accountHistoryDesc = accountHistoryDesc;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
}
