/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;

import com.esinotrans.payment.account.exceptions.OptimisticLockingException;

/**
 * 乐观锁异常重试 拦截器
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:19:43
 * @version 1.0
 */
public class RetryOnOptimistLockExceptionAspect implements Ordered {

	private static final int DEFAULT_MAX_RETRIES = 2;		//默认最大重试数

	private int retryCounter = DEFAULT_MAX_RETRIES;

	private int order = 1;

	public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
		int currentCount = 0;
		Throwable e;
		do {
			try {
				return joinPoint.proceed();
			} catch (Throwable throwable) {
				e = throwable;
				if(throwable instanceof OptimisticLockingException)
					currentCount++;
				else
					break;
			}
		} while (currentCount <= retryCounter);
		throw e;
	}

	public int getRetryCounter() {
		return retryCounter;
	}

	public void setRetryCounter(int retryCounter) {
		this.retryCounter = retryCounter;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
