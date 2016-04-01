/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.exception;

import java.math.BigDecimal;


/**
 * 账户异常类
 * 
 * 该类定义了一些账户里存在的异常
 * 并且不同的code 对应了不同的异常情况
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午11:17:17
 * @version 1.0
 */
public class AccountException extends RuntimeException{
	private static final long serialVersionUID = -3519605079887966459L;
	/**
	 * 账户可用余额不足
	 */
	public static AccountException AVAILABLE_BALANCE_ISNOT_ENOUGH = new AccountException("001");        
	/**
	 * 流水号已经存在，该请求是重复请求
	 */
	public static AccountException FLOW_ID_IS_EXIST = new AccountException("002");				
	/**
	 * 非法参数 
	 */
	public static AccountException ILLEGAL_ARGUMNET = new AccountException("003");				
	/**
	 *账户不存在
	 */
	public static AccountException ACCOUNT_ISNOT_EXIST = new AccountException("004");			
	/**
	 * 资金冻结记录不存在
	 */
	public static  AccountException FUNDFROZENRECORD_IS_NULL =  new AccountException("005");    
	/**
	 * 账户状态不合法
	 */
	public static AccountException ACCOUNT_STATUS_IS_NOT_VALID = new AccountException("006"); 
	/**
	 * 提现额度不够
	 */
	public static AccountException WITHDRAW_QUOTA_IS_NOT_ENOUGH = new AccountException("007"); 
	/**
	 * 账户余额不为零
	 */
	public static AccountException BALANCE_IS_NOT_ZERO = new AccountException("008");			
	/**
	 * 参数验证不合法
	 */
	public static AccountException PARAMETER_VALIDATE_IS_NOT_VALID = new AccountException("009"); 
	/**
	 * 付款账户已创建
	 */
	public static AccountException PAYMENT_ACCOUNT_HAS_CREATED = new AccountException("010"); 
	
	/**
	 * 账户冻结记录已创建
	 */
	public static AccountException ACCOUNT_FROZEN_RECORD_HAS_CREATED = new AccountException("011"); 
	
	/**
	 * 账户冻结记录不存在
	 */
	public static AccountException ACCOUNT_FROZEN_RECORD_IS_NOT_EXIST = new AccountException("012"); 
	
	/**
	 * 账户类型不存在
	 */
	public static AccountException ACCOUNT_TYPE_IS_NOT_EXIST = new AccountException("013");
	
	/**
	 * 不合法余额，比如余额为负
	 */
	public static AccountException INVALID_BALANCE = new AccountException("014");
	
	/**
	 *账户提供方不存在
	 */
	public static AccountException ACCOUNT_PROVIDER_NOT_EXIST = new AccountException("015");
	
	/**
	 *账户余额被篡改
	 */
	public static AccountException ACCOUNT_BALANCE_DISTORT = new AccountException("016");	
	
	/**
	 * 可充退额度不够
	 */
	public static AccountException RECHARGE_REFUND_QUOTA_IS_NOT_ENOUGH = new AccountException("017"); 
	
	protected AccountException(String code) {
		super(code);
	}

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	public AccountException newInstance(String string, String string2) {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * @param string
	 * @param objects
	 * @return
	 */
	public AccountException newInstance(String string, Object... objects) {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * @param string
	 * @param accountNo
	 * @param availableBalance
	 * @param amount
	 * @return
	 */
	public AccountException newInstance(String string, String accountNo,
			BigDecimal availableBalance, BigDecimal amount) {
		// TODO Auto-generated method stub
		return this;
	}

}
