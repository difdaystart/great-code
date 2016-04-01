/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.SelfCheckTaskDao;
import com.esinotrans.payment.account.entity.SelfCheckTask;

/**
 * 自检任务记录 DAO 实现类
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:04:32
 * @version 1.0
 */
public class SelfCheckTaskDaoImpl extends SqlSessionDaoSupport 
	implements SelfCheckTaskDao{

	/* (non-Javadoc)
	 * @see com.esinotrans.payment.account.dao.SelfCheckTaskDao#add(com.esinotrans.payment.account.entity.SelfCheckTask)
	 */
	@Override
	public void add(SelfCheckTask task) {
		super.getSqlSession().insert("insertSelfCheckTask", task);
	}

}
