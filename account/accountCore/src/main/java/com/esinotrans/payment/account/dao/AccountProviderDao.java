/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import com.esinotrans.payment.account.entity.AccountProvider;

/**
 * 账户提供方实体DAO
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:58:56
 * @version 1.0
 */
public interface AccountProviderDao {

	/**
	 * 查询账户提供方实体
	 * @param providerCode
	 * @return
	 */
	public AccountProvider queryAccountProvider(String providerCode);
	
	/**
	 * 查询账户提供方实体
	 * @param providerCode
	 * @return
	 */
	public AccountProvider queryAccountProviderById(Long id);
}
