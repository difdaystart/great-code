delete from TBL_ACCOUNT_PROVIDER;

delete from TBL_ACCOUNT_SERIAL;

delete from TBL_QUOTA_LIMIT_CHANGE_RULE;


--初始化账户提供方
insert into TBL_ACCOUNT_PROVIDER(ID,CUSTOMER_ID,PROVIDER_CODE,PROVIDER_NAME,DESCRIPTION) values(1,'1','ESINOTRANS','esinotransAccount','资金帐户');

--初始化账号
insert into TBL_ACCOUNT_SERIAL( ID, FLOW_ID, PREFIX_NUM, ACCOUNT_SYS_TYPE) values(1,1, '1000','ESINOTRANS');


--初始化额度限制规则
--付款账户
--提现业务减少可提现额度
--insert into TBL_QUOTA_LIMIT_CHANGE_RULE(trade_type, direction, quota_type, account_type_id) values(22,2,1,1);

--转入业务增加可提现额度 
--insert into TBL_QUOTA_LIMIT_CHANGE_RULE(trade_type, direction, quota_type, account_type_id) values(11,1,1,1);
--转出业务减少可提现额度
--insert into TBL_QUOTA_LIMIT_CHANGE_RULE(trade_type, direction, quota_type, account_type_id) values(12,2,1,1);


--收款账户
--转入业务增加可提现额度
--insert into TBL_QUOTA_LIMIT_CHANGE_RULE(trade_type, direction, quota_type, account_type_id) values(11,1,1,2);
--转出业务减少可提现额度
--insert into TBL_QUOTA_LIMIT_CHANGE_RULE(trade_type, direction, quota_type, account_type_id) values(12,2,1,2);





