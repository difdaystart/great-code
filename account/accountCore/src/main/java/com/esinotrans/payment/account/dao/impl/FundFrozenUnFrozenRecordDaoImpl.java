/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.FundFrozenUnFrozenRecordDao;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenRecord;

/**
 * 资金冻结解冻记录Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:03:32
 * @version 1.0
 */
public class FundFrozenUnFrozenRecordDaoImpl extends SqlSessionDaoSupport
		implements FundFrozenUnFrozenRecordDao {

	/**
	 * 保存 资金冻结解冻记录
	 * 
	 * @param fundFrozenUnFrozenRecord
	 *            资金冻结解冻记录
	 */
	@Override
	public void saveFundFrozenUnFrozenRecord(
			FundFrozenUnFrozenRecord fundFrozenUnFrozenRecord) {
		super.getSqlSession().insert("insertFundFrozenRecord", fundFrozenUnFrozenRecord);
	}

	/**
	 * 根据授权码，获取与其匹配的 资金冻结解冻记录
	 * 
	 * @param credential
	 *            授权码
	 * @return 资金冻结记录
	 */
	@Override
	public FundFrozenUnFrozenRecord queryFundFrozenUnFrozenRecordByCredential(
			String credential) {
		return (FundFrozenUnFrozenRecord)super.getSqlSession().selectOne("queryRecordByCredential", credential);
	}

	/**
	 * 修改资金冻结解冻记录 信息
	 * 
	 * @param record
	 */
	@Override
	public void updateFundFrozenUnFrozenRecord(FundFrozenUnFrozenRecord record) {
		super.getSqlSession().update("updateFundFrozenUnFrozenRecord",record);
	}

	@Override
	public void updateAutoUnfrozenDate(Map<String, Object> params) {
		super.getSqlSession().update("updateAutoUnfrozenDate",params);
	}

	/**
	 * 获取需要自动解冻的 资金冻结解冻记录
	 * 
	 * @return
	 */
	@Override
	public List<FundFrozenUnFrozenRecord> queryAllAutoUnfrozenFundRecords(Date autoUnfrozenDate) {
		return super.getSqlSession().selectList("queryAllAutoUnfrozenFundRecords",autoUnfrozenDate);
	}

	@Override
	public List<FundFrozenUnFrozenRecord> queryUnfrozenFundRecords(
			String accountNo) {
		return super.getSqlSession().selectList("queryUnfrozenFundRecords", accountNo);
	}
}
