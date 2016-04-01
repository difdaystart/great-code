/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.locator;

import java.util.List;
import java.util.Map;

/**
 * Bean定位器，通过此接口能够获得相应的Bean实例
 * @company YeePay
 * @author 王伟
 * @since 2010-8-27
 * @version 1.0
 */
public interface IBeanLocator {
	
	/**
	 * 获得具有此BeanName的bean实例
	 * @param beanName
	 * @return Object
	 */
	public Object getBean(String beanName);
	
	/**
	 * 获得具有此beanType的一个bean实例
	 * @param beanType
	 * @return Object
	 */
	public Object getBean(Class beanType);
	
	/**
	 * 获得具有此beanName，并且符合beanType类型的bean实例
	 * @param beanName
	 * @param beanType
	 * @return Object
	 */
	public Object getBean(String beanName, Class beanType);
	
	/**
	 * 获得具有此BeanType的所有bean对象Map
	 * @param beanType
	 * @return Map key:beanName value:bean instance
	 */
	public Map<String, Object> getBeanOfType(Class beanType);
	
	/**
	 * 获得具有此beanType的beanName列表
	 * @return List<String>
	 */
	public List<String> getBeanNameForType(Class beanType);
	
	/**
	 * 获得具有此BeanType的所有bean对象Map
	 * @param beanType
	 * @return Map  key:beanType  value: 实现此beanType的所有bean实例
	 */
	public Map<Class, List> getBeanMapOfType(Class beanType);

}
