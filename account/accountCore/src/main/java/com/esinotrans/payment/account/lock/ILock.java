/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.lock;

/** 
 * 并发锁。在使用锁时，需要将其放入try{} finally{}块中，以防止锁失效
 * 使用场景为：
 * try{
 * 
 *    lock.lock();
 *    
 *    
 * }finally{
 * 	  lock.unlock();
 * }
 * @company YeePay 
 * @author 王伟
 * @since 2010-9-20 
 * @version 1.0  
 */
public interface ILock {

	/**
	 * 将对象加锁
	 */
	public void lock();
	
	/**
	 * 将对象解锁
	 */
	public void unlock();
}
