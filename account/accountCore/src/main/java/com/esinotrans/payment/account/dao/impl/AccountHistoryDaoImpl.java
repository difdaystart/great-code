/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.AccountHistoryDao;
import com.esinotrans.payment.account.entity.AccountHistory;

/**
 * 帐户历史DAO 实现类
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:02:29
 * @version 1.0
 */
public class AccountHistoryDaoImpl extends SqlSessionDaoSupport implements
		AccountHistoryDao {

	@Override
	public void saveAccountHistory(AccountHistory history){
		super.getSqlSession().insert("insertAccountHistory", history);
	}
	
	/**
	 * 保存账户历史集合
	 * @param historys 账户历史集合
	 */
	public void saveAccountHistoryList(List<AccountHistory> historys){
		SqlSession sqlSession = this.getBatchSession();
		for (AccountHistory e : historys)
			sqlSession.insert("insertAccountHistory", e);
		sqlSession.commit();
	}
	
	private SqlSession getBatchSession() {
		SqlSession sqlSession = SqlSessionUtils.getSqlSession(new SqlSessionFactoryBuilder().build(super.getSqlSession().getConfiguration()),ExecutorType.BATCH, null);
		return sqlSession;
	}

	@Override
	public List<AccountHistory> findAccountHistoryByAccountNo(String accountNo,Date startDate, Date endDate,int startPage, int pageSize){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("accountNo", accountNo);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		int skipResults = (startPage - 1) * pageSize;
		RowBounds rowBounds = new RowBounds(skipResults,pageSize);
		return getSqlSession().selectList("findAccountHistoryByAccountNo", params, rowBounds);
	}

	@Override
	public int getAccountHistoryCount(String accountNo, Date startDate, Date endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("accountNo", accountNo);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return (Integer) super.getSqlSession().selectOne("getAccountHistoryCountByAccountNo", params);
	}

	@Override
	public List<AccountHistory> findAccountHistoryByAccountNo(String accountNo,
			Date startDate, Date endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("accountNo", accountNo);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return super.getSqlSession().selectList("findAccountHistoryByAccountNo", params);
	}

}
