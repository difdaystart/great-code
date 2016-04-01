/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * 反射工具类
 * @company YeePay
 * @author 王伟
 * @since 2010-8-24
 * @version 1.0
 */
public class ReflectionUtil {

	private static final String Setter_Method_Prefix = "set";
	private static final String Getter_Method_Prefix = "get";
	private static final int ZERO = 0;
	private static final int FIRST = 1;

	  /**
	   * 执行目标对象的特定方法
	   * @param target
	   * @param methodName
	   * @return oject 
	   */
	  public static Object executeMethod(Object target, String methodName)
	  {
	    return executeMethod(target, methodName, null, null);
	  }
	  
	  /**
	   * 执行目标对象的特定方法
	   * @param target
	   * @param methodName
	   * @param paramValue
	   * @param paramType
	   * @return Object
	   */
	  public static Object executeMethod(Object target, String methodName, Object[] paramValue, Class[] paramType)
	  {
	    if (paramType == null)
	      paramType = new Class[ZERO];
	    return invokeMethod(target, methodName, paramValue, paramType);
	  }

	  /**
	   * 执行目标对象的field的set方法
	   * @param object
	   * @param fieldName
	   * @param paramValue
	   * @param paramType
	   */
	  public static void executeSetterMethodByField(Object object, String fieldName, Object[] paramValue, Class[] paramType)
	  {
	    checkExecuteMethodParameter(object, fieldName);
	    String methodName = getMethodName(fieldName, Setter_Method_Prefix);
	    invokeMethod(object, methodName, paramValue, paramType);
	  }

	  /**
	   * 执行目标对象的field的get方法
	   * @param object
	   * @param fieldName
	   * @param paramType
	   * @return Object
	   */
	  public static Object executeGetterMethodByField(Object object, String fieldName, Class[] paramType)
	  {
	    if (paramType == null)
	      paramType = new Class[ZERO];
	    return executeGetterMethodByField(object, fieldName, paramType, null);
	  }

	  /**
	   * 执行目标对象的field的get方法
	   * @param object
	   * @param fieldName
	   * @param paramType
	   * @param paramValue
	   * @return Object
	   */
	  public static Object executeGetterMethodByField(Object object, String fieldName, Class[] paramType, Object[] paramValue)
	  {
	    checkExecuteMethodParameter(object, fieldName);
	    String methodName = getMethodName(fieldName, Getter_Method_Prefix);
	    return invokeMethod(object, methodName, paramValue, paramType);
	  }

	  private static void checkExecuteMethodParameter(Object object, String fieldName) {
	    Assert.notNull(object, "arugment object must not null");
	    Assert.notNull(fieldName, "arugment fieldName must not null");
	    Assert.hasLength(fieldName, "arugment fieldName's length must not be 0");
	  }

	  private static String getMethodName(String fieldName, String Method_Prefix) {
	    String firstChar = fieldName.substring(ZERO, FIRST);
	    String otherChar = fieldName.substring(FIRST);
	    return Method_Prefix + firstChar.toUpperCase() + otherChar;
	  }

	  private static Object invokeMethod(Object object, String methodName, Object[] paramValue, Class[] paramType)
	  {
	    Method method = null;
	    try
	    {
	      method = getMethod(object, methodName, paramType);
	      if (method != null)
	        return method.invoke(object, paramValue);
	    } catch (SecurityException e) {
	      throw new RuntimeException(ReflectionUtil.class.getName() + 
	        "Method: invokeMethod" + "occur error", e);
	    } catch (IllegalArgumentException e) {
	      throw new RuntimeException(ReflectionUtil.class.getName() + 
	        "Method: invokeMethod" + "occur error", e);
	    } catch (IllegalAccessException e) {
	      throw new RuntimeException(ReflectionUtil.class.getName() + 
	        "Method: invokeMethod" + "occur error", e);
	    } catch (InvocationTargetException e) {
	      throw new RuntimeException(ReflectionUtil.class.getName() + 
	        "Method: invokeMethod" + "occur error", e);
	    }
	    return null;
	  }

	  /**
	   * 获得目标对象的特定方法
	   * @param target
	   * @param methodName
	   * @param paramType
	   * @return  Method
	   */
	  public static Method getMethod(Object target, String methodName, Class[] paramType) {
	    if (paramType == null)
	      paramType = new Class[ZERO];
	    Method[] methods = target.getClass().getMethods();
	    for (int index = 0; index < methods.length; ++index) {
	      Method method = methods[index];
	      if ((methodNameIsEquals(method, methodName)) && 
	        (methodParameterTypeAreEquals(method, paramType)))
	        return method;
	    }
	    return null;
	  }
	  
	  /**
	   * 根据ClassName获得Class对象
	   * @param className
	   * @return
	   */
	  public static Class classForName(String className){
		  try {
			return Resources.classForName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(ReflectionUtil.class.getName() + 
			        "Method: classForName " + "occur error", e);
		}
	  }

	  private static boolean methodNameIsEquals(Method method, String methodName) {
	    return method.getName().equals(methodName);
	  }

	  private static boolean methodParameterTypeAreEquals(Method method, Class[] types)
	  {
	    Class[] parameterTypes = method.getParameterTypes();
	    if ((parameterTypes == null) && (types == null)) 
	    	return true;
	    if (parameterTypes.length != types.length) 
	    	return false;
	    for (int index = 0; index < parameterTypes.length; ++index) {
	      Class parameterType = parameterTypes[index];
	      Class type = types[index];
	      if (!(parameterType.equals(type)))
	        return false;
	    }
	    return true;
	  }

	  /**
	   * 获得目标对象的所用的Field
	   * @param obj
	   * @return  Map
	   */
	  public static Map getObjAttributeMap(Object obj) {
	    return getObjAttributeMap(obj, new NoConditionFieldFilter());
	  }

	  /**
	   * 获得目标对象的Field
	   * @param obj
	   * @param filter field过滤器，返回false,返回结果不包括此Field
	   * @return map
	   */
	  public static Map getObjAttributeMap(Object obj, IFieldFilter filter)
	  {
	    Map fieldMap = new HashMap();
	    if (obj != null) {
	      Field[] fields = obj.getClass().getDeclaredFields();
	      for (int index = 0; index < fields.length; ++index) {
	        Field field = fields[index];
	        boolean hasCondition = filter.filter(field.getName());
	        if (!(hasCondition)) {
	          FieldKey key = createFieldKey(field);
	          FieldValueObject fieldValueObject = createFieldValueObject(
	            key, field, obj);
	          fieldMap.put(key, fieldValueObject);
	        }
	      }
	    }
	    return fieldMap;
	  }

	  private static FieldKey createFieldKey(Field field) {
	    String fieldName = field.getName();
	    Class type = field.getType();
	    FieldKey key = new FieldKey(fieldName, type);
	    return key;
	  }

	  private static FieldValueObject createFieldValueObject(FieldKey key, Field field, Object target)
	  {
	    Object value = null;
	    try {
	      field.setAccessible(true);
	      value = field.get(target);
	    } catch (IllegalArgumentException e) {
	      throw new RuntimeException(ReflectionUtil.class.getName() + 
	        "Method: getObjAttributeMap" + "occur error", e);
	    } catch (IllegalAccessException e) {
	      throw new RuntimeException(ReflectionUtil.class.getName() + 
	        "Method: getObjAttributeMap" + "occur error", e);
	    }
	    return new FieldValueObject(key, value);
	  }
}
