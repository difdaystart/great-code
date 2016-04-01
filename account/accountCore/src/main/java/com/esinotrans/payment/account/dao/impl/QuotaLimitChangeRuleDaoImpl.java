/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.esinotrans.payment.account.dao.QuotaLimitChangeRuleDao;
import com.esinotrans.payment.account.entity.QuotaLimitChangeRule;

/**
 * 额度限制变更规则Dao实现
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:03:38
 * @version 1.0
 */
public class QuotaLimitChangeRuleDaoImpl extends SqlSessionDaoSupport
		implements QuotaLimitChangeRuleDao {

	
	@Override
	public void saveQuotaLimitChangeRules(List<QuotaLimitChangeRule> rules) {
		for(Iterator<QuotaLimitChangeRule> iter = rules.iterator(); iter.hasNext();){
			QuotaLimitChangeRule rule = iter.next();
			super.getSqlSession().insert("insertQuotaLimitChangeRule", rule);
		}
	}

	
	/**
	 * 查询额度限制变更规则
	 * @param condition
	 * @return
	 */
	public List<QuotaLimitChangeRule> queryQuotaLimitChangeRule(String tradeType){
		return super.getSqlSession().selectList("queryQuotaLimitChangeRule", tradeType);
	}


	@Override
	public List<QuotaLimitChangeRule> queryAllQuotaLimitChangeRule() {
		return super.getSqlSession().selectList("getAll");
	}
}
