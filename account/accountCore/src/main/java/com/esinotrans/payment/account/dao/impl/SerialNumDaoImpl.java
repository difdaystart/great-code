/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.SerialNumDao;
import com.esinotrans.payment.account.entity.AccountSerialNum;

/**
 * 顺序号Dao 实现类
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-15 上午8:51:54
 * @version 1.0
 */
public class SerialNumDaoImpl extends SqlSessionDaoSupport implements SerialNumDao {

	@Override
	public Map<String,Object> getAccountSerialNum(String accountSystemType){
		AccountSerialNum accountSerialNum = (AccountSerialNum)super.getSqlSession().selectOne("getAccountSerialNum", accountSystemType);
		
		Long oldSerialNum = accountSerialNum.getFlowId();
		accountSerialNum.increaseSerialNumByOne();
		super.getSqlSession().update("update", accountSerialNum);
		
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("serailNum", String.valueOf(oldSerialNum));
		rtnMap.put("prefixNum", accountSerialNum.getPrefixNum());
		return rtnMap;
	}
	
	@Override
	public void insertInitAccountFlowNum(String prefixNum,String accountSystemType, Long initFlowNum){
		AccountSerialNum serialNum = new AccountSerialNum();
		serialNum.setFlowId(initFlowNum);
		serialNum.setPrefixNum(prefixNum);
		serialNum.setAccountSystemType(accountSystemType);
		super.getSqlSession().insert("insertSerialNum", serialNum);
	}
	
}
