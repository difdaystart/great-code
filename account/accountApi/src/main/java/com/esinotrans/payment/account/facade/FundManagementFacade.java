/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.facade;

import java.math.BigDecimal;
import java.util.Date;

import com.esinotrans.payment.account.dto.ContextParam;

/**
 * 资金管理Facade
 * 
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午10:58:33
 * @version 1.0
 */
public interface FundManagementFacade {

	/**
	 * 冻结资金<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 冻结金额：amount：BigDecimal<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param amount
	 *            冻结金额
	 * @param unfrozenDate
	 *            解冻时间 @ 账户异常
	 * @return 授权码
	 */
	public String freezeFund(ContextParam contextParam, String accountNo,
			BigDecimal amount, Date unfrozenDate);

	/**
	 * 解冻指定金额的资金<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 解冻金额：amount：BigDecimal<br>
	 * 授权码: credential: String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param amount
	 *            解冻金额
	 * @param credential
	 *            授权码 @ 账户异常
	 */
	public void unfreezeFund(ContextParam contextParam, String accountNo,
			BigDecimal amount, String credential);

	/**
	 * 设置冻结资金的自动解冻时间<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 解冻时间：unfrozenDate：Date<br>
	 * 授权码: credential: String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param unfrozenDate
	 *            解冻时间
	 * @param credential
	 *            授权码 @ 账户异常
	 */
	public void setThawDate(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential);

	/**
	 * 定时解冻资金
	 * 
	 * @param currentDate
	 *            当前时间
	 */
	public void unfreezeFundByTimer();

	/**
	 * 资金自检 FOR ALL 账号
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void accountFundSelfCheck();

	/**
	 * 资金自检 FOR 指定的帐号
	 * 
	 * @param accountNos
	 * @param startDate
	 * @param endDate
	 */
	public void accountFundSelfCheck(String[] accountNos, Date startDate,
			Date endDate);
}
