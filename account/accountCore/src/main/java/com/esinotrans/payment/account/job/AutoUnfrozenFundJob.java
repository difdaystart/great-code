/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.esinotrans.payment.account.service.SchedualBussinessService;

/**
 * 自动解冻资金 (定时器)
 * @company YeePay
 * @author xingwei.bi
 * @since 2010-9-7
 * @version 1.0
 */

public class AutoUnfrozenFundJob extends QuartzJobBean {
	
	private SchedualBussinessService schedualBussinessService;
	
	/**
	 * 执行 自动解冻资金
	 */
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		//Execute the actual job
		schedualBussinessService.autoUnfrozenFund();
		
	}

	public SchedualBussinessService getSchedualBussinessService() {
		return schedualBussinessService;
	}

	public void setSchedualBussinessService(
			SchedualBussinessService schedualBussinessService) {
		this.schedualBussinessService = schedualBussinessService;
	}
}
