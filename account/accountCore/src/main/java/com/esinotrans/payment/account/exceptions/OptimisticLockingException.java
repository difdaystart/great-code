/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.exceptions;

/**
 * 乐观锁异常
 * @Copyright: Copyright (c)2013
 * @company esinotrans
 * @author xingwei.bi
 * @since 2011-3-21
 * @version 1.0
 */
public class OptimisticLockingException extends RuntimeException{

	private static final long serialVersionUID = 3821087967248373161L;

	public OptimisticLockingException(String message) {
		super(message);
	}

}
