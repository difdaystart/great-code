/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.AccountProviderDao;
import com.esinotrans.payment.account.entity.AccountProvider;

/**
 * 账户提供方实体DAO 实现类
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:03:09
 * @version 1.0
 */
public class AccountProviderDaoImpl extends SqlSessionDaoSupport
			implements AccountProviderDao{

	@Override
	public AccountProvider queryAccountProvider(String providerCode) {
		return (AccountProvider)super.getSqlSession().selectOne("findAccountProviderByCode", providerCode);
	}

	@Override
	public AccountProvider queryAccountProviderById(Long id) {
		return (AccountProvider)super.getSqlSession().selectOne("findAccountProviderById", id);
	}

}
