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
public class FieldKey {

	private String name;
	  private Class type;

	  public FieldKey()
	  {
	  }

	  public FieldKey(String name, Class type)
	  {
	    this.name = name;
	    this.type = type;
	  }

	  public String getName() {
	    return this.name; }

	  public void setName(String name) {
	    this.name = name; }

	  public Class getType() {
	    return this.type; }

	  public void setType(Class type) {
	    this.type = type;
	  }

	  public boolean equals(Object obj) {
	    if (!(obj instanceof FieldKey)) return false;
	    FieldKey key = (FieldKey)obj;

	    return ((getName().equals(key.getName())) && 
	      (getType().equals(key.getType())));
	  }

	  public int hashCode() {
	    return (37 * getName().hashCode() + 37 * getType().hashCode());
	  }
}
