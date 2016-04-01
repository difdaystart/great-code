/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.esinotrans.payment.account.dto.AccountParam;
import com.esinotrans.payment.account.dto.FundFrozenRecordResultDTO;
import com.esinotrans.payment.account.dto.GetAccountHistoryParam;

/**
 * 账户查询Facade 提供查询账户的功能
 * 
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午10:58:59
 * @version 1.0
 */
public interface AccountQueryFacade {

	/**
	 * 获得账户信息<br>
	 * 必须的参数：<br>
	 * 账户号：accountNo ：String<br>
	 * 
	 * @param accountNo
	 *            账户号
	 * @return AccountParam 账户信息 @ 账户异常
	 */
	public AccountParam getAccountByAccountNo(String accountNo);

	/**
	 * 获得此客户拥有的所有账户<br>
	 * 必须的参数：<br>
	 * 客户id：customerID ：long<br>
	 * 
	 * @param customerNo
	 * @return List 账户参数列表
	 */
	public List<AccountParam> getAccountsByCustomerNo(String customerNo);

	/**
	 * 获得此账户历史记录<br>
	 * 必须的参数：<br>
	 * 账户号：accountNo ：String<br>
	 * 开始时间：StartDate ：Date<br>
	 * 截止时间：endDate ：Date<br>
	 * 
	 * @param accountNo
	 *            账户号
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            截止时间
	 * @return GetAccountHistoryParam 账户历史信息
	 */
	public GetAccountHistoryParam getAccountHistory(String accountNo,
			Date startDate, Date endDate);

	/**
	 * 获取此帐户的资金冻结记录
	 * 
	 * @param accountNo
	 *            账户号
	 * @return 资金冻结记录 集合
	 */
	public List<FundFrozenRecordResultDTO> getFundFrozenRecords(String accountNo);

	/**
	 * 验证该帐户是否能进行存入操作
	 * 
	 * @param accountNo
	 *            账户号
	 * @return true: 可以进行存入操作，false:不可以存入操作 @ 账户异常
	 */
	public boolean checkCanCredit(String accountNo);

	/**
	 * 验证该帐户是否能进行扣款操作
	 * 
	 * @param accountNo
	 *            账户号
	 * @return @ 账户异常
	 */
	public void checkCanDebit(String accountNo, BigDecimal amount);
}
