/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenRecord;
import com.esinotrans.payment.account.exception.AccountException;

/**
 * 资金管理service
 * @company YeePay
 * @author 王伟
 * @since 2010-12-27
 * @version 1.0
 */
public interface FundManagementService {

	/**
	 * 冻结资金
	 * @param contextParam
	 * @param accountNo
	 * @param amount
	 * @param unfrozenDate
	 * @return
	 * @throws AccountException
	 */
	public String freezeFund(ContextParam contextParam, String accountNo,
			BigDecimal amount, Date unfrozenDate) throws AccountException;
	
	/**
	 * 解冻指定金额的资金
	 * @param frozenUnFrozenFundParam 冻结资金管理对象
	 */
	public void unfreezeFund(ContextParam contextParam, String accountNo,
			BigDecimal amount, String credential) throws AccountException;
	
	/**
	 * 设置冻结资金的自动解冻时间
	 * @param frozenUnFrozenFundParam 冻结资金管理对象
	 */
	public void setThawDate(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential) throws AccountException;
	
	/**
	 * 获取需要自动解冻的 资金冻结解冻记录 
	 * @return
	 */
	public List<FundFrozenUnFrozenRecord> queryAllAutoUnfrozenFundRecords(Date autoUnfrozenDate);
	
	/**
	 * 定时解冻资金
	 * @param currentDate
	 * @param record
	 */
	public void unfreezeFundByTimer(Date currentDate,  FundFrozenUnFrozenRecord record);
	
	/**
	 * 资金自检 FOR ALL 账号
	 * @param startDate
	 * @param endDate
	 */
	public void accountFundSelfCheck(Date startDate,Date endDate);
	
	/**
	 * 资金资金 FOR 指定的帐号
	 * @param accountNos
	 * @param startDate
	 * @param endDate
	 */
	public void accountFundSelfCheck(String[] accountNos,Date startDate,Date endDate);
}
