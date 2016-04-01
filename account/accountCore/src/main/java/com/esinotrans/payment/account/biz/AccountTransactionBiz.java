/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.biz;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.esinotrans.payment.account.annotation.RetryingTransaction;
import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.dto.TransactionCommandParam;
import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.facade.AccountTransactionFacade;
import com.esinotrans.payment.account.service.AccountTransactionService;
import com.esinotrans.payment.account.service.FundManagementService;
import com.esinotranse.payment.utils.CheckUtils;
import com.esinotranse.payment.utils.MathUtils;
import com.esinotranse.payment.utils.StringUtils;

/**
 * 账户交易 BIZ
 * @company esinotrans
 * @author yzjia
 * @since 2013-8-19 上午9:58:17
 * @version 1.0
 */

@Transactional(rollbackFor = { AccountException.class })
public class AccountTransactionBiz extends AbstractAccountBiz implements
		AccountTransactionFacade {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountTransactionBiz.class);
	
	private FundManagementService fundManagementService;		//资金管理service
	
	private AccountTransactionService accountTransactionService;	//账户交易服务

	/**
	 * 存入<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 收款方账号：receiverAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 账务类型编码：accountTradeTypeCode<br>
	 * 
	 * @param param
	 *            账户交易命令参数
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void credit(TransactionCommandParam param) throws AccountException {
		CheckUtils.valueIsNull(param, CheckUtils.COMMON_FIELD
				+ "receiverAccountNo,amount,tradeType");
		logger.info("credit,receiverAccountNo:{},amount:{},tradeType:{}", param.getReceiverAccountNo(),
				param.getAmount(),param.getTradeType());

		trimAccountNo(param);
		validateAmountIsNegtive(param.getAmount());
		saveTransactionFlow(param.getReceiverAccountNo(), param,param.getTradeType());
		doCredit(param);
	}

	/**
	 * 批量存入<br>
	 * 必须赋值的属性和 credit 方法一样<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void batchCredit(List<TransactionCommandParam> params)
			throws AccountException {
		logger.info("batchCredit start");
		for (TransactionCommandParam param : params) {
			credit(param);
		}
	}

	/**
	 * 扣款<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 付款方账号：payerAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 账务类型编码：accountTradeTypeCode <br>
	 * 
	 * @param param
	 *            账户交易命令参数
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void debit(TransactionCommandParam param) throws AccountException {
		CheckUtils.valueIsNull(param, CheckUtils.COMMON_FIELD
				+ "payerAccountNo,amount,tradeType");
		logger.info("debit,payerAccountNo:{},amount:{},tradeType:{}", param.getPayerAccountNo(),
				param.getAmount(),param.getTradeType());
		trimAccountNo(param);
		validateAmountIsNegtive(param.getAmount());
		saveTransactionFlow(param.getPayerAccountNo(), param,param.getTradeType());
		doDebit(param);
	}

	/**
	 * 批量扣款<br>
	 * 必须赋值的属性和 debit 方法一样<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void batchDebit(List<TransactionCommandParam> params)
			throws AccountException {
		logger.info("batchDebit start");
		for (TransactionCommandParam param : params) {
			this.debit(param);
		}
	}

	/**
	 * 解冻并扣款<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 付款方账号：payerAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 账务类型编码：accountTradeTypeCode <br>
	 * 授权码：credential ：String <br>
	 * 
	 * @param param
	 *            账户交易命令参数
	 * @param credential
	 *            授权码
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void debitUnfrozen(TransactionCommandParam param, String credential)
			throws AccountException {
		CheckUtils.valueIsNull(new Object[] { param, credential },
				new String[] {CheckUtils.COMMON_FIELD+ "payerAccountNo,amount,tradeType","credential" });
		logger.info("debitUnfrozen ,payerAccountNo:{},amount:{},tradeType:{}, credential:{}",
				param.getPayerAccountNo(),param.getAmount(),param.getTradeType(),credential);
		trimAccountNo(param);
		validateAmountIsNegtive(param.getAmount());
		saveTransactionFlow(param.getPayerAccountNo(), param,
				param.getTradeType());
		fundManagementService.unfreezeFund(param, param.getPayerAccountNo(),
				param.getAmount(), credential);
		this.doDebit(param);
	}

	/**
	 * 批量冻结并扣款<br>
	 * 必须赋值的属性和 debitUnfrozen 方法一样<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表
	 * @param credential
	 *            授权码
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void batchDebitUnfrozen(List<TransactionCommandParam> params,
			String credential) throws AccountException {
		 logger.info("batchDebitUnfrozen start ");
		for (TransactionCommandParam param : params) {
			this.debitUnfrozen(param, credential);
		}
	}
	
	/**
	 * 存入并冻结资金
	 */
	@Override
	public String creditAndFreezeFund(TransactionCommandParam param,BigDecimal freezeAmount)
			throws AccountException {
		CheckUtils.valueIsNull(param, CheckUtils.COMMON_FIELD
				+ "receiverAccountNo,amount,tradeType");
		CheckUtils.notNull(freezeAmount, "freezeAmount");
		logger.info("creditAndFreezeFund,receiverAccountNo:{},amount:{},tradeType:{}", param.getReceiverAccountNo(),
				param.getAmount(),param.getTradeType());
		
		//存入
		validateAmountIsNegtive(param.getAmount());
		validateFreezeAmountIsNegtive(param.getAmount(),freezeAmount);
		saveTransactionFlow(param.getReceiverAccountNo(), param,param.getTradeType());
		doCredit(param);
		
		//冻结资金
		return fundManagementService.freezeFund(new ContextParam(param.getFlowID(),param.getInitiator()),
				param.getReceiverAccountNo(), MathUtils.round(freezeAmount), null);
	}

	/**
	 * 转账<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 付款方账号：payerAccountNo ：String <br>
	 * 收款方账号：receiverAccountNo ：String <br>
	 * 发生额：amount ：BigDecimal <br>
	 * 账务类型编码：accountTradeTypeCode <br>
	 * 
	 * @param param
	 *            账户交易命令参数
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void transfer(TransactionCommandParam param) throws AccountException {
		CheckUtils.valueIsNull(param, CheckUtils.COMMON_FIELD
				+ "payerAccountNo,receiverAccountNo,amount,tradeType");
		logger.info("transfer,payerAccountNo:{},receiverAccountNo:{},amount:{},tradeType:{}", 
				param.getPayerAccountNo(),param.getReceiverAccountNo(),param.getAmount(),param.getTradeType());
		trimAccountNo(param);
		validateAmountIsNegtive(param.getAmount());
		this.saveTransactionFlow("", param, param.getTradeType());
		this.doCredit(param,"TRANSFER_RECEIVER");
		this.doDebit(param);
	}

	/**
	 * 批量转账<br>
	 * 必须赋值的属性和 transfer 方法一样<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表
	 * @throws AccountException
	 *             账户异常
	 * @param params
	 */
	@Override
	@RetryingTransaction
	public void batchTransfer(List<TransactionCommandParam> params)
			throws AccountException {
		logger.info("batchTransfer start");
		for (TransactionCommandParam param : params) {
			this.transfer(param);
		}
	}

	/**
	 * 调账(根据资金方向设置调出方账号和调入方账号)<br>
	 * 参数param 必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 调出方账号：payerAccountNo ：String <br>
	 * 调入方账号：receiverAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 账务类型编码：accountTradeTypeCode <br>
	 * 资金方向：direction ：FundChangeDirectionEnum(账户api定义的枚举类型)<br>
	 * 
	 * @param param
	 *            账户交易命令参数
	 * @throws AccountException
	 *             账户异常
	 * 
	 */
	@Override
	@RetryingTransaction
	public void adjustBalance(TransactionCommandParam param)
			throws AccountException {
		CheckUtils.valueIsNull(param, CheckUtils.COMMON_FIELD
				+ "amount,tradeType,direction");
		logger.info("adjustBalance,amount:{},tradeType:{},direction:{}", param.getAmount(),param.getTradeType(),param.getDirection());
		trimAccountNo(param);
		validateAmountIsNegtive(param.getAmount());
		this.saveTransactionFlow(param.getPayerAccountNo(), param,
				param.getTradeType());
		accountTransactionService.adjusting(param);
	}

	/**
	 * 此接口比较特殊，支持不同账户操作类型的交易指令， 比如一笔交易有三种账户操作：付款方支出，收款方存入，手续费记账。<br>
	 * 参数params列表中的TransactionCommandParam必须赋值的属性：<br>
	 * 流水号：flowID: String <br>
	 * 发起方：initiator: String <br>
	 * 付款方账号：payerAccountNo ：String<br>
	 * 收款方账号：receiverAccountNo ：String<br>
	 * 发生额：amount ：BigDecimal<br>
	 * 账务类型编码：accountTradeTypeCode <br>
	 * 资金方向：direction ：AccountTradeTypeEnum(账户api定义的枚举类型)<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	@RetryingTransaction
	public void batchProcessTransaction(List<TransactionCommandParam> params)
			throws AccountException {
		for (TransactionCommandParam param : params) {
			CheckUtils.valueIsNull(param, "Direction");
			if (FundChangeDirectionEnum.INCREMENT.equals(param.getDirection()))
				this.credit(param);
			else
				this.debit(param);
		}
	}

	/**
	 * 对于同一账户，进行批量存入<br>
	 * 必须赋值的属性和 credit 方法一样,流水号必须相同<br>
	 * 
	 * @param params
	 *            账户交易命令参数列表
	 * @throws AccountException
	 *             账户异常
	 */
	@Override
	public void batchCreditForSameAccount(List<TransactionCommandParam> params)
			throws AccountException {
		if (params != null && params.size() > 0) {
			// 检查并判断账户交易命令参数列表元素，是否符合条件
			TransactionCommandParam param = null;
			String receiverAccountNo = null;
			for (TransactionCommandParam command : params) {
				CheckUtils.valueIsNull(command, CheckUtils.COMMON_FIELD
						+ "ReceiverAccountNo,Amount,TradeType");
				if(param == null){
					param = command;
					receiverAccountNo = command.getReceiverAccountNo();
				}
				if (!command.getReceiverAccountNo().equals(receiverAccountNo))
					return;
				trimAccountNo(command);
				validateAmountIsNegtive(command.getAmount());
			}
			this.saveTransactionFlow(receiverAccountNo, param,
					param.getTradeType());
			
			accountTransactionService.batchCreditForSameAccount(params);
		}
	}

	/**
	 * 进行存入操作
	 * @param param
	 * @throws AccountException
	 */
	private void doCredit(TransactionCommandParam param)
			throws AccountException {
		CheckUtils.valueIsNull(param, "receiverAccountNo");
		accountTransactionService.credit(param);
	}
	
	/**
	 * 进行存入操作
	 * @param param
	 * @param tradeType
	 * 		变更额度限制的交易类型
	 * @throws AccountException
	 */
	private void doCredit(TransactionCommandParam param,String tradeType)
			throws AccountException {
		CheckUtils.valueIsNull(param, "receiverAccountNo");
		accountTransactionService.credit(param,tradeType);
	}
	
	/**
	 * 进行扣款操作
	 * @param param
	 * @throws AccountException
	 */
	private void doDebit(TransactionCommandParam param) throws AccountException {
		CheckUtils.valueIsNull(param, "payerAccountNo");
		accountTransactionService.debit(param);
	}
	
	/**
	 * 验证冻结金额是否为负
	 * @param freezeAmount
	 */
	private void validateFreezeAmountIsNegtive(BigDecimal amount,BigDecimal freezeAmount){
		if(freezeAmount.compareTo(amount) > 0)
			throw AccountException.INVALID_BALANCE.newInstance("冻结金额不能大于存入金额", new Object[]{});
		if(freezeAmount.compareTo(BigDecimal.ZERO) <= 0)
			throw AccountException.INVALID_BALANCE.newInstance("冻结金额必须大于0", new Object[]{});
	}

	private void trimAccountNo(TransactionCommandParam param) {
		param.setPayerAccountNo(StringUtils.trim(param.getPayerAccountNo()));
		param.setReceiverAccountNo(StringUtils.trim(param
				.getReceiverAccountNo()));
	}

	public void setFundManagementService(FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	public void setAccountTransactionService(
			AccountTransactionService accountTransactionService) {
		this.accountTransactionService = accountTransactionService;
	}

}
