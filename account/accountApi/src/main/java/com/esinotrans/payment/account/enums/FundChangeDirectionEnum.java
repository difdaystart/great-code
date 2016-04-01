/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.enums;

/**
 * 资金变动方向 枚举<br>
 * 定义了资金在交易过程中的变动方向<br>
 * INCREMENT 表示资金对应的是从某个账户上增加资金<br>
 * DECREASE 对应减少<br>
 * @company esinotrans
 * @author yzjia
 * @since 2013-7-3 上午10:59:30
 * @version 1.0
 */
public enum FundChangeDirectionEnum {
	/** 资金变动方向增加 */
	INCREMENT,		
	/** 资金变动方向减少 */
	DECREASE;

	@Override
	public String toString() {
		return this.name();
	}		
	
}
