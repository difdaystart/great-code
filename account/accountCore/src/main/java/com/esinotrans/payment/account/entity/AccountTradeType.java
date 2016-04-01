/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;

/**
 * 账务类型
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:07:41
 * @version 1.0
 */
public class AccountTradeType implements Serializable{

	private static final long serialVersionUID = 7584741578080565189L;
	
	private Long id;
	/**
	 * 账务类型名字
	 */
	private String typeName;
	/**
	 * 账务类型编码
	 */
	private Integer typeCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}
}
