/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁管理器
 * 
 * 使用场景为：
 * <pre>
 * ILock lock = LockManager.getAccountLock(key);
 * try{
 *    lock.lock();
 *    
 *    //do something
 *    
 * }finally{
 * 	  lock.unlock();
 * }
 * </pre>
 * @company YeePay
 * @author 王伟
 * @author 毕兴伟
 * @since 2010-9-20
 * @version 1.0
 */
public class LockManager {

	private static final Map<Object, ILock> accountLockMap = new HashMap<Object, ILock>();		//账户锁集合
	
	private static byte[] mainLock = new byte[0];		
	
	/**
	 * 获取账户锁
	 * @param accountNo
	 * @return
	 */
	public static ILock getAccountLock(String accountNo) {
		return doGetLock(accountNo, accountLockMap);
	}
	
	/**
	 * 获取锁
	 * @param key
	 * @param lockMap		锁集合
	 * @return
	 */
	private static ILock doGetLock(Object key, Map<Object, ILock> lockMap){
		synchronized(mainLock){
			ILock lock = lockMap.get(key);
			if(lock == null){
				lock = new ObjectLock(key);
				lockMap.put(key, lock);
			}
			ObjectLock objLock = (ObjectLock)lock;
			objLock.addCounter();
			return objLock;
		}
	}

	//对象锁 实体
	private static class ObjectLock implements ILock{

		private final Lock lock = new ReentrantLock();
		
		private Object lockKey;
		
		private int counter = 0;
		
		public ObjectLock(Object lockKey){
			this.lockKey = lockKey;
		}
		
		/**
		 * 增加计数器
		 */
		public void addCounter(){
			counter++;
		}
		
		/**
		 * 减少计数器
		 */
		private void decreaseCounter(){
			synchronized(mainLock){
				counter--;
				if(counter == 0)
					accountLockMap.remove(this.lockKey);
			}
		}
		
		/**
		 * 将对象加锁
		 */
		@Override
		public void lock() {
			lock.lock();
		}

		/**
		 * 将对象解锁
		 */
		@Override
		public void unlock() {
			decreaseCounter();
			lock.unlock();
		}
	}
}
