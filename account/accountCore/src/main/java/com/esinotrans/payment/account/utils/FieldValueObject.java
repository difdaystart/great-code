/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.utils;

/**
 * @company YeePay
 * @author 王伟
 * @since 2010-8-24
 * @version 1.0
 */
public class FieldValueObject {

	 private FieldKey fieldKey;
	  private Object fieldValue;

	  public FieldValueObject()
	  {
	  }

	  public FieldValueObject(FieldKey key, Object value)
	  {
	    this.fieldKey = key;
	    this.fieldValue = value;
	  }

	  public FieldKey getFieldKey() {
	    return this.fieldKey; }

	  public void setFieldKey(FieldKey fieldKey) {
	    this.fieldKey = fieldKey; }

	  public Object getFieldValue() {
	    return this.fieldValue; }

	  public void setFieldValue(Object fieldValue) {
	    this.fieldValue = fieldValue;
	  }
}
