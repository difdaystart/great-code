/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import java.util.List;

import com.esinotrans.payment.account.entity.SelfCheckFailureDetail;

/**
 * 自检失败明细表DAO
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:00:28
 * @version 1.0
 */
public interface SelfCheckFailureDetailDao {
	
	/**
	 * 保存自检失败明细表 集合
	 * @param details
	 */
	public void saveSelfCheckFailureDetails(List<SelfCheckFailureDetail> details);

}
