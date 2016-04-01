/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import com.esinotrans.payment.account.entity.AccountSnapshot;


/**
 * 帐户快照表 DAO
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:59:03
 * @version 1.0
 */
public interface AccountSnapshotDao {

	/**
	 * @param snapshot
	 */
	void add(AccountSnapshot snapshot);

}
