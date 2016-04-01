/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.AccountSnapshotDao;
import com.esinotrans.payment.account.entity.AccountSnapshot;

/**
 * 帐户快照表 DAO 实现类
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:03:15
 * @version 1.0
 */
public class AccountSnapshotDaoImpl extends SqlSessionDaoSupport
	implements AccountSnapshotDao{

	/* (non-Javadoc)
	 * @see com.esinotrans.payment.account.dao.AccountSnapshotDao#add(com.esinotrans.payment.account.entity.AccountSnapshot)
	 */
	@Override
	public void add(AccountSnapshot snapshot) {
		super.getSqlSession().insert("insertAccountSnapshot", snapshot);
	}

}
