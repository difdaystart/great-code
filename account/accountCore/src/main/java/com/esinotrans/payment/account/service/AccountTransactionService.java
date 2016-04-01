/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.esinotrans.payment.account.dto.TransactionCommandParam;
import com.esinotrans.payment.account.exception.AccountException;


/**
 * 账户交易服务
 * @company YeePay
 * @author 王伟
 * @since 2010-8-18
 * @version 1.0
 */
public interface AccountTransactionService {
	
	/**
	 * 存入
	 * @param command 
	 * 			交易命令参数
	 */
	public void credit(TransactionCommandParam command) throws AccountException;
	
	/**
	 * 存入
	 * @param command 
	 * 			交易命令参数
	 * @param tradeType
	 * 			变更额度限制的交易类型
	 */
	public void credit(TransactionCommandParam command,String tradeType) throws AccountException;
	
	/**
	 * 调账
	 * @param command
	 * 			交易命令参数
	 */
	public void adjusting(TransactionCommandParam command);
	
	/**
	 * 支出
	 * -1 判断交易流水号是否重复
	 * 0.判断帐户状态是否允许
	 * 1.对于可提现交易，判断是否超过提现额度
	 * 2.可用余额是否足够
	 * 3.增加流水记录
	 * 4.扣款（对于代打款和支付，需要计算是不是需要扣可提现额度）
	 * 5.根据额度限制变更规则，判断是否需要修改可提现额度
	 * 6.增加顺序号
	 * 7.生成账户历史
	 */
	
	public void debit(TransactionCommandParam command) throws AccountException;
	
	/**
	 * 对于同一账户，进行批量存入
	 * @param params 交易命令参数集合
	 */
	public void batchCreditForSameAccount(List<TransactionCommandParam> params) throws AccountException;
	
	/**
	 * 生成余额的签名
	 * @param amount
	 * @return
	 */
	public String generateBalanceSign(BigDecimal amount);
}
