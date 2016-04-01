/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.facade;

import java.util.Date;

import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.dto.CreateAccountParam;

/**
 * 账户管理Facade 提供各种账户管理服务(如：创建账户,冻结账户等)
 * <p>
 * Examples : example 1 下面是利用 IAccountManagementFacade 来保存一个账户的实例
 * 
 * <p>
 * 先获得 spring的context(spring的上下文对象) 对象<br>
 * private IAccountManagementFacade accountManagementFacade =
 * (IAccountManagementFacade) context.getBean("accountManagementService");
 * CreateAccountParam param = new CreateAccountParam("12458111117", "yeepay",
 * "1000", Long.valueOf(17));<br>
 * param.setBalance(new BigDecimal(210.92));<br>
 * try { String accountNo = accountManagementFacade.createAccount(param);<br>
 * 
 * } catch (AccountException e) {<br>
 * e.printStackTrace();<br>
 * } <br>
 * <br>
 * example 2 下面是以个冻结资金的例子：<br>
 * private IAccountManagementFacade accountManagementFacade =
 * (IAccountManagementFacade) context.getBean("accountManagementService");<br>
 * 
 * AccountManagementParam param = new AccountManagementParam("12345612",<br>
 * "yeepay", accountNo); try { accountManagementFacade.freezeAccount(param);<br>
 * } catch (AccountException e) {<br>
 * e.printStackTrace(); }
 * 
 * 
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午10:59:14
 * @version 1.0
 */
public interface AccountManagementFacade {

	/**
	 * 创建账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 账户类型：accountType：String <br>
	 * 客户编号: customerNo: String <br>
	 * 
	 * @param contextParam
	 * @param createAccountParam
	 *            @ 账户异常
	 * @return 账户号
	 */
	public String createAccount(ContextParam contextParam,
			CreateAccountParam createAccountParam);

	/**
	 * 创建账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 账户类型：accountType：String <br>
	 * 客户编号: customerNo: String <br>
	 * 
	 * @param contextParam
	 * @param createAccountParam
	 * @param status
	 *            状态
	 * @param createDate
	 *            创建日期
	 * @param accountHistorySerial
	 *            帐户历史序列号
	 * @return @
	 */
	public String createAccount(ContextParam contextParam,
			CreateAccountParam createAccountParam, String status,
			Date createDate, Long accountHistorySerial);

	/**
	 * 注销账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 账户号：accountNo：String <br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号 @ 账户异常
	 */
	public void removeAccount(ContextParam contextParam, String accountNo);

	/**
	 * 冻结账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 
	 * 可选赋值的属性为：<br>
	 * 自动解冻时间：unfrozenDate: Date<br>
	 * 授权码：credential: String <br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param unfrozenDate
	 *            解冻时间
	 * @return 授权码 @ 账户异常
	 */
	public String freezeAccount(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential);

	/**
	 * 解冻账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param credential
	 *            授权码 @ 账户异常
	 */
	public void unfreezeAccount(ContextParam contextParam, String accountNo,
			String credential);

	/**
	 * 冻结止付<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param unfrozenDate
	 *            解冻时间
	 * @return 授权码 @ 账户异常
	 */
	public String forbidDebit(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential);

	/**
	 * 冻结止收 <br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param unfrozenDate
	 *            解冻时间
	 * @return 授权码 @ 账户异常
	 */
	public String forbidCredit(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential);

	/**
	 * 自动解冻账户 for 定时器 必须赋值的属性为：<br>
	 * 时间：currentDate: Date<br>
	 * 
	 * @param currentDate
	 *            时间 @
	 */
	public void autoProcessUnfreezeAccountForTimer();

}
