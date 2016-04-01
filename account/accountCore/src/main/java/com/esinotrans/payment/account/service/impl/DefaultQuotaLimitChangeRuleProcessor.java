/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.esinotrans.payment.account.dao.QuotaLimitChangeRuleDao;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.QuotaLimitChangeRule;
import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotrans.payment.account.enums.QuotaTypeEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.service.QuotaLimitChangeRuleProcessor;

/**
 * 额度限制变更规则处理器 实现
 * @company YeePay
 * @author 王伟
 * @since 2010-8-29
 * @version 1.0
 */
public class DefaultQuotaLimitChangeRuleProcessor implements
		QuotaLimitChangeRuleProcessor {
	
	private QuotaLimitChangeRuleDao quotaLimitChangeRuleDao;		//额度限制变更规则Dao

	/**
	 * 处理额度限制变更规则
	 */
	public void processQuotaLimitChangeRule(Account account,
			String tradeType, BigDecimal amount) throws AccountException{
		List<QuotaLimitChangeRule> rules = quotaLimitChangeRuleDao.queryAllQuotaLimitChangeRule();
		doProcessQuotaLimit(rules, account,tradeType, amount);
	}

	/**
	 * 进行额度限制变更操作
	 * @param rules
	 * @param account
	 * @param tradeType
	 * @param amount
	 * @throws AccountException
	 */
	private void doProcessQuotaLimit(List<QuotaLimitChangeRule> rules,
			Account account, String tradeType,BigDecimal amount){
		for(QuotaLimitChangeRule rule : rules){
			if(tradeType.equals(rule.getTradeType())){
				if(rule.getQuotaType() == QuotaTypeEnum.WITHDRAW_QUOTA)
					processWithdrawQuota(account,amount,rule.getChangeDirection());
				else if(rule.getQuotaType() == QuotaTypeEnum.RECHARGE_REFUND)
					processRechargeRefundQuota(account,amount,rule.getChangeDirection());
			}
		}
	}
	
	/**
	 * 处理可提现额度
	 * @param account
	 * @param amount
	 */
	private void processWithdrawQuota(Account account, BigDecimal amount,FundChangeDirectionEnum direction){
		if(direction == FundChangeDirectionEnum.INCREMENT){
			account.increaseWithdrawQuota(amount);
		}else{
			if(!account.isSatisfyWithdrawQuota(amount))
				throw AccountException.WITHDRAW_QUOTA_IS_NOT_ENOUGH.newInstance(
						"账户：{0} 的可提现额度为：{1}， 要提现的额度为：{2}， 可提现额度不足", new Object[] {
								account.getAccountNo(),account.getAvailableWithdrawQuota(), amount});
			account.decreaseWithdrawQuota(amount);
		}
	}
	
	/**
	 * 处理可充退额度
	 * @param account
	 * @param amount
	 */
	private void processRechargeRefundQuota(Account account, BigDecimal amount,FundChangeDirectionEnum direction){
		if(direction == FundChangeDirectionEnum.INCREMENT){
			account.increaseRechargeRefundQuota(amount);
		}else{
			if(!account.isSatisfyRechargeRefundQuota(amount))
				throw AccountException.RECHARGE_REFUND_QUOTA_IS_NOT_ENOUGH.newInstance(
						"账户：{0} 的可充退额度为：{1}， 要充退的额度为：{2}， 可充退额度不足", new Object[] {
								account.getAccountNo(),account.getAvailableRechargeRefundQuota(),amount});
			account.decreaseRechargeRefundQuota(amount);
		}
	}

	public void setQuotaLimitChangeRuleDao(
			QuotaLimitChangeRuleDao quotaLimitChangeRuleDao) {
		this.quotaLimitChangeRuleDao = quotaLimitChangeRuleDao;
	}
	
}
