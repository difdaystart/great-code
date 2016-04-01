/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.SelfCheckFailureDetailDao;
import com.esinotrans.payment.account.entity.SelfCheckFailureDetail;

/**
 * 自检失败明细表DAO 实现类
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:03:45
 * @version 1.0
 */
public class SelfCheckFailureDetailDaoImpl extends SqlSessionDaoSupport 
	implements SelfCheckFailureDetailDao{

	@Override
	public void saveSelfCheckFailureDetails(List<SelfCheckFailureDetail> details) {
		SqlSession sqlSession = this.getBatchSession();
		for(SelfCheckFailureDetail detail : details){
			sqlSession.insert("insertSelfCheckFailureDetail", detail);
		}
		sqlSession.commit();
	}
	
	private SqlSession getBatchSession() {
		SqlSession sqlSession = SqlSessionUtils.getSqlSession(new SqlSessionFactoryBuilder().build(super.getSqlSession().getConfiguration()),ExecutorType.BATCH, null);
		return sqlSession;
	}

}
