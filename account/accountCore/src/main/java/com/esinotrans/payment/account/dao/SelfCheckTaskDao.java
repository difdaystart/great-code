/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import com.esinotrans.payment.account.entity.SelfCheckTask;


/**
 * 自检任务记录 DAO
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:00:56
 * @version 1.0
 */
public interface SelfCheckTaskDao {

	/**
	 * @param task
	 */
	void add(SelfCheckTask task);

}
