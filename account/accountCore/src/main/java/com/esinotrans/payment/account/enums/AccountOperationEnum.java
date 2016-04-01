/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.enums;

import java.io.Serializable;

/**
 * 账户操作类型 枚举<br>
 *定义了一些账户管理操作类型的枚举变量 
 *如：FREEZE_DEBIT 是冻结某个账户的支付操作，使账户不可以做支付
 *FREEZE_FUND 冻结某个账户的部分余额（用来做某些特殊的操作）
 * @author 王伟
 *
 */
public enum AccountOperationEnum implements Serializable{
	
	/** 冻结止付 */
	FREEZE_DEBIT,
	
	/**解冻止付 */
	UNFREEZE_DEBIT,
	
	/**冻结止收 */
	FREEZE_CREDIT,	
	
	/** 解冻止收 */
	UNFREEZE_CREDIT,
	
	/**冻结账户 */					
	FREEZE_ACCOUNT,	
	
	/**解冻账户 */
	UNFREEZE_ACCOUNT,
	
	/**创建付款账户 */
	CREATE_ACCOUNT,
	
	/**冻结资金 */
	FREEZE_FUND,
	
	/** 解冻资金 */
	UNFREEZE_FUND,	
	
	/** 注销账户 */
	REMOVE_ACCOUNT,
	
	/**设置冻结资金的自动解冻时间 */
	SET_THAW_DATE,
	
	/** 更改账户冻结记录*/
	UPDATE_ACCOUNT_FREEZE_STATUS,
	
	/** 定时解冻资金*/
	UNFREEZE_FUND_BY_TIMER;
}
