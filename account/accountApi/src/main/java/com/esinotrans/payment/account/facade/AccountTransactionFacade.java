/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.facade;

import java.math.BigDecimal;
import java.util.List;

import com.esinotrans.payment.account.dto.TransactionCommandParam;

/**
 * 账户交易Facade Examples ：
 * <p>
 * example1 转账
 * <p>
 * IAccountTransactionFacade transactionFacade = (IAccountTransactionFacade)
 * getBean("accountTransactionService"); <br>
 * TransactionCommandParam param = new TransactionCommandParam("10002", "交易系统",
 * "10000000000018", "10000000000017", new BigDecimal(150),
 * AccountTradeTypeEnum.TRANSFER);<br>
 * try { transactionFacade.transfer(param);<br>
 * } catch (AccountException e) {<br>
 * e.printStackTrace();<br>
 * }<br>
 * 
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午10:58:50
 * @version 1.0
 */
public interface AccountTransactionFacade {

	/**
	 * 存入<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 收款方账号：receiverAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 交易类型：tradeType ：AccountTradeTypeEnum(账户api定义的枚举类型) <br>
	 * 
	 * @param param
	 *            账户交易命令参数 @ 账户异常
	 */
	public void credit(TransactionCommandParam param);

	/**
	 * 批量存入<br>
	 * 必须赋值的属性和 credit 方法一样<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表 @ 账户异常
	 */
	public void batchCredit(List<TransactionCommandParam> params);

	/**
	 * 扣款<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 付款方账号：payerAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 交易类型：tradeType ：AccountTradeTypeEnum(账户api定义的枚举类型) <br>
	 * 
	 * @param param
	 *            账户交易命令参数 @ 账户异常
	 */
	public void debit(TransactionCommandParam param);

	/**
	 * 批量扣款<br>
	 * 必须赋值的属性和 debit 方法一样<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表 @ 账户异常
	 */
	public void batchDebit(List<TransactionCommandParam> params);

	/**
	 * 转账<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 付款方账号：payerAccountNo ：String <br>
	 * 收款方账号：receiverAccountNo ：String <br>
	 * 发生额：amount ：BigDecimal <br>
	 * 交易类型：tradeType ：AccountTradeTypeEnum(账户api定义的枚举类型) <br>
	 * 
	 * @param param
	 *            账户交易命令参数 @ 账户异常
	 */
	public void transfer(TransactionCommandParam param);

	/**
	 * 批量转账<br>
	 * 必须赋值的属性和 transfer 方法一样<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表 @ 账户异常
	 * @param params
	 */
	public void batchTransfer(List<TransactionCommandParam> params);

	/**
	 * 解冻并扣款<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 付款方账号：payerAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 交易类型：tradeType ：AccountTradeTypeEnum(账户api定义的枚举类型) <br>
	 * 授权码：credential ：String <br>
	 * 
	 * @param param
	 *            账户交易命令参数
	 * @param credential
	 *            授权码 @ 账户异常
	 */
	public void debitUnfrozen(TransactionCommandParam param, String credential);

	/**
	 * 存入并冻结资金 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 收款方账号：receiverAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 交易类型：tradeType ：AccountTradeTypeEnum(账户api定义的枚举类型) <br>
	 * 
	 * @param param
	 *            账户交易命令参数
	 * @param 冻结金额
	 * @return 授权码 @
	 */
	public String creditAndFreezeFund(TransactionCommandParam param,
			BigDecimal freezeAmount);

	/**
	 * 批量解冻并扣款<br>
	 * 必须赋值的属性和 debitUnfrozen 方法一样<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表
	 * @param credential
	 *            授权码 @ 账户异常
	 */
	public void batchDebitUnfrozen(List<TransactionCommandParam> params,
			String credential);

	/**
	 * 调账(根据资金方向设置调出方账号和调入方账号)<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 调出方账号：payerAccountNo ：String <br>
	 * 调入方账号：receiverAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 交易类型：tradeType ：AccountTradeTypeEnum(账户api定义的枚举类型) <br>
	 * 资金方向：direction ：FundChangeDirectionEnum(账户api定义的枚举类型)<br>
	 * 
	 * @param param
	 *            账户交易命令参数 @ 账户异常
	 * 
	 */
	public void adjustBalance(TransactionCommandParam param);

	/**
	 * 此接口比较特殊，支持不同账户操作类型的交易指令， 比如一笔交易有三种账户操作：付款方支出，收款方存入，手续费记账。<br>
	 * 参数params列表中的TransactionCommandParam必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 付款方账号：payerAccountNo ：String<br>
	 * 收款方账号：receiverAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 交易类型：tradeType ：AccountTradeTypeEnum(账户api定义的枚举类型) <br>
	 * 资金方向：direction ：AccountTradeTypeEnum(账户api定义的枚举类型)<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表 @ 账户异常
	 */
	public void batchProcessTransaction(List<TransactionCommandParam> params);

	/**
	 * 对于同一账户，进行批量存入<br>
	 * 必须赋值的属性和 credit 方法一样,流水号必须相同<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表 @ 账户异常
	 */
	public void batchCreditForSameAccount(List<TransactionCommandParam> params);
}
