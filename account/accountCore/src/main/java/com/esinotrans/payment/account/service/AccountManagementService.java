/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.dto.CreateAccountParam;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.AccountFrozenRecord;
import com.esinotrans.payment.account.enums.AccountOperationEnum;
import com.esinotrans.payment.account.exception.AccountException;

/**
 * 账户管理服务
 * 
 * @author Administrator
 * 
 */
public interface AccountManagementService {

	/**
	 * 创建账户
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param createAccountParam
	 *            创建账户的参数 对象
	 * @return 账户号
	 */
	public String createAccount(ContextParam contextParam,
			CreateAccountParam createAccountParam) throws AccountException;
	
	/**
	 * 创建账户
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param createAccountParam
	 *            创建账户的参数 对象
	 * @return 账户号
	 */
	public String createAccount(ContextParam contextParam,
			CreateAccountParam createAccountParam,String status,Date createDate,Long accountHistorySerial) throws AccountException;

	/**
	 * 注销账户
	 * 
	 * @param accountNo
	 * @param baseParam
	 *            基本参数对象
	 */
	public void removeAccount(String accountNo, ContextParam baseParam)
			throws AccountException;

	/**
	 * 冻结账户
	 * 
	 * @param accountNo
	 *            账号
	 * @param param
	 *            基本参数对象
	 * @param unfrozenDate
	 *            解冻时间 可以为空
	 */
	public String freezeAccount(ContextParam param, String accountNo,
			Date unfrozenDate, String credential) throws AccountException;

	/**
	 * 解冻账户
	 * 
	 * @param accountNo
	 *            账号
	 * @param param
	 *            基本参数对象
	 */
	public void unfreezeAccount(String accountNo, String credential,
			ContextParam param) throws AccountException;

	/**
	 * 冻结止付
	 * 
	 * @param accountNo
	 *            账号
	 * @param param
	 *            基本参数对象
	 * @param unfrozenDate
	 *            解冻时间 可以为空
	 */
	public String forbidDebit(ContextParam param, String accountNo,
			Date unfrozenDate, String credential) throws AccountException;

	/**
	 * 冻结止收
	 * 
	 * @param accountNo
	 *            账号
	 * @param param
	 *            基本参数对象
	 * @param unfrozenDate
	 *            解冻时间 可以为空
	 */
	public String forbidCredit(ContextParam param, String accountNo,
			Date unfrozenDate, String credential) throws AccountException;

	/**
	 * 自动解冻账户 for 定时器
	 * 
	 * @param currentDate
	 * @throws AccountException
	 */
	public void autoProcessUnfreezeAccountForTimer(String accountNo,
			List<AccountFrozenRecord> frozenRecords) throws AccountException;
	
	/**
	 * 更改账户冻结额度
	 * @param param
	 * @param account
	 * @param operationType
	 */
	public void updateAccountFrozenQuota(ContextParam param,
			Account account, BigDecimal frozenAmount, AccountOperationEnum operationType);
	
	/**
	 * 根据自动解冻时间，查询账户冻结记录
	 * @param date
	 * @return
	 */
	public List<AccountFrozenRecord> queryAccountFrozenRecordsByUnfrozenDate(Date date);
}
