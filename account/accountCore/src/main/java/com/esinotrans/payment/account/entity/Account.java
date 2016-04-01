/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.esinotrans.payment.account.enums.AccountStatusEnum;
import com.esinotrans.payment.common.enums.CurrencyEnum;
import com.esinotranse.payment.utils.MathUtils;

/**
 * 账户实体
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午10:04:49
 * @version 1.0
 */
public class Account implements Serializable{

	private static final long serialVersionUID = -9008780941752356875L;

	private Long id;

	/** 账号 */
	private String accountNo;

	/**账户类型 */
	private String accountType;

	/** 账户状态 */
	private AccountStatusEnum accountStatus;
	
	/**  账户状态账户码值 */
	private Integer accountStatusCode; 

	/** 账户余额 */
	private BigDecimal balance = BigDecimal.ZERO;
	
	/**
	 * 余额签名
	 */
	private String balanceSign;
	
	/**币种*/
	private CurrencyEnum currencyEnum;
	
	/**账户提供方*/
	private Long accountProviderId;

	/** 客户 */
	private String customerNo;

	/** 交易密码 */
	private String transactionPassword;

	/** 可提现额度 */
	private BigDecimal availableWithdrawQuota = BigDecimal.ZERO;
	
	/**
	 * 可充退额度
	 */
	private BigDecimal availableRechargeRefundQuota = BigDecimal.ZERO;

	/** 冻结额度 */
	private BigDecimal frozenQuota = BigDecimal.ZERO;
	
	/** 账户历史流水顺序号 */
	private Long accountHistorySerial;
	
	/** 账户管理顺序号 */
	private Long manageSerial;
	
	/** 冻结解冻记录顺序号 */
	private Long frozenSerial;
	
	/** 版本号 */
	private Long version;
	
	/**
	 * 最近快照日期
	 */
	private Date snapshotDate;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 备注
	 */
	private String remark;
	
	
	public Account(){
		
	}
	
	/**
	 * 减少冻结额度
	 */
	public void decreaseFrozenQuota(BigDecimal amount){
		this.frozenQuota = this.frozenQuota.subtract(amount);
	}
	
	/**
	 * 增加冻结额度
	 * @param amount
	 */
	public void increaseFrozonQuota(BigDecimal amount){
		this.frozenQuota = this.frozenQuota.add(amount);
		this.frozenSerial = this.frozenSerial + 1;
	}

	/**
	 * 获得可用余额
	 * 
	 * @return
	 */
	public BigDecimal getAvailableBalance() {
		return balance.subtract(frozenQuota);
	}
	
	/**
	 * 验证存入时账户账户状态
	 * @return
	 */
	public boolean validateCreditAccountStatus(){
		if (AccountStatusEnum.ACCOUNT_FROZEN.equals(getAccountStatus())
				|| AccountStatusEnum.ACCOUNT_FREEZE_CREDIT.equals(getAccountStatus())
				|| AccountStatusEnum.ACCOUNT_CANCELLED.equals(getAccountStatus()))
			return false;
		return true;
	}

	/**
	 * 存入
	 * 
	 * @param amount
	 */
	public void credit(BigDecimal amount) {
		this.balance = this.balance.add(amount);
	}
	
	/**
	 * 验证支出时账户状态
	 * @return
	 */
	public boolean validateDebitAccountStatus(){
		if (AccountStatusEnum.ACCOUNT_FROZEN.equals(getAccountStatus())
				|| AccountStatusEnum.ACCOUNT_FREEZE_DEBIT.equals(getAccountStatus())
				|| AccountStatusEnum.ACCOUNT_CANCELLED.equals(getAccountStatus()))
			return false;
		return true;
	}
	
	/**
	 * 验证可用余额是否足够
	 * @param amount
	 * @return
	 */
	public boolean availableBalanceIsEnough(BigDecimal amount){
		BigDecimal diff = this.getAvailableBalance().subtract(amount);
		if (diff.compareTo(BigDecimal.ZERO) < 0)
			return false;
		return true;
	}
	
	/**
	 * 支出
	 * 
	 * @param amount
	 */
	public void debit(BigDecimal amount) {
		this.balance = this.balance.subtract(amount);
	}

	/**
	 * 判断是否满足可提现额度限制
	 * true:满足,false:不满足
	 * @param amount
	 * @return
	 */
	public boolean isSatisfyWithdrawQuota(BigDecimal amount){
		BigDecimal diff = this.availableWithdrawQuota.subtract(amount);
		if(diff.compareTo(BigDecimal.ZERO) < 0 )
			return false;
		return true;
	}
	
	/**
	 * 增加可提现额度
	 * @param amount
	 */
	public void increaseWithdrawQuota(BigDecimal amount) {
		if (this.availableWithdrawQuota != null) {
			this.availableWithdrawQuota = this.availableWithdrawQuota.add(amount);
			if(this.availableWithdrawQuota.compareTo(this.balance) > 0)
				this.availableWithdrawQuota = this.balance;
		}
	}
	
	/**
	 * 减少可提现额度
	 * @param amount
	 */
	public void decreaseWithdrawQuota(BigDecimal amount){
		if (this.availableWithdrawQuota != null) {
			this.availableWithdrawQuota = this.availableWithdrawQuota.subtract(amount);
			if(availableWithdrawQuota.compareTo(BigDecimal.ZERO) < 0)
				this.availableWithdrawQuota = BigDecimal.ZERO;
		}
	}
	
	/**
	 * 判断是否满足可充退额度限制
	 * true:满足,false:不满足
	 * @param amount
	 * @return
	 */
	public boolean isSatisfyRechargeRefundQuota(BigDecimal amount){
		BigDecimal diff = this.availableRechargeRefundQuota.subtract(amount);
		if(diff.compareTo(BigDecimal.ZERO) < 0 )
			return false;
		return true;
	}
	
	/**
	 * 增加可充退额度
	 * @param amount
	 */
	public void increaseRechargeRefundQuota(BigDecimal amount) {
		if (this.availableRechargeRefundQuota != null) {
			this.availableRechargeRefundQuota = this.availableRechargeRefundQuota.add(amount);
			if(this.availableRechargeRefundQuota.compareTo(this.balance) > 0)
				this.availableRechargeRefundQuota = this.balance;
		}
	}
	
	/**
	 * 减少可充退额度
	 * @param amount
	 */
	public void decreaseRechargeRefundQuota(BigDecimal amount){
		if (this.availableRechargeRefundQuota != null) {
			this.availableRechargeRefundQuota = this.availableRechargeRefundQuota.subtract(amount);
			if(availableRechargeRefundQuota.compareTo(BigDecimal.ZERO) < 0)
				this.availableRechargeRefundQuota = BigDecimal.ZERO;
		}
	}
	
	/**
	 * 检查各种额度与余额比对情况
	 * 各种额度金额不得大于余额
	 */
	public void checkQuotaLimit(){
		if(this.availableWithdrawQuota.compareTo(this.balance) > 0)
			this.availableWithdrawQuota = this.balance;
		if(this.availableRechargeRefundQuota.compareTo(this.balance) > 0)
			this.availableRechargeRefundQuota = this.balance;
	}

	/**
	 * 获得余额与可提现额的差值
	 * 
	 * @return
	 */
	public BigDecimal getBalanceWithdrawQuotaDiff() {
		return this.balance.subtract(this.availableWithdrawQuota);
	}
	
	/**
	 * 四舍五入余额与控制额度
	 */
	public void roundQuotaAndBalance(){
		this.balance = MathUtils.round(this.balance);
		this.availableWithdrawQuota = MathUtils.round(this.availableWithdrawQuota);
		this.frozenQuota = MathUtils.round(this.frozenQuota);
	}
	
	/**
	 * 激活账户，账户状态为可用
	 */
	public void activate(){
		this.accountStatusCode = Integer.valueOf(1000000);
		this.accountStatus = AccountStatusEnum.ACCOUNT_AVAILABLE;
	}
	
	/**
	 * 注销账户
	 */
	public void cancelled(){
		this.accountStatusCode = Integer.valueOf(9000000);
		this.accountStatus = AccountStatusEnum.ACCOUNT_CANCELLED;
		this.manageSerial = (this.manageSerial == null?0:this.manageSerial) + 1;
	}
	
	/**
	 * 冻结账户
	 */
	public void frozen(){
		if(this.accountStatusCode.intValue() < 2000000)
			this.accountStatusCode = Integer.valueOf(2000000);
		this.accountStatusCode = Integer.valueOf(this.accountStatusCode.intValue() + 10000);
		this.accountStatus = calculateAccountStatus();
		this.manageSerial = (this.manageSerial == null?0:this.manageSerial) + 1;
	}
	
	/**
	 * 冻结止付
	 */
	public void freezeDebit(){
		if(this.accountStatusCode.intValue() < 2000000)
			this.accountStatusCode = Integer.valueOf(2000000);
		this.accountStatusCode = Integer.valueOf(this.accountStatusCode.intValue() + 100);
		this.accountStatus = calculateAccountStatus();
		this.manageSerial = (this.manageSerial == null?0:this.manageSerial) + 1;
	}
	
	/**
	 * 冻结止收
	 */
	public void freezeCredit(){
		if(this.accountStatusCode.intValue() < 2000000)
			this.accountStatusCode = Integer.valueOf(2000000);
		this.accountStatusCode = Integer.valueOf(this.accountStatusCode.intValue() + 1);
		this.accountStatus = calculateAccountStatus();
		this.manageSerial = (this.manageSerial == null?0:this.manageSerial) + 1;
	}
	
	/**
	 * 解冻账户
	 */
	public void unfrozen(){
		this.accountStatusCode = Integer.valueOf(this.accountStatusCode.intValue() - 10000);
		if(this.accountStatusCode.intValue() == 2000000)
			this.accountStatusCode = Integer.valueOf(1000000);
		this.accountStatus = calculateAccountStatus();
		this.manageSerial = (this.manageSerial == null?0:this.manageSerial) + 1;
	}
	
	/**
	 * 解冻止付
	 */
	public void unfreezeDebit(){
		this.accountStatusCode = Integer.valueOf(this.accountStatusCode.intValue() - 100);
		if(this.accountStatusCode.intValue() == 2000000)
			this.accountStatusCode = Integer.valueOf(1000000);
		this.accountStatus = calculateAccountStatus();
		this.manageSerial = (this.manageSerial == null?0:this.manageSerial) + 1;
	}
	
	/**
	 * 解冻止收
	 */
	public void unfreezeCredit(){
		this.accountStatusCode = Integer.valueOf(this.accountStatusCode.intValue() - 1);
		if(this.accountStatusCode.intValue() == 2000000)
			this.accountStatusCode = Integer.valueOf(1000000);
		this.accountStatus = calculateAccountStatus();
		this.manageSerial = (this.manageSerial == null?0:this.manageSerial) + 1;
	}
	
	/**
	 * 增加账户管理顺序号
	 */
	public void increaseManageSeralNum(){
		this.manageSerial = (this.manageSerial == null?0:this.manageSerial) + 1;
	}
	
	/**
	 * 获取账户状态
	 * 账户状态用4位定长数字表示。
	 * 1000000: 正常状态
	 * 2000000 – 2999999 冻结状态（冻结、冻结止收、冻结止付） 其中，冻结的状态表示如下：2 [冻结][冻结止付][冻结止收]
	 * 9000000: 表示注销状态
	 * @return
	 */
	private AccountStatusEnum calculateAccountStatus() {
		if(accountStatusCode == null)
			return this.accountStatus;
		int code = accountStatusCode.intValue();
		if(code == 1000000){
			return AccountStatusEnum.ACCOUNT_AVAILABLE;
		}else if(code ==  9000000  ){
			return AccountStatusEnum.ACCOUNT_CANCELLED;
		}else if(code / 10000 % 100 >= 1){ //取冻结位
			return AccountStatusEnum.ACCOUNT_FROZEN;
		}else if(code / 100 % 100 >= 1 && code % 100 >= 1 ) {	
			return AccountStatusEnum.ACCOUNT_FROZEN;
		}else if (code / 100 % 100 >= 1){	//取冻结止付位
			return AccountStatusEnum.ACCOUNT_FREEZE_DEBIT;
		}else if( code % 100 >= 1){			//取冻结止收位
			return AccountStatusEnum.ACCOUNT_FREEZE_CREDIT;
		}
		return this.accountStatus;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getTransactionPassword() {
		return transactionPassword;
	}

	public void setTransactionPassword(String transactionPassword) {
		this.transactionPassword = transactionPassword;
	}

	public BigDecimal getAvailableWithdrawQuota() {
		return availableWithdrawQuota;
	}

	public void setAvailableWithdrawQuota(BigDecimal availableWithdrawQuota) {
		this.availableWithdrawQuota = availableWithdrawQuota;
	}

	public BigDecimal getFrozenQuota() {
		return frozenQuota;
	}

	public void setFrozenQuota(BigDecimal frozenQuota) {
		this.frozenQuota = frozenQuota;
	}

	public Long getAccountHistorySerial() {
		return accountHistorySerial;
	}

	public void setAccountHistorySerial(Long accountHistorySerial) {
		this.accountHistorySerial = accountHistorySerial;
	}

	public Long getManageSerial() {
		return manageSerial;
	}

	public void setManageSerial(Long manageSerial) {
		this.manageSerial = manageSerial;
	}

	public Long getFrozenSerial() {
		return frozenSerial;
	}

	public void setFrozenSerial(Long frozenSerial) {
		this.frozenSerial = frozenSerial;
	}

	public Integer getAccountStatusCode() {
		return accountStatusCode;
	}

	public void setAccountStatusCode(Integer accountStatusCode) {
		this.accountStatusCode = accountStatusCode;
	}

	public AccountStatusEnum getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatusEnum accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public CurrencyEnum getCurrencyEnum() {
		return currencyEnum;
	}

	public void setCurrencyEnum(CurrencyEnum currencyEnum) {
		this.currencyEnum = currencyEnum;
	}

	public Long getAccountProviderId() {
		return accountProviderId;
	}

	public void setAccountProviderId(Long accountProviderId) {
		this.accountProviderId = accountProviderId;
	}

	public String getBalanceSign() {
		return balanceSign;
	}

	public void setBalanceSign(String balanceSign) {
		this.balanceSign = balanceSign;
	}

	public Date getSnapshotDate() {
		return snapshotDate;
	}

	public void setSnapshotDate(Date snapshotDate) {
		this.snapshotDate = snapshotDate;
	}

	public BigDecimal getAvailableRechargeRefundQuota() {
		return availableRechargeRefundQuota;
	}

	public void setAvailableRechargeRefundQuota(
			BigDecimal availableRechargeRefundQuota) {
		this.availableRechargeRefundQuota = availableRechargeRefundQuota;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
