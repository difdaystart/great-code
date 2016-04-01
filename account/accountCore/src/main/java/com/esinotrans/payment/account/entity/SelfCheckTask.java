/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.util.Date;

import com.esinotrans.payment.account.enums.SelfCheckTaskStatusEnum;

/**
 * 自检任务记录
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:08:57
 * @version 1.0
 */
public class SelfCheckTask implements Serializable{
	
	private static final long serialVersionUID = -3429452706209580031L;

	private Long id;
	
	/**
	 * 任务号
	 */
	private String taskNo;
	
	/**
	 * 自检范围起至时间
	 */
	private Date startDate;
	
	/**
	 * 自检范围终止时间
	 */
	private Date endDate;
	
	/**
	 * 自检类型
	 */
	private String checkType;
	
	/**
	 * 状态
	 */
	private SelfCheckTaskStatusEnum status;
	
	/**
	 * 失败笔数
	 */
	private Integer failureNum;
	
	/**
	 * 成功帐户数
	 */
	private Long successAccountNum;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 备注信息
	 */
	private String remark;
	
	public SelfCheckTask(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public SelfCheckTaskStatusEnum getStatus() {
		return status;
	}

	public void setStatus(SelfCheckTaskStatusEnum status) {
		this.status = status;
	}

	public Integer getFailureNum() {
		return failureNum;
	}

	public void setFailureNum(Integer failureNum) {
		this.failureNum = failureNum;
	}

	public Long getSuccessAccountNum() {
		return successAccountNum;
	}

	public void setSuccessAccountNum(Long successAccountNum) {
		this.successAccountNum = successAccountNum;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
