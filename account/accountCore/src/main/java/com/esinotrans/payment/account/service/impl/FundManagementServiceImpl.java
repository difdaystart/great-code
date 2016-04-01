/**
 * @Copyright Copyright (c)2013
 * @company esinotrans
 */
package com.esinotrans.payment.account.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.esinotrans.payment.account.dao.AccountDao;
import com.esinotrans.payment.account.dao.AccountManagementRecordDao;
import com.esinotrans.payment.account.dao.FundFrozenUnFrozenHistoryDao;
import com.esinotrans.payment.account.dao.FundFrozenUnFrozenRecordDao;
import com.esinotrans.payment.account.dao.SelfCheckFailureDetailDao;
import com.esinotrans.payment.account.dao.SelfCheckTaskDao;
import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.entity.Account;
import com.esinotrans.payment.account.entity.AccountHistory;
import com.esinotrans.payment.account.entity.AccountManagementRecord;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenHistory;
import com.esinotrans.payment.account.entity.FundFrozenUnFrozenRecord;
import com.esinotrans.payment.account.entity.SelfCheckFailureDetail;
import com.esinotrans.payment.account.entity.SelfCheckTask;
import com.esinotrans.payment.account.enums.AccountOperationEnum;
import com.esinotrans.payment.account.enums.FrozenUnFrozenEnum;
import com.esinotrans.payment.account.enums.FundChangeDirectionEnum;
import com.esinotrans.payment.account.enums.SelfCheckTaskStatusEnum;
import com.esinotrans.payment.account.exception.AccountException;
import com.esinotrans.payment.account.service.AccountManagementService;
import com.esinotrans.payment.account.service.FundManagementService;
import com.esinotranse.payment.utils.CommonUtils;
import com.esinotranse.payment.utils.DateUtils;
import com.esinotranse.payment.utils.json.JSONUtils;

/**
 * 资金管理service  实现类
 * @company YeePay
 * @author 王伟
 * @since 2010-12-27
 * @version 1.0
 */
public class FundManagementServiceImpl implements
		FundManagementService {

	private AccountDao accountDao;				//账户实体DAO

	private FundFrozenUnFrozenRecordDao fundFrozenUnFrozenRecordDao; // 资金冻结解冻记录Dao

	private FundFrozenUnFrozenHistoryDao fundFrozenUnFrozenHistoryDao; // 资金冻结解冻历史Dao

	private AccountManagementRecordDao accountManagementRecordDao; // 账户管理记录DAO

	private AccountManagementService accountManagementService;			//账户管理服务
	
	private JdbcTemplate jdbcTemplate;									//JDBC 模板
	
	private SelfCheckTaskDao selfCheckTaskDao;							//自检任务记录 DAO
	
	private SelfCheckFailureDetailDao selfCheckFailureDetailDao;		//自检失败明细表DAO

	/**
	 * 冻结资金
	 */
	@Override
	public String freezeFund(ContextParam contextParam, String accountNo,
			BigDecimal amount, Date unfrozenDate) throws AccountException {
		Account account = queryAccountByAccountNo(accountNo);
		return doFreezeFund(contextParam,account,amount,unfrozenDate);
	}
	
	@Override
	public void setThawDate(ContextParam contextParam, String accountNo,
			Date unfrozenDate, String credential) throws AccountException {
		validateFundFrozenUnFrozenRecordExist(accountNo, credential);
		Map<String, Object> params = updateAutoUnfrozenDate(accountNo,
				unfrozenDate, credential);
		Account account = queryAccountByAccountNo(accountNo);
		saveAccountManagementHistory(account, params, contextParam);
	}

	/**
	 * 解冻指定金额的资金
	 */
	@Override
	public void unfreezeFund(ContextParam contextParam, String accountNo,
			BigDecimal amount, String credential) throws AccountException {
		FundFrozenUnFrozenRecord record = validateFundFrozenUnFrozenRecordExist(accountNo, credential);
		validateWhetherCanUnfreezeFund(amount, record);
		modifyUnFrozenFund(amount, record);
		Account account = queryAccountByAccountNo(accountNo);
		account.decreaseFrozenQuota(amount);
		modifyFrozenQuotaLimit(contextParam, account, amount,
				AccountOperationEnum.UNFREEZE_FUND);
		saveFundFrozenUnFrozenHistory(accountNo, record.getId(), amount,
				FrozenUnFrozenEnum.UNFROZEN);
	}
	
	@Override
	public List<FundFrozenUnFrozenRecord> queryAllAutoUnfrozenFundRecords(
			Date autoUnfrozenDate) {
		Date endDate = DateUtils.getDayEnd(autoUnfrozenDate);
		return fundFrozenUnFrozenRecordDao
				.queryAllAutoUnfrozenFundRecords(endDate);
	}

	/**
	 * 定时解冻资金
	 */
	@Override
	public void unfreezeFundByTimer(Date currentDate,
			FundFrozenUnFrozenRecord record) {
		Account account = queryAccountByAccountNo(record.getAccountNo());
		BigDecimal unfrozenAmount = record.caculateAvailableUnfrozenAmount();
		if(unfrozenAmount.compareTo(BigDecimal.ZERO) <= 0)
			return;
		this.modifyUnFrozenFund(unfrozenAmount, record);
		account.decreaseFrozenQuota(unfrozenAmount);
		this.modifyFrozenQuotaLimit(new ContextParam(CommonUtils.getUUID(), "Timer"), account,
				unfrozenAmount, AccountOperationEnum.UNFREEZE_FUND_BY_TIMER);
		this.saveFundFrozenUnFrozenHistory(account.getAccountNo(), record
				.getId(), unfrozenAmount, FrozenUnFrozenEnum.UNFROZEN);
	}
	
	/**
	 *  资金自检 FOR ALL 账号
	 */
	@Override
	public void accountFundSelfCheck(Date startDate, Date endDate) {
		
		long startTime = System.currentTimeMillis();
		System.out.println("accountFundSelfCheck ..... start");
		
		SelfCheckTask task = createSelfCheckTask(startDate, endDate, "ALL");
		//查询sql
		String sql = "SELECT a.ACCOUNT_NO,a.DIRECTION,a.AMOUNT,a.BALANCE,a.SERIAL_NUM FROM TBL_ACCOUNT_HISTORY AS a"+
					" WHERE a.CREATE_DATE >= ? and a.CREATE_DATE <= ? order by ACCOUNT_NO";
		final List<SelfCheckFailureDetail> failureDetails = new CopyOnWriteArrayList<SelfCheckFailureDetail>();
		final Queue<AccountHistory> cacheQueue = new ConcurrentLinkedQueue<AccountHistory>();
		final AtomicLong count = new AtomicLong();
		jdbcTemplate.query(sql,new Object[]{startDate,endDate},new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				count.incrementAndGet();
				doProcessRow(rs,failureDetails,cacheQueue);	//处理每行记录
			}
		});
		
		//保存资金任务记录
		task.setFailureNum(failureDetails.size());
		task.setStatus(SelfCheckTaskStatusEnum.PROCESSED);
		selfCheckTaskDao.add(task);
		
		//保存自检失败明细表
		for(SelfCheckFailureDetail detail : failureDetails){
			detail.setTaskNo(task.getTaskNo());			//设置自检任务号
		}
		selfCheckFailureDetailDao.saveSelfCheckFailureDetails(failureDetails);
		
		long endTime = System.currentTimeMillis();
		System.out.println("accountFundSelfCheck run time ="+(endTime - startTime)+"ms, time ="+(endTime - startTime)/1000+" ,count = "+count.get());
	}
	
	/**
	 * 资金资金 FOR 指定的帐号
	 */
	@Override
	public void accountFundSelfCheck(String[] accountNos, Date startDate,
			Date endDate) {
		long startTime = System.currentTimeMillis();
		System.out.println("accountFundSelfCheck accountNOS ..... start");
		
		//创建资金任务记录
		SelfCheckTask task = createSelfCheckTask(startDate, endDate, "PART");
		
		//查询sql
		String sql = "SELECT a.ACCOUNT_NO,a.DIRECTION,a.AMOUNT,a.BALANCE,a.SERIAL_NUM FROM TBL_ACCOUNT_HISTORY AS a"+
					" WHERE a.ACCOUNT_NO = ? and a.CREATE_DATE >= ? and a.CREATE_DATE <= ? order by ACCOUNT_NO";
		final List<SelfCheckFailureDetail> failureDetails = new CopyOnWriteArrayList<SelfCheckFailureDetail>();
		final Queue<AccountHistory> cacheQueue = new ConcurrentLinkedQueue<AccountHistory>();
		final AtomicLong count = new AtomicLong();
		for(String accountNo : accountNos){
			jdbcTemplate.query(sql,new Object[]{accountNo,startDate,endDate},new RowCallbackHandler(){
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					count.incrementAndGet();
					doProcessRow(rs,failureDetails,cacheQueue);	//处理每行记录
				}
			});
		}
		
		//保存资金任务记录
		task.setFailureNum(failureDetails.size());
		task.setStatus(SelfCheckTaskStatusEnum.PROCESSED);
		selfCheckTaskDao.add(task);
		
		//保存自检失败明细表
		for(SelfCheckFailureDetail detail : failureDetails){
			detail.setTaskNo(task.getTaskNo());			//设置自检任务号
		}
		selfCheckFailureDetailDao.saveSelfCheckFailureDetails(failureDetails);
		
		long endTime = System.currentTimeMillis();
		System.out.println("accountFundSelfCheck run time ="+(endTime - startTime)+"ms, time ="+(endTime - startTime)/1000+" ,count = "+count.get());
	}
	
	/**
	 * 创建自检任务记录
	 * @param startDate
	 * @param endDate
	 * @param checkType
	 * @return
	 */
	private SelfCheckTask createSelfCheckTask(Date startDate, Date endDate,String checkType){
		SelfCheckTask task = new SelfCheckTask();
		String taskNo = CommonUtils.getUUID().substring(0,16);
		task.setTaskNo(taskNo);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		task.setCheckType(checkType);
		task.setStatus(SelfCheckTaskStatusEnum.PROCESSING);
		task.setCreateDate(DateUtils.getDayStart(new Date()));
		return task;
	}
	
	/**
	 * 帐户自检 处理每行记录
	 * @param rs
	 * @param failureDetails
	 * @param cacheQueue
	 */
	private void doProcessRow(ResultSet rs,final List<SelfCheckFailureDetail> failureDetails,
			final Queue<AccountHistory> cacheQueue){
		if(rs == null)
			return;
		AccountHistory history = populateAccountHistory(rs);
		AccountHistory eveHistory = cacheQueue.poll();
		if(eveHistory == null){
			cacheQueue.add(history);
			return;
		}
		if(eveHistory.getAccountNo().equals(history.getAccountNo())){
			doSelfCheck(eveHistory,history,failureDetails);
		}
		cacheQueue.add(history);
		eveHistory = null;
	}
	
	/**
	 * 填充帐户历史
	 * @param rs
	 * @return
	 */
	private AccountHistory populateAccountHistory(ResultSet rs){
		AccountHistory history = new AccountHistory();
		try {
			history.setAccountNo(rs.getString("ACCOUNT_NO"));
			history.setDirection(rs.getString("DIRECTION") == null?
					null:FundChangeDirectionEnum.valueOf(rs.getString("DIRECTION")));
			history.setAmount(rs.getBigDecimal("AMOUNT"));
			history.setBalance(rs.getBigDecimal("BALANCE"));
			history.setSerialNum(rs.getLong("SERIAL_NUM"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return history;
	}
	
	/**
	 * 资金资金
	 * @param eveHis
	 * @param targetHis
	 * @param failureDetials
	 */
	private void doSelfCheck(AccountHistory eveHis,AccountHistory targetHis,final List<SelfCheckFailureDetail> failureDetails){
		//资金序号是否连续
		if(!(eveHis.getSerialNum() +1 == targetHis.getSerialNum())){
			failureDetails.add(createSelfCheckFailureDetail(eveHis,targetHis,"资金序号不连续"));
			return;
		}
		
		if(targetHis.getDirection() == FundChangeDirectionEnum.INCREMENT){
			if(eveHis.getBalance().add(targetHis.getAmount()).compareTo(targetHis.getBalance()) != 0){
				failureDetails.add(createSelfCheckFailureDetail(eveHis,targetHis,"资金的变动不连续"));
				return;
			}
		}
		
		if(targetHis.getDirection() == FundChangeDirectionEnum.DECREASE){
			if(eveHis.getBalance().subtract(targetHis.getAmount()).compareTo(targetHis.getBalance()) != 0){
				failureDetails.add(createSelfCheckFailureDetail(eveHis,targetHis,"资金的变动不连续"));
				return;
			}
		}
	}
	
	/**
	 * 创建自检失败明细表
	 * @param eveHis
	 * @param targetHis
	 * @param describe
	 * @return
	 */
	private SelfCheckFailureDetail createSelfCheckFailureDetail(AccountHistory eveHis,
			AccountHistory targetHis,String describe){
		SelfCheckFailureDetail detail = new SelfCheckFailureDetail();
		detail.setAccountNo(targetHis.getAccountNo());
		detail.setEveAccountHistoryDesc(JSONUtils.toJsonString(eveHis));
		detail.setAccountHistoryDesc(JSONUtils.toJsonString(targetHis));
		detail.setDescribe(describe);
		return detail;
	}
	
	/**
	 * 验证冻结解冻记录是否存在
	 * 
	 * @param accountNo
	 * @param credential
	 * @return
	 * @throws AccountException
	 */
	private FundFrozenUnFrozenRecord validateFundFrozenUnFrozenRecordExist(
			String accountNo, String credential) throws AccountException {
		FundFrozenUnFrozenRecord record = fundFrozenUnFrozenRecordDao
				.queryFundFrozenUnFrozenRecordByCredential(credential);
		if (record == null)
			throw AccountException.FUNDFROZENRECORD_IS_NULL.newInstance(
					"账号为{0}授权码为{1}的资金冻结记录不存在", new Object[] { accountNo,
							credential });
		return record;
	}

	/**
	 * 验证是否能够解冻资金
	 * 
	 * @param amount
	 * @param record
	 * @throws AccountException
	 */
	private void validateWhetherCanUnfreezeFund(BigDecimal amount,
			FundFrozenUnFrozenRecord record) throws AccountException {
		if(!record.canUnfrozenFund(amount))
			throw AccountException.ILLEGAL_ARGUMNET.newInstance(
					"解冻资金累计不能大于冻结资金", new Object[] { "" });
	}

	/**
	 * 更新解冻金额
	 * 
	 * @param amount
	 * @param record
	 */
	private void modifyUnFrozenFund(BigDecimal amount,
			FundFrozenUnFrozenRecord record) {
		// 修改资金冻结解冻记录
		record.increaseUnfrozenAmount(amount);
		record.setLastModifyDate(new Date());
		record.roundFrozenUnFrozenAmount();
		fundFrozenUnFrozenRecordDao.updateFundFrozenUnFrozenRecord(record);
	}

	/**
	 * 更新自动解冻时间
	 * 
	 * @param accountNo
	 * @param unfrozenDate
	 * @param credential
	 */
	private Map<String, Object> updateAutoUnfrozenDate(String accountNo,
			Date unfrozenDate, String credential) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("autoUnfrozenDate", unfrozenDate);
		params.put("credential", credential);
		params.put("accountNo", accountNo);
		fundFrozenUnFrozenRecordDao.updateAutoUnfrozenDate(params);
		return params;
	}

	/**
	 * 验证账户是否存在
	 * 
	 * @param accountNo
	 * @return
	 * @throws AccountException
	 */
	private Account queryAccountByAccountNo(String accountNo)
			throws AccountException {
		Account account = accountDao.queryAccountByAccountNo(accountNo, true);
		if (account == null)
			throw AccountException.ACCOUNT_ISNOT_EXIST.newInstance(
					"账号{0}的账户不存在", new Object[] { accountNo });
		return account;
	}

	/**
	 * 验证有足够的余额能够冻结
	 * 
	 * @param account
	 * @param amount
	 * @throws AccountException
	 */
	private void validateBalanceIsEnoughToFrozen(Account account,
			BigDecimal amount) throws AccountException {
		BigDecimal availableAmount = account.getBalance().subtract(
				account.getFrozenQuota());
		if (availableAmount.compareTo(amount) < 0) {
			throw AccountException.AVAILABLE_BALANCE_ISNOT_ENOUGH.newInstance(
					"账号{0}可用余额不足", new Object[] { account.getAccountNo() });
		}
	}

	/**
	 * 修改额度限制
	 * 
	 * @param account
	 * @param amount
	 */
	private void modifyFrozenQuotaLimit(ContextParam contextParam,
			Account account, BigDecimal frozenAmount,
			AccountOperationEnum operationType) {
		accountManagementService.updateAccountFrozenQuota(contextParam, account, frozenAmount,
				operationType);
	}

	/**
	 * 保存资金冻结解冻记录
	 * 
	 * @param contextParam
	 * @param account
	 * @param amount
	 * @param unfrozenDate
	 */
	private FundFrozenUnFrozenRecord saveFundFrozenUnFrozenRecord(
			ContextParam contextParam, Account account, BigDecimal amount,
			Date unfrozenDate) {
		FundFrozenUnFrozenRecord record = FundFrozenUnFrozenRecord.newIntance(
				contextParam.getFlowID(), account.getAccountNo(), contextParam
						.getInitiator(), AccountOperationEnum.FREEZE_FUND,
				amount, UUID.randomUUID().toString(), account.getFrozenSerial(), unfrozenDate,
				new Date(), contextParam.getDescription(), Long.valueOf(0));
		fundFrozenUnFrozenRecordDao.saveFundFrozenUnFrozenRecord(record);
		return record;
	}

	/**
	 * 保存资金冻结解冻历史
	 * 
	 * @param accountNo
	 * @param recordId
	 * @param amount
	 */
	private void saveFundFrozenUnFrozenHistory(String accountNo, Long recordId,
			BigDecimal amount, FrozenUnFrozenEnum type) {
		FundFrozenUnFrozenHistory history = FundFrozenUnFrozenHistory
				.newInstance(accountNo, recordId, type, amount, new Date());
		this.fundFrozenUnFrozenHistoryDao
				.saveFundFrozenUnFrozenHistory(history);
	}

	/**
	 * 保存账户管理历史
	 * 
	 * @param account
	 * @param changedValueMap
	 * @param contextParam
	 */
	private void saveAccountManagementHistory(Account account,
			Map<String, Object> changedValueMap, ContextParam contextParam) {
		Long manageSerial = Long
				.valueOf(account.getManageSerial().longValue() + 1);
		String changedValue = JSONUtils.toJsonString(changedValueMap);
		AccountManagementRecord managementRecord = AccountManagementRecord
				.newInstance(contextParam.getFlowID(), contextParam
						.getInitiator(), account.getAccountNo(), manageSerial,
						AccountOperationEnum.SET_THAW_DATE, changedValue,
						new Date());
		this.accountManagementRecordDao
				.saveAccountManagementRecord(managementRecord);
	}
	
	/**
	 * 进行冻结资金操作
	 * @param contextParam
	 * @param account
	 * @param amount
	 * @param unfrozenDate
	 * @return
	 */
	private String doFreezeFund(ContextParam contextParam,Account account,
			BigDecimal amount, Date unfrozenDate){
		validateBalanceIsEnoughToFrozen(account, amount);
		account.increaseFrozonQuota(amount);
		modifyFrozenQuotaLimit(contextParam, account, amount,
				AccountOperationEnum.FREEZE_FUND);
		FundFrozenUnFrozenRecord record = saveFundFrozenUnFrozenRecord(
				contextParam, account, amount, unfrozenDate);
		saveFundFrozenUnFrozenHistory(account.getAccountNo(), record.getId(), amount,
				FrozenUnFrozenEnum.FROZEN);
		return record.getCredential();
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setFundFrozenUnFrozenRecordDao(
			FundFrozenUnFrozenRecordDao fundFrozenUnFrozenRecordDao) {
		this.fundFrozenUnFrozenRecordDao = fundFrozenUnFrozenRecordDao;
	}

	public void setFundFrozenUnFrozenHistoryDao(
			FundFrozenUnFrozenHistoryDao fundFrozenUnFrozenHistoryDao) {
		this.fundFrozenUnFrozenHistoryDao = fundFrozenUnFrozenHistoryDao;
	}

	public void setAccountManagementRecordDao(
			AccountManagementRecordDao accountManagementRecordDao) {
		this.accountManagementRecordDao = accountManagementRecordDao;
	}

	public void setAccountManagementService(
			AccountManagementService accountManagementService) {
		this.accountManagementService = accountManagementService;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setSelfCheckTaskDao(SelfCheckTaskDao selfCheckTaskDao) {
		this.selfCheckTaskDao = selfCheckTaskDao;
	}

	public void setSelfCheckFailureDetailDao(
			SelfCheckFailureDetailDao selfCheckFailureDetailDao) {
		this.selfCheckFailureDetailDao = selfCheckFailureDetailDao;
	}

}
