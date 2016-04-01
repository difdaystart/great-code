/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.esinotrans.payment.account.dto.AccountHistoryParam;
import com.esinotrans.payment.account.dto.AccountParam;
import com.esinotrans.payment.account.dto.FundFrozenRecordResultDTO;
import com.esinotrans.payment.account.dto.GetAccountHistoryParam;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.AccountProvider;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenRecord;
import com.esinotrans.payment.account.enums.AccountStatusEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.facade.AccountQueryFacade;
import com.esinotrans.payment.account.service.AccountQueryService;
import com.esinotrans.payment.common.enums.CurrencyEnum;
import com.esinotranse.payment.utils.CheckUtils;

/**
 * 帐户查询
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:58:09
 * @version 1.0
 */
@Transactional(rollbackFor = { AccountException.class })
public class AccountQueryBiz implements AccountQueryFacade {

	private AccountQueryService accountQueryService;			//账户查询服务
	
	private static final Logger logger = LoggerFactory
			.getLogger(AccountQueryBiz.class);

	
	/**
	 * 获得账户信息<br>
	 * @param accountNo 账户号
	 * @return AccountParam 账户信息
	 */
	public AccountParam getAccountByAccountNo(String accountNo){
		CheckUtils.valueIsNull(accountNo, "accountNo");
		Account qryAccount = accountQueryService.getAccountByAccountNo(accountNo);
		return populateAccountParam(qryAccount);
	}

	/**
	 * 获得此账户历史记录<br>
	 * 必须的参数：<br>
	 * 账户号：accountNo ：String<br>
	 * 开始时间：StartDate ：Date<br>
	 * 截止时间：endDate ：Date<br>
	 * 
	 * @param accountNo
	 *            账户号
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            截止时间
	 * @return GetAccountHistoryParam 账户历史信息
	 */
	@Override
	public GetAccountHistoryParam getAccountHistory(String accountNo,
			Date startDate, Date endDate) {
		CheckUtils.notEmpty(accountNo, "accountNo");
		logger.info("getAccountHistory, accountNo is [{}], startDate is [{}], endDate is [{}]", 
					new Object[]{accountNo, startDate, endDate});
		
		// 1.通过service 获得账户账户历史dto列表
		List<AccountHistoryParam> historys = accountQueryService
				.getAccountHistory(accountNo, startDate, endDate);
		if (historys.size() > 0) {
			// 2. 设置返回值
			GetAccountHistoryParam getAccountHistoryParam = new GetAccountHistoryParam();
			getAccountHistoryParam.setAccountNo(accountNo);
			getAccountHistoryParam.setHistory(historys);
			return getAccountHistoryParam;
		} 
		return null;
	}

	/**
	 * 获得此客户拥有的所有账户<br>
	 * 必须的参数：<br>
	 * 客户id：customerID ：long<br>
	 * 
	 * @param customerID
	 *            客户ID
	 * @return List 账户参数列表
	 */
	@Override
	public List<AccountParam> getAccountsByCustomerNo(String customerNo) {
		
		logger.info("getAccountsByCustomerNo, customerNo is [{}]", customerNo);
		CheckUtils.notEmpty(customerNo, "customerNo");
			
		// 对账户类型表 进行查询，并把结果放进列表里
		List<Account> accounts = accountQueryService.getAccountsByCustomerNo(customerNo);
		
		// 将map 对象转化成 AccountParam 对象
		return populateAccountParam(accounts);
		
	}
	
	@Override
	public List<FundFrozenRecordResultDTO> getFundFrozenRecords(String accountNo) {
		CheckUtils.notNull(accountNo, "accountNo");
		List<FundFrozenRecordResultDTO> rtnRecords = new ArrayList<FundFrozenRecordResultDTO>();
		List<FundFrozenUnFrozenRecord> qryList = accountQueryService.getFundFrozenRecords(accountNo);
		for(FundFrozenUnFrozenRecord record : qryList){
			rtnRecords.add(populateFundFrozen(record));
		}
		return rtnRecords;
	}
	
	/**
	 * 验证该帐户是否能进行存入操作
	 * @param accountNo
	 * 			账户号
	 * @return
	 */
	@Override
	public boolean checkCanCredit(String accountNo){
		CheckUtils.notNull(accountNo, "accountNo");
		Account account = accountQueryService.getAccountByAccountNo(accountNo);
		if(account.validateCreditAccountStatus())
			return true;
		return false;
	}
	
	/**
	 * 验证该帐户是否能进行扣款操作
	 */
	@Override
	public void checkCanDebit(String accountNo,BigDecimal amount){
		CheckUtils.notNull(accountNo, "accountNo");
		Account account = accountQueryService.getAccountByAccountNo(accountNo);
		if(!account.validateDebitAccountStatus())
			throw AccountException.ACCOUNT_STATUS_IS_NOT_VALID.newInstance("账户的状态异常，不能进行扣款操作");
		if(amount.compareTo(account.getAvailableBalance()) > 0)
			throw AccountException.AVAILABLE_BALANCE_ISNOT_ENOUGH.newInstance("账户可用余额不足，不能进行扣款操作");
	}
	
	/**
	 * 填充资金冻结解冻记录
	 * @param record
	 * @return
	 */
	private FundFrozenRecordResultDTO populateFundFrozen(FundFrozenUnFrozenRecord record){
		FundFrozenRecordResultDTO dto = new FundFrozenRecordResultDTO();
		dto.setTradeFlowId(record.getTradeFlowId());
		dto.setAccountNo(record.getAccountNo());
		dto.setInitiator(record.getInitiator());
		dto.setOperationType(record.getOperationType() == null?"":record.getOperationType().name());
		dto.setFrozenAmount(record.getFrozenAmount());
		dto.setUnfrozenAmount(record.getUnfrozenAmount());
		dto.setCredential(record.getCredential());
		dto.setLastModifyDate(record.getLastModifyDate());
		dto.setAutoUnfrozenDate(record.getAutoUnfrozenDate());
		dto.setCreateDate(record.getCreateDate());
		dto.setComment(record.getComment());
		return dto;
	}

	/*
	 * 把 Account列表 对象转化成 AccountParam列表
	 * 
	 * @param accounts
	 * 
	 * @return
	 */

	private List<AccountParam> populateAccountParam(List<Account> accounts) {
		
		List<AccountParam> accountParams = new ArrayList<AccountParam>();
		if(accounts.size() <= 0 )
			return accountParams;

		AccountParam accountParam = null;
		for (Account accountMap : accounts) {
			accountParam = new AccountParam();
			BeanUtils.copyProperties(accountMap, accountParam);
			accountParam.setCurrency((accountMap.getCurrencyEnum().name()));
			AccountStatusEnum status = accountMap.getAccountStatus();
			accountParam.setAccountStatusName(status == null?null:status.name());
			
			//查询账户提供方		
			AccountProvider provider = accountQueryService.queryAccountProvider((Long)accountMap.getAccountProviderId());
			if(provider != null){
				accountParam.setAccountProviderCode(provider.getProviderCode());
				accountParam.setAccountProviderName(provider.getProviderName());
			}
			accountParams.add(accountParam);
		}
		return accountParams;
	}
	
	/**
	 * 填充账户参数实体
	 * @param origAccount
	 * @return
	 */
	private AccountParam populateAccountParam(Account origAccount){
		if(origAccount == null)
			return null;
		AccountParam accountParam = new AccountParam();
		BeanUtils.copyProperties(origAccount, accountParam);
		accountParam.setAccountStatusName(origAccount.getAccountStatus().name());
		accountParam.setCurrency(origAccount.getCurrencyEnum().name());
		
		//查询账户提供方		
		AccountProvider provider = accountQueryService.queryAccountProvider(origAccount.getAccountProviderId());
		if(provider != null){
			accountParam.setAccountProviderCode(provider.getProviderCode());
			accountParam.setAccountProviderName(provider.getProviderName());
		}
		return accountParam;
	}

	public void setAccountQueryService(AccountQueryService accountQueryService) {
		this.accountQueryService = accountQueryService;
	}

}
