/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.dao;

import java.util.List;

import com.esinotrans.payment.account.entity.QuotaLimitChangeRule;

/**
 * 额度限制变更规则Dao
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:00:22
 * @version 1.0
 */
public interface QuotaLimitChangeRuleDao {

	/**
	 * 保存一批额度限制变更规则
	 * @param rules
	 */
	public void saveQuotaLimitChangeRules(List<QuotaLimitChangeRule> rules);

	/**
	 * 查询额度限制变更规则
	 * @param condition
	 * @return
	 */
	public List<QuotaLimitChangeRule> queryQuotaLimitChangeRule(String tradeType);
	
	/**
	 * 查询所有的额度限制变更规则
	 * @param condition
	 * @return
	 */
//	@Cache(name="queryAllQuotaLimitChangeRule", cacheRegion="account")
	public List<QuotaLimitChangeRule> queryAllQuotaLimitChangeRule();
}
