delete from TBL_ACCOUNT_PROVIDER;

delete from TBL_ACCOUNT_SERIAL;

delete from TBL_QUOTA_LIMIT_CHANGE_RULE;


--初始化账户提供方
insert into TBL_ACCOUNT_PROVIDER(ID,CUSTOMER_ID,PROVIDER_CODE,PROVIDER_NAME,DESCRIPTION) values(1,'1','ESINOTRANS','esinotransAccount','资金帐户');

--初始化账号
insert into TBL_ACCOUNT_SERIAL( ID, FLOW_ID, PREFIX_NUM, ACCOUNT_SYS_TYPE) values(1,1, '1000','ESINOTRANS');


--初始化额度限制规则
--转账、收款和打款退回业务增加可提现额度
insert into TBL_QUOTA_LIMIT_CHANGE_RULE(ID, DIRECTION, QUOTA_TYPE, TRADE_TYPE)
values(-1, 'INCREMENT','WITHDRAW_QUOTA','TRANSFER_RECEIVER');
insert into TBL_QUOTA_LIMIT_CHANGE_RULE(ID, DIRECTION, QUOTA_TYPE, TRADE_TYPE)
values(-2, 'INCREMENT','WITHDRAW_QUOTA','REMIT_BACK');
insert into TBL_QUOTA_LIMIT_CHANGE_RULE(ID, DIRECTION, QUOTA_TYPE, TRADE_TYPE)
values(-3, 'INCREMENT','WITHDRAW_QUOTA','INCOMING');

--提现业务减少可提现额度
insert into TBL_QUOTA_LIMIT_CHANGE_RULE(ID, DIRECTION, QUOTA_TYPE, TRADE_TYPE)
values(-4, 'DECREASE','WITHDRAW_QUOTA','WITHDRAW');

--充值业务增加可充退额度
insert into TBL_QUOTA_LIMIT_CHANGE_RULE(ID, DIRECTION, QUOTA_TYPE, TRADE_TYPE)
values(-5, 'INCREMENT','RECHARGE_REFUND','RECHARGE');
insert into TBL_QUOTA_LIMIT_CHANGE_RULE(ID, DIRECTION, QUOTA_TYPE, TRADE_TYPE)
values(-6, 'INCREMENT','RECHARGE_REFUND','REFUND_BACK');

--充退业务减少可充退额度
insert into TBL_QUOTA_LIMIT_CHANGE_RULE(ID, DIRECTION, QUOTA_TYPE, TRADE_TYPE)
values(-7, 'DECREASE','RECHARGE_REFUND','RECHARGE_REFUND');



--commit;