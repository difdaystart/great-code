/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.esinotrans.payment.account.entity.FundFrozenUnFrozenRecord;

/**
 * 资金冻结解冻记录Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:00:15
 * @version 1.0
 */
public interface FundFrozenUnFrozenRecordDao {
	
	/**
	 * 保存 资金冻结解冻记录
	 * @param fundFrozenUnFrozenRecord 资金冻结解冻记录
	 */
	public void saveFundFrozenUnFrozenRecord(FundFrozenUnFrozenRecord fundFrozenUnFrozenRecord);
	
	/**
	 * 根据授权码，获取与其匹配的 资金冻结解冻记录
	 * @param credential 授权码
	 * @return 资金冻结记录
	 */
	public FundFrozenUnFrozenRecord queryFundFrozenUnFrozenRecordByCredential(String credential);
	
	/**
	 * 修改资金冻结解冻记录 信息
	 * @param record (账号)
	 */
	public void updateFundFrozenUnFrozenRecord(FundFrozenUnFrozenRecord record);
	
	/**
	 * 修改资金的解冻时间
	 * @param params (账号，授权码，解冻时间)
	 */
	public void updateAutoUnfrozenDate(Map<String,Object> params);
	
	/**
	 * 获取需要自动解冻的 资金冻结解冻记录 
	 * @return
	 */
	public List<FundFrozenUnFrozenRecord> queryAllAutoUnfrozenFundRecords(Date autoUnfrozenDate);
	
	/**
	 * 根据账号，获取资金冻结解冻记录 
	 * @return
	 */
	public List<FundFrozenUnFrozenRecord> queryUnfrozenFundRecords(String accountNo);
}
