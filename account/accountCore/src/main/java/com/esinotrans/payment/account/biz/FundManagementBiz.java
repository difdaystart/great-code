/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.biz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenRecord;
import com.esinotrans.payment.account.enums.AccountOperationEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.facade.FundManagementFacade;
import com.esinotrans.payment.account.service.FundManagementService;
import com.esinotranse.payment.utils.CheckUtils;
import com.esinotranse.payment.utils.DateUtils;
import com.esinotranse.payment.utils.StringUtils;

/**
 * 资金管理服务
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:58:25
 * @version 1.0
 */
@Transactional(rollbackFor = { AccountException.class })
public class FundManagementBiz extends AbstractAccountBiz implements
		FundManagementFacade {

	private FundManagementService fundManagementService;			//资金管理service

	private static final Logger logger = LoggerFactory.getLogger(FundManagementBiz.class);

	/**
	 * 冻结资金<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 冻结金额：amount：BigDecimal<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param amount
	 *            冻结金额
	 * @param unfrozenDate
	 *            解冻时间
	 * @throws AccountException
	 *             账户异常
	 * @return 授权码
	 */
	@Override
	public String freezeFund(ContextParam contextParam, String accountNo,
			BigDecimal amount, Date unfrozenDate) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, accountNo, amount },
				new String[] { CheckUtils.COMMON_FIELD, "accountNo", "amount" });
		logger.info("freezeFund ,flowId is [{}], accountNo is [{}], amount is [{}], unfrozenDate is [{}]",
						new Object[] { contextParam.getFlowID(), accountNo, amount,unfrozenDate });
		accountNo = StringUtils.trim(accountNo);
		validateAmountIsNegtive(amount);
		saveTransactionFlow(accountNo, contextParam,
				AccountOperationEnum.FREEZE_FUND.name());
		return fundManagementService.freezeFund(contextParam, accountNo,
				amount, unfrozenDate);
	}

	/**
	 * 设置冻结资金的自动解冻时间<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 解冻时间：unfrozenDate：Date<br>
	 * 授权码: credential: String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param unfrozenDate
	 *            解冻时间
	 * @param credential
	 *            授权码
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	public void setThawDate(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, accountNo,
				unfrozenDate, credential }, new String[] {CheckUtils.COMMON_FIELD, "accountNo", "unfrozenDate","credential" });
		logger.info("setThawDate ,flowId is [{}], accountNo is [{}], unfrozenDate is [{}], credential is [{}] ",
						new Object[] { contextParam.getFlowID(), accountNo, unfrozenDate,credential });
		accountNo = StringUtils.trim(accountNo);
		saveTransactionFlow(accountNo, contextParam,
				AccountOperationEnum.SET_THAW_DATE.name());
		fundManagementService.setThawDate(contextParam, accountNo,
				unfrozenDate, credential);

	}

	/**
	 * 解冻指定金额的资金<br>
	 * 必须赋值的属性为：<br>
	 * 流水号：flowID: String<br>
	 * 发起方：initiator: String<br>
	 * 账户号：accountNo：String<br>
	 * 解冻金额：amount：BigDecimal<br>
	 * 授权码: credential: String<br>
	 * 
	 * @param contextParam
	 *            上下文参数
	 * @param accountNo
	 *            账号
	 * @param amount
	 *            解冻金额
	 * @param credential
	 *            授权码
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	public void unfreezeFund(ContextParam contextParam, String accountNo,
			BigDecimal amount, String credential) throws AccountException {
		CheckUtils.valueIsNull(new Object[] { contextParam, accountNo, amount,
				credential }, new String[] { CheckUtils.COMMON_FIELD,
				"accountNo", "amount", "credential" });
		logger.info("unfreezeFund, flowId is [{}], accountNo is [{}], amount is [{}], credential is [{}] ",
						new Object[] { contextParam.getFlowID(), accountNo, amount,credential });
		accountNo = StringUtils.trim(accountNo);
		validateAmountIsNegtive(amount);
		saveTransactionFlow(accountNo, contextParam,
				AccountOperationEnum.UNFREEZE_FUND.name());
		fundManagementService.unfreezeFund(contextParam, accountNo, amount,
				credential);
	}

	/**
	 * 定时解冻资金
	 */
	@Override
	public void unfreezeFundByTimer() {
		logger.info(" unfreezeFundByTimer is started ");
		Date currentDate = new Date();
		List<FundFrozenUnFrozenRecord> records = fundManagementService
					.queryAllAutoUnfrozenFundRecords(currentDate);
		for (Iterator<FundFrozenUnFrozenRecord> iter = records.iterator(); iter
				.hasNext();) {
			fundManagementService.unfreezeFundByTimer(currentDate, iter.next());
		}
	}
	
	@Override
	public void accountFundSelfCheck() {
		logger.info(" accountFundSelfCheck is started ");
		fundManagementService.accountFundSelfCheck(DateUtils.getDayStart(new Date())
				, DateUtils.getDayEnd(new Date()));
	}

	@Override
	public void accountFundSelfCheck(String[] accountNos, Date startDate,
			Date endDate) {
		if(accountNos == null || accountNos.length == 0)
			return;
		CheckUtils.notNull(new Object[]{startDate,endDate}, "startDate","endDate");
		fundManagementService.accountFundSelfCheck(accountNos, DateUtils.getDayStart(startDate), DateUtils.getDayEnd(endDate));
		
	}

	public void setFundManagementService(FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

}
