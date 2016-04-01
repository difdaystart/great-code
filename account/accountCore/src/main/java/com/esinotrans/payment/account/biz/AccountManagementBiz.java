/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.esinotrans.payment.account.annotation.RetryingTransaction;
import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.dto.CreateAccountParam;
import com.esinotrans.payment.account.entity.AccountFrozenRecord;
import com.esinotrans.payment.account.enums.AccountOperationEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.facade.AccountManagementFacade;
import com.esinotrans.payment.account.service.AccountManagementService;
import com.esinotranse.payment.utils.CheckUtils;
import com.esinotranse.payment.utils.StringUtils;

/**
 * 账户管理Biz 实现account-api 中的IAccountManagementFacade 接口 使得core 对api有依赖
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-16 下午5:08:54
 * @version 1.0
 */

@Transactional(rollbackFor = { AccountException.class })
public class AccountManagementBiz extends AbstractAccountBiz implements
		AccountManagementFacade {
	
	private AccountManagementService accountManagementService;			//账户管理服务
	
	private static final Logger logger = LoggerFactory.getLogger(AccountManagementBiz.class);

	/**
	 * 创建账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 账户类型：accountType：String <br>
	 * 客户ID: customerNo: Long <br>
	 * 
	 * @param createAccountParam
	 * @throws AccountException
	 *             账户异常
	 * @return 账户号
	 */
	@Override
	public String createAccount(ContextParam contextParam,
			CreateAccountParam createAccountParam) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, createAccountParam },
						new String[] { CheckUtils.COMMON_FIELD,"accountType,customerNo,accountProviderCode" });
		logger.info("createAccount ,flowId:{},accountType:{},balance:{},customerNo:{}",contextParam.getFlowID(),
				createAccountParam.getAccountType(),createAccountParam.getBalance(),createAccountParam.getCustomerNo());
		String accountNo = accountManagementService.createAccount(contextParam,
				createAccountParam);
		saveTransactionFlow(accountNo,contextParam,AccountOperationEnum.CREATE_ACCOUNT.name());
		return accountNo;
	}
	
	/**
	 * 创建账户
	 */
	@Override
	public String createAccount(ContextParam contextParam,
			CreateAccountParam createAccountParam, String status,
			Date createDate,Long accountHistorySerial) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, createAccountParam },
				new String[] { CheckUtils.COMMON_FIELD,"accountType,customerNo,accountProviderCode" });
		logger.info("createAccount ,flowId:{},accountType:{},balance:{},customerNo:{}",contextParam.getFlowID(),
				createAccountParam.getAccountType(),createAccountParam.getBalance(),createAccountParam.getCustomerNo());
		String accountNo = accountManagementService.createAccount(contextParam,
				createAccountParam,status,createDate,accountHistorySerial);
		saveTransactionFlow(accountNo,contextParam,AccountOperationEnum.CREATE_ACCOUNT.name());
		return accountNo;
	}

	/**
	 * 冻结止收 <br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param unfrozenDate
	 *            解冻时间
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public String forbidCredit(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, accountNo },
				new String[] { CheckUtils.COMMON_FIELD, "accountNo" });
		logger.info(
				"forbidCredit, flowId is [{}], accountNo is [{}], unfrozenDate is [{}]",
				new Object[] { contextParam.getFlowID(), accountNo, unfrozenDate });
		accountNo = StringUtils.trim(accountNo);
		saveTransactionFlow(accountNo, contextParam,
				AccountOperationEnum.FREEZE_CREDIT.name());
		return accountManagementService.forbidCredit(contextParam, accountNo,
				unfrozenDate, credential);
	}

	/**
	 * 冻结止付<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param unfrozenDate
	 *            解冻时间
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public String forbidDebit(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, accountNo },
				new String[] { CheckUtils.COMMON_FIELD, "accountNo" });
		logger.info(
				"forbidDebit ,flowId is [{}], accountNo is [{}], unfrozenDate is [{}]",
				new Object[] { contextParam.getFlowID(), accountNo, unfrozenDate });
		accountNo = StringUtils.trim(accountNo);
		saveTransactionFlow(accountNo, contextParam,
				AccountOperationEnum.FREEZE_DEBIT.name());
		return accountManagementService.forbidDebit(contextParam, accountNo,
				unfrozenDate, credential);
	}

	/**
	 * 冻结账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param unfrozenDate
	 *            解冻时间
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public String freezeAccount(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, accountNo },
				new String[] { CheckUtils.COMMON_FIELD, "accountNo" });
		logger.info(
				"freezeAccount, flowId is [{}], accountNo is [{}], unfrozenDate is [{}]",
				new Object[] { contextParam.getFlowID(), accountNo, unfrozenDate });
		accountNo = StringUtils.trim(accountNo);
		saveTransactionFlow(accountNo, contextParam,
				AccountOperationEnum.FREEZE_ACCOUNT.name());
		return accountManagementService.freezeAccount(contextParam, accountNo,
				unfrozenDate, credential);
	}

	/**
	 * 注销账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 账户号：accountNo：String <br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void removeAccount(ContextParam contextParam, String accountNo)
			throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, accountNo },
				new String[] { CheckUtils.COMMON_FIELD, "accountNo" });
		logger.info("removeAccount ,flowId is  [{}], accountNo is [{}] ",
				new Object[] { contextParam.getFlowID(), accountNo });
		accountNo = StringUtils.trim(accountNo);
		saveTransactionFlow(accountNo, contextParam,
				AccountOperationEnum.REMOVE_ACCOUNT.name());
		accountManagementService.removeAccount(accountNo, contextParam);
	}

	/**
	 * 解冻账户<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void unfreezeAccount(ContextParam contextParam, String accountNo,
			String credential) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, accountNo },
				new String[] { CheckUtils.COMMON_FIELD, "accountNo" });
		logger.info("unfreezeAccount , flowId is [{}], accountNo is [{}] ",
				new Object[] { contextParam.getFlowID(), accountNo });
		accountNo = StringUtils.trim(accountNo);
		saveTransactionFlow(accountNo, contextParam,
				AccountOperationEnum.UNFREEZE_ACCOUNT.name());
		accountManagementService.unfreezeAccount(accountNo, credential,
				contextParam);
	}

	/**
	 * 自动解冻账户 for 定时器 必须赋值的属性为：<br>
	 * 时间：currentDate: Date<br>
	 * 
	 * @param currentDate
	 *            时间
	 * @throws AccountException
	 */
	@Override
	public void autoProcessUnfreezeAccountForTimer()
			throws AccountException {

		logger.info(" autoProcessUnfreezeAccountForTimer is started ");
		
		//TODO 需分页查询
		//查询待解冻 的  账户冻结记录
		List<AccountFrozenRecord> frozenRecords = accountManagementService
				.queryAccountFrozenRecordsByUnfrozenDate(new Date());
		Map<String, List<AccountFrozenRecord>> frozenRecordMap = doGroupAccountFrozenRecordByAccountNo(frozenRecords);
		for (Iterator<String> it = frozenRecordMap.keySet().iterator(); it.hasNext();) {
			String accountNo = it.next();
			accountManagementService.autoProcessUnfreezeAccountForTimer(accountNo, frozenRecordMap.get(accountNo));
		}
	}
	
	/**
	 * 按账号分组账户冻结记录集合
	 * @param frozenRecords
	 * @return
	 */
	private Map<String,List<AccountFrozenRecord>> doGroupAccountFrozenRecordByAccountNo(List<AccountFrozenRecord> frozenRecords){
		Map<String,List<AccountFrozenRecord>> rtnFrozenRecordMap = new HashMap<String,List<AccountFrozenRecord>>();
		List<AccountFrozenRecord> records = null;
		for(AccountFrozenRecord record : frozenRecords){
			if(rtnFrozenRecordMap.containsKey(record.getAccountNo())){
				records = rtnFrozenRecordMap.get(record.getAccountNo());
				records.add(record);
			}else{
				records = new ArrayList<AccountFrozenRecord>();
				records.add(record);
				rtnFrozenRecordMap.put(record.getAccountNo(), records);
			}
		}
		return rtnFrozenRecordMap;
	}

	public void setAccountManagementService(
			AccountManagementService accountManagementService) {
		this.accountManagementService = accountManagementService;
	}

}
