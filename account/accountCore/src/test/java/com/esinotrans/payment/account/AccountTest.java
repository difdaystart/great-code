package com.esinotrans.payment.account;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.esinotrans.payment.account.dto.ContextParam;
import com.esinotrans.payment.account.dto.CreateAccountParam;
import com.esinotrans.payment.account.dto.TransactionCommandParam;
import com.esinotrans.payment.account.facade.AccountManagementFacade;
import com.esinotrans.payment.account.facade.AccountTransactionFacade;

public class AccountTest {

	private static ClassPathXmlApplicationContext appContext;
	private AccountManagementFacade accountManagementBiz;
	private AccountTransactionFacade accountTransactionBiz;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		appContext = new ClassPathXmlApplicationContext("classpath:applicationContext-account.xml");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		accountManagementBiz = (AccountManagementFacade) appContext.getBean("accountManagementBiz");
		accountTransactionBiz = (AccountTransactionFacade) appContext.getBean("accountTransactionBiz");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateAccount() {
		CreateAccountParam createAccountParam = new CreateAccountParam();
		
		createAccountParam.setAccountProviderCode("ESINOTRANS");
		createAccountParam.setAccountType("PAY_ACCOUNT");
		createAccountParam.setBalance(new BigDecimal("10000.00"));
		createAccountParam.setCustomerNo("test0000000003");
		
		ContextParam contextParam = new ContextParam();
		
		contextParam.setDescription("测试创建账户");
		contextParam.setFlowID("test0000000003001");
		contextParam.setInitiator("TESTCASE");
		
		String accountNo = accountManagementBiz.createAccount(contextParam, createAccountParam);
		
		System.out.println("accountNo: " + accountNo);
		
		Assert.assertNotNull(accountNo);
	}
	
	@Test
	public void testRemoveAccount(){
		ContextParam contextParam = new ContextParam();
		
		contextParam.setDescription("测试删除账户");
		contextParam.setFlowID("test0000000003002");
		contextParam.setInitiator("TESTCASE");
		accountManagementBiz.removeAccount(contextParam, "10000000000003");
	}
	
	@Test
	public void testTransfer(){
		TransactionCommandParam transactionCommandParam = new TransactionCommandParam();
		transactionCommandParam.setFlowID("transfertest0000001");
		transactionCommandParam.setAmount(new BigDecimal("10000.00"));
		transactionCommandParam.setInitiator("TESTCASE");
		transactionCommandParam.setPayerAccountNo("10000000000003");
		transactionCommandParam.setReceiverAccountNo("10000000000001");
		transactionCommandParam.setTradeType("转账");
		
		accountTransactionBiz.transfer(transactionCommandParam );
		
		Assert.assertTrue(true);
	}
	
	@Test
	@Ignore
	public void testGetByid(){
		
	}
}
