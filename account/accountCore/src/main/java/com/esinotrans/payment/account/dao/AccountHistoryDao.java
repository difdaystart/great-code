/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import java.util.Date;
import java.util.List;

import com.esinotrans.payment.account.entity.AccountHistory;

/**
 * 账户历史Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:58:43
 * @version 1.0
 */
public interface AccountHistoryDao {

	/**
	 * 保存账户历史
	 * 
	 * @param history
	 * 
	 */
	public void saveAccountHistory(AccountHistory history);
	
	/**
	 * 保存账户历史集合
	 * @param historys 账户历史集合
	 */
	public void saveAccountHistoryList(List<AccountHistory> historys);

	/**
	 * 根据账号分页查询在某个时段账户历史  
	 * @param accountNo  账号
	 * @param startDate 开始时间
	 * @param endDate 截止时间
	 * @param startPage 开始分页的页码
	 * @param pageSize 每一页显示的size
	 * @return 账户历史列表
	 */
	public List<AccountHistory> findAccountHistoryByAccountNo(String accountNo,Date startDate, Date endDate,int startPage, int pageSize);
   
	/**
	 * 根据账号查询在某个时段账户历史
	 * @param accountNo 账号
	 * @param startDate 开始时间
	 * @param endDate 截止时间
	 * @return 账户历史列表
	 */
	public List<AccountHistory> findAccountHistoryByAccountNo(String accountNo,Date startDate, Date endDate);
	
	/**
     * 查询账户在某个时段的账户历史记录总数
     * @param accountNo 账号
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 账户的总数
     */
	public  int getAccountHistoryCount( String accountNo, Date startDate, Date endDate );

}
