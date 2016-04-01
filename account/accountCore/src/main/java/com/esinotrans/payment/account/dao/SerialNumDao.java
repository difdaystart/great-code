/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import java.util.Map;

/**
 * 顺序号Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:01:03
 * @version 1.0
 */
public interface SerialNumDao {

	/**
	 * 初始化特定类型的账户号中流水号部分
	 * @param prefixNum
	 * @param accountSystemType
	 * @param initFlowNum
	 */
	public void insertInitAccountFlowNum(String prefixNum,String accountSystemType, Long initFlowNum);
	
	/**
	 * 获得账号中的顺序号部分
	 * 
	 * @param  accountSystemType
	 * @return 顺序号
	 */
	public Map<String,Object> getAccountSerialNum(String accountSystemType);
	
}
