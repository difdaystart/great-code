/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.locator.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.esinotrans.payment.account.locator.IBeanLocator;

/**
 * 与Spring容器结合，提供Bean获得功能
 * @company YeePay
 * @author 王伟
 * @since 2010-8-27
 * @version 1.0
 */

@Component
public class SpringBeanLocator implements IBeanLocator, ApplicationContextAware{

	private ApplicationContext context;
	
	@Override
	public Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	@Override
	public Object getBean(Class beanType) {
		return context.getBean(beanType);
	}
	
	@Override
	public Object getBean(String beanName, Class beanType){
		try{
			return context.getBean(beanName, beanType);
		}catch(BeanNotOfRequiredTypeException e){
			return null;
		}
	}
	
	@Override
	public Map<String, Object> getBeanOfType(Class beanType){
		return context.getBeansOfType(beanType);
	}
	
	@Override
	public List<String> getBeanNameForType(Class beanType) {
		return Arrays.asList(context.getBeanNamesForType(beanType));
	}
	
	@Override
	public Map<Class, List> getBeanMapOfType(Class beanType) {
		Map<Class, List> resultMap = new HashMap<Class, List>();
		List beanInstances = new ArrayList();
		List<String> beanNames = this.getBeanNameForType(beanType);
		for(Iterator<String> iter = beanNames.iterator(); iter.hasNext();){
			String beanName = iter.next();
			Object beanInstance = this.getBean(beanName, beanType);
			beanInstances.add(beanInstance);
		}
		resultMap.put(beanType, beanInstances);
		return resultMap;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}
}
