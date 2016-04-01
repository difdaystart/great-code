/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.enums;

/**
 * 账户状态
 * 
 * @author 王伟
 * 
 */
public enum AccountStatusEnum {

	ACCOUNT_AVAILABLE, 			// 正常
	ACCOUNT_FROZEN, 				// 冻结
	ACCOUNT_FREEZE_DEBIT, 		// 冻结止付
	ACCOUNT_FREEZE_CREDIT, 		// 冻结止收
	ACCOUNT_CANCELLED; 			// 注销

}
