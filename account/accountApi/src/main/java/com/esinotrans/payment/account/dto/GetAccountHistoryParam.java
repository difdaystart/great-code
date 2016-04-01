/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dto;

import java.io.Serializable;
import java.util.List;

import com.esinotranse.payment.utils.StringUtils;

/**
 * 获得账户历史参数
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午10:59:46
 * @version 1.0
 */
public final class GetAccountHistoryParam implements Serializable{
	
	private static final long serialVersionUID = -1290072388193802166L;
	
	/**
	 * 账号
	 */
	private String accountNo;
	
	/**
	 * 账户历史列表
	 */
	private List<AccountHistoryParam> history;
	
	public GetAccountHistoryParam(){
		
	}
	
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public List<AccountHistoryParam> getHistory() {
		return history;
	}
	public void setHistory(List<AccountHistoryParam> history) {
		this.history = history;
	}
	public String toString(){
		return "accountNo:"+StringUtils.defaultIfNull(accountNo)+"history: "+history;
	}
}
