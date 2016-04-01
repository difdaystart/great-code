--==============================================================
-- DBMS name:      IBM DB2 UDB 8.x Common Server
-- Created on:     2011-12-6 11:16:10
--==============================================================


drop index IND_ACCNT_CUST_NO;

drop index IND_ACCNT_NO;

drop index IND_FRZN_ACCNT_NO;

drop index IND_HIS_ACCNT_NO;

drop index IND_FUND_ACCNT_NO;

drop index IND_FUND_CRED;

drop index INDEX_TRANS_FLOW;

drop table TBL_ACCOUNT;

drop table TBL_ACCOUNT_FROZEN_RECORD;

drop table TBL_ACCOUNT_HISTORY;

drop table TBL_ACCOUNT_MANAGE_HISTORY;

drop table TBL_ACCOUNT_PROVIDER;

drop table TBL_ACCOUNT_SERIAL;

drop table TBL_ACCOUNT_SNAPSHOT;

drop table TBL_ACCOUNT_SNAPSHOT_HISTORY;

drop table TBL_EVE_ACCOUNT_SNAPSHOT;

drop table TBL_FUND_FROZEN_HISTORY;

drop table TBL_FUND_FROZEN_RECORD;

drop table TBL_QUOTA_LIMIT_CHANGE_RULE;

drop table TBL_SELF_CHECK_FAILURE_DETAIL;

drop table TBL_SELF_CHECK_TASK;

drop table TBL_TRANSACTION_FLOW;

--==============================================================
-- Table: TBL_ACCOUNT
--==============================================================
create table TBL_ACCOUNT
(
   ID                   NUMBER(32)                 not null,
   ACCOUNT_NO           VARCHAR(32)            not null,
   ACCOUNT_TYPE         VARCHAR(32)            not null,
   CUSTOMER_NO          VARCHAR(32)            not null,
   STATUS               VARCHAR(32)            not null,
   STATUS_CODE          INT                    not null,
   BALANCE              NUMBER(18,2)          not null,
   BALANCE_SIGN         VARCHAR(50),
   CURRENCY_ENUM        VARCHAR(16),
   PROVIDER_ID          NUMBER(32),
   TRADE_PASSWORD       VARCHAR(32),
   WITHDRAW_AMOUNT      NUMBER(18,2)          not null,
   RECHARGE_REFUND_AMOUNT NUMBER(18,2),
   FREEZE_AMOUNT        NUMBER(18,2)          not null,
   ACC_HIS_SERIAL       NUMBER(32)                 not null,
   MANAGE_SERIAL        NUMBER(32)                 not null,
   FROZEN_SERIAL        NUMBER(32)                 not null,
   VERSION              NUMBER(32),
   SNAPSHOT_DATE        TIMESTAMP,
   CREATE_DATE			TIMESTAMP,
   REMARK       		VARCHAR(512)
);

alter table TBL_ACCOUNT
  add constraint PK_ACCOUNT_ID primary key (ID);

comment on table TBL_ACCOUNT is
'资金账户表';

comment on column TBL_ACCOUNT.ID is
'ID';

comment on column TBL_ACCOUNT.ACCOUNT_NO is
'账户编号';

comment on column TBL_ACCOUNT.ACCOUNT_TYPE is
'账户类型';

comment on column TBL_ACCOUNT.CUSTOMER_NO is
'账户持有人';

comment on column TBL_ACCOUNT.STATUS is
'账户状态';

comment on column TBL_ACCOUNT.STATUS_CODE is
'账户状态码';

comment on column TBL_ACCOUNT.BALANCE is
'账户余额';

comment on column TBL_ACCOUNT.BALANCE_SIGN is
'账户余额签名';

comment on column TBL_ACCOUNT.CURRENCY_ENUM is
'币种';

comment on column TBL_ACCOUNT.PROVIDER_ID is
'账户提供方';

comment on column TBL_ACCOUNT.TRADE_PASSWORD is
'交易密码';

comment on column TBL_ACCOUNT.WITHDRAW_AMOUNT is
'可提现额度';

comment on column TBL_ACCOUNT.RECHARGE_REFUND_AMOUNT is
'可充退额度';

comment on column TBL_ACCOUNT.FREEZE_AMOUNT is
'冻结额度';

comment on column TBL_ACCOUNT.ACC_HIS_SERIAL is
'账户历史流水顺序号';

comment on column TBL_ACCOUNT.MANAGE_SERIAL is
'账户管理顺序号';

comment on column TBL_ACCOUNT.FROZEN_SERIAL is
'冻结解冻记录顺序号';

comment on column TBL_ACCOUNT.VERSION is
'版本号';

comment on column TBL_ACCOUNT.SNAPSHOT_DATE is
'最近快照日期';

--==============================================================
-- Index: IND_ACCNT_NO
--==============================================================
create unique index IND_ACCNT_NO on TBL_ACCOUNT (
   ACCOUNT_NO           ASC
);

--==============================================================
-- Index: IND_ACCNT_CUST_NO
--==============================================================
create index IND_ACCNT_CUST_NO on TBL_ACCOUNT (
   CUSTOMER_NO          ASC
);

--==============================================================
-- Table: TBL_ACCOUNT_FROZEN_RECORD
--==============================================================
create table TBL_ACCOUNT_FROZEN_RECORD
(
   ID                   NUMBER(32)             not null,
   TRADE_FLOW_ID        VARCHAR(60)            not null,
   ACCOUNT_NO           VARCHAR(32)            not null,
   INITIATOR            VARCHAR(16)            not null,
   OPERATE_TYPE         VARCHAR(32)            not null,
   CREDENTIAL           VARCHAR(36),
   UNFROZEN_DATE        TIMESTAMP,
   MODIFY_DATE          TIMESTAMP
);

alter table TBL_ACCOUNT_FROZEN_RECORD
  add constraint PK_ACCOUNT_FROZEN_RECORD_ID primary key (ID);

comment on table TBL_ACCOUNT_FROZEN_RECORD is
'账户冻结记录';

comment on column TBL_ACCOUNT_FROZEN_RECORD.ID is
'ID';

comment on column TBL_ACCOUNT_FROZEN_RECORD.TRADE_FLOW_ID is
'交易流水号';

comment on column TBL_ACCOUNT_FROZEN_RECORD.ACCOUNT_NO is
'账户编号';

comment on column TBL_ACCOUNT_FROZEN_RECORD.INITIATOR is
'发起方';

comment on column TBL_ACCOUNT_FROZEN_RECORD.OPERATE_TYPE is
'操作类型';

comment on column TBL_ACCOUNT_FROZEN_RECORD.CREDENTIAL is
'授权码';

comment on column TBL_ACCOUNT_FROZEN_RECORD.UNFROZEN_DATE is
'自动解冻时间';

comment on column TBL_ACCOUNT_FROZEN_RECORD.MODIFY_DATE is
'最后修改时间';

--==============================================================
-- Index: IND_FRZN_ACCNT_NO
--==============================================================
create index IND_FRZN_ACCNT_NO on TBL_ACCOUNT_FROZEN_RECORD (
   ACCOUNT_NO           ASC
);

--==============================================================
-- Table: TBL_ACCOUNT_HISTORY
--==============================================================
create table TBL_ACCOUNT_HISTORY
(
   ID                   NUMBER(32)                 not null,
   TRADE_FLOW_ID        VARCHAR(60)            not null,
   ACCOUNT_NO           VARCHAR(32),
   DIRECTION            VARCHAR(32)            not null,
   AMOUNT               NUMBER(18,2)          not null ,
   BALANCE              NUMBER(18,2),
   TRADE_TYPE           VARCHAR(32),
   SERIAL_NUM           NUMBER(32)                 not null,
   CREATE_DATE          TIMESTAMP,
   SUMMARY              VARCHAR(200),
   USER_REMARK          VARCHAR(200),
   SYSTEM_REMARK        VARCHAR(100)
);

alter table TBL_ACCOUNT_HISTORY
  add constraint PK_TBL_ACCOUNT_HISTORY_ID primary key (ID);


comment on table TBL_ACCOUNT_HISTORY is
'账户历史';

comment on column TBL_ACCOUNT_HISTORY.ID is
'ID';

comment on column TBL_ACCOUNT_HISTORY.TRADE_FLOW_ID is
'交易流水号';

comment on column TBL_ACCOUNT_HISTORY.ACCOUNT_NO is
'账号';

comment on column TBL_ACCOUNT_HISTORY.DIRECTION is
'资金方向';

comment on column TBL_ACCOUNT_HISTORY.AMOUNT is
'发生额';

comment on column TBL_ACCOUNT_HISTORY.BALANCE is
'当前余额';

comment on column TBL_ACCOUNT_HISTORY.TRADE_TYPE is
'交易类型';

comment on column TBL_ACCOUNT_HISTORY.SERIAL_NUM is
'顺序号';

comment on column TBL_ACCOUNT_HISTORY.CREATE_DATE is
'创建时间';

comment on column TBL_ACCOUNT_HISTORY.SUMMARY is
'摘要信息';

comment on column TBL_ACCOUNT_HISTORY.USER_REMARK is
'用户备注';

comment on column TBL_ACCOUNT_HISTORY.SYSTEM_REMARK is
'系统备注';

--==============================================================
-- Index: IND_HIS_ACCNT_NO
--==============================================================
create index IND_HIS_ACCNT_NO on TBL_ACCOUNT_HISTORY (
   ACCOUNT_NO           ASC
);

--==============================================================
-- Table: TBL_ACCOUNT_MANAGE_HISTORY
--==============================================================
create table TBL_ACCOUNT_MANAGE_HISTORY
(
   ID                   NUMBER(32),
   TRADE_FLOW_ID        VARCHAR(60),
   ACCOUNT_NO           VARCHAR(32),
   INITIATOR            VARCHAR(16),
   CREATE_DATE          TIMESTAMP,
   SERIAL_NUM           NUMBER(32)                 not null,
   OPERATE_TYPE         VARCHAR(32)            not null,
   CHANGED_VALUE        VARCHAR(500)
);

alter table TBL_ACCOUNT_MANAGE_HISTORY
  add constraint PK_TBL_ACCOUNT_MANAGE_HISTORY_ID primary key (ID);

comment on table TBL_ACCOUNT_MANAGE_HISTORY is
'账户管理历史';

comment on column TBL_ACCOUNT_MANAGE_HISTORY.ID is
'ID';

comment on column TBL_ACCOUNT_MANAGE_HISTORY.TRADE_FLOW_ID is
'交易流水号';

comment on column TBL_ACCOUNT_MANAGE_HISTORY.ACCOUNT_NO is
'账号';

comment on column TBL_ACCOUNT_MANAGE_HISTORY.INITIATOR is
'发起方';

comment on column TBL_ACCOUNT_MANAGE_HISTORY.CREATE_DATE is
'创建时间';

comment on column TBL_ACCOUNT_MANAGE_HISTORY.SERIAL_NUM is
'顺序号';

comment on column TBL_ACCOUNT_MANAGE_HISTORY.OPERATE_TYPE is
'操作类型';

comment on column TBL_ACCOUNT_MANAGE_HISTORY.CHANGED_VALUE is
'修改后的值';

--==============================================================
-- Table: TBL_ACCOUNT_PROVIDER
--==============================================================
create table TBL_ACCOUNT_PROVIDER
(
   ID                   NUMBER(32)                 not null,
   CUSTOMER_ID          VARCHAR(64)            not null,
   PROVIDER_CODE        VARCHAR(32)            not null,
   PROVIDER_NAME        VARCHAR(64)            not null,
   DESCRIPTION          VARCHAR(200)
);

alter table TBL_ACCOUNT_PROVIDER
  add constraint PK_TBL_ACCOUNT_PROVIDER_ID primary key (ID);

comment on table TBL_ACCOUNT_PROVIDER is
'账户提供方的含义是：账户系统为这个客户提供账户管理服务，而账户提供方使用账户系统的服务为他的客户创建账户并记账';

comment on column TBL_ACCOUNT_PROVIDER.ID is
'ID';

comment on column TBL_ACCOUNT_PROVIDER.CUSTOMER_ID is
'提供方客户ID';

comment on column TBL_ACCOUNT_PROVIDER.PROVIDER_CODE is
'提供方编码';

comment on column TBL_ACCOUNT_PROVIDER.PROVIDER_NAME is
'提供方名称';

comment on column TBL_ACCOUNT_PROVIDER.DESCRIPTION is
'描述';

--==============================================================
-- Table: TBL_ACCOUNT_SERIAL
--==============================================================
create table TBL_ACCOUNT_SERIAL
(
   ID                   NUMBER(32)                 not null,
   FLOW_ID              NUMBER(32)                 not null ,
   PREFIX_NUM           VARCHAR(4),
   ACCOUNT_SYS_TYPE     VARCHAR(16)            not null
);

alter table TBL_ACCOUNT_SERIAL
  add constraint PK_TBL_ACCOUNT_SERIAL_ID primary key (ID);

comment on table TBL_ACCOUNT_SERIAL is
'账号流水号';

comment on column TBL_ACCOUNT_SERIAL.ID is
'ID';

comment on column TBL_ACCOUNT_SERIAL.FLOW_ID is
'流水号';

comment on column TBL_ACCOUNT_SERIAL.PREFIX_NUM is
'账号前缀';

comment on column TBL_ACCOUNT_SERIAL.ACCOUNT_SYS_TYPE is
'账户系统类型';

--==============================================================
-- Table: TBL_ACCOUNT_SNAPSHOT
--==============================================================
create table TBL_ACCOUNT_SNAPSHOT
(
   ID                   NUMBER(32)                 not null,
   ACCOUNT_NO           VARCHAR(32),
   SNAP_DATE            TIMESTAMP,
   BALANCE              NUMBER(18,2)
);

comment on table TBL_ACCOUNT_SNAPSHOT is
'帐户快照表';

comment on column TBL_ACCOUNT_SNAPSHOT.ID is
'ID';

comment on column TBL_ACCOUNT_SNAPSHOT.ACCOUNT_NO is
'账号';

comment on column TBL_ACCOUNT_SNAPSHOT.SNAP_DATE is
'快照日期';

comment on column TBL_ACCOUNT_SNAPSHOT.BALANCE is
'余额';

--==============================================================
-- Table: TBL_ACCOUNT_SNAPSHOT_HISTORY
--==============================================================
create table TBL_ACCOUNT_SNAPSHOT_HISTORY
(
   ID                   NUMBER(32)                 not null,
   ACCOUNT_NO           VARCHAR(32),
   SNAP_DATE            TIMESTAMP,
   BALANCE              NUMBER(18,2)
);

alter table TBL_ACCOUNT_SNAPSHOT_HISTORY
  add constraint PK_SNAPSHOT_HISTORY_ID primary key (ID);

comment on table TBL_ACCOUNT_SNAPSHOT_HISTORY is
'帐户快照表历史表';

comment on column TBL_ACCOUNT_SNAPSHOT_HISTORY.ID is
'ID';

comment on column TBL_ACCOUNT_SNAPSHOT_HISTORY.ACCOUNT_NO is
'账号';

comment on column TBL_ACCOUNT_SNAPSHOT_HISTORY.SNAP_DATE is
'快照日期';

comment on column TBL_ACCOUNT_SNAPSHOT_HISTORY.BALANCE is
'余额';

--==============================================================
-- Table: TBL_EVE_ACCOUNT_SNAPSHOT
--==============================================================
create table TBL_EVE_ACCOUNT_SNAPSHOT
(
   ID                   NUMBER(32)                 not null,
   ACCOUNT_NO           VARCHAR(32),
   SNAP_DATE            TIMESTAMP,
   BALANCE              NUMBER(18,2),
   BALANCE_CHANGE_DATE  TIMESTAMP
);

alter table TBL_EVE_ACCOUNT_SNAPSHOT
  add constraint PK_SNAPSHOT_ID primary key (ID);

comment on table TBL_EVE_ACCOUNT_SNAPSHOT is
'上一日帐户快照表';

comment on column TBL_EVE_ACCOUNT_SNAPSHOT.ID is
'ID';

comment on column TBL_EVE_ACCOUNT_SNAPSHOT.ACCOUNT_NO is
'账号';

comment on column TBL_EVE_ACCOUNT_SNAPSHOT.SNAP_DATE is
'快照日期';

comment on column TBL_EVE_ACCOUNT_SNAPSHOT.BALANCE is
'余额';

comment on column TBL_EVE_ACCOUNT_SNAPSHOT.BALANCE_CHANGE_DATE is
'最近余额变动日期';

--==============================================================
-- Table: TBL_FUND_FROZEN_HISTORY
--==============================================================
create table TBL_FUND_FROZEN_HISTORY
(
   ID                   NUMBER(32)                 not null,
   ACCOUNT_NO           VARCHAR(32),
   OPERATE_TYPE         VARCHAR(32)            not null,
   AMOUNT               NUMBER(18,2)          not null,
   FUND_FROZEN_ID       NUMBER(32)                 not null,
   CREATE_DATE          TIMESTAMP
);

alter table TBL_FUND_FROZEN_HISTORY
  add constraint PK_FROZEN_HISTORY_ID primary key (ID);

comment on table TBL_FUND_FROZEN_HISTORY is
'资金冻结解冻历史';

comment on column TBL_FUND_FROZEN_HISTORY.ID is
'ID';

comment on column TBL_FUND_FROZEN_HISTORY.ACCOUNT_NO is
'账号';

comment on column TBL_FUND_FROZEN_HISTORY.OPERATE_TYPE is
'冻结/解冻操作';

comment on column TBL_FUND_FROZEN_HISTORY.AMOUNT is
'冻结或解冻的额度';

comment on column TBL_FUND_FROZEN_HISTORY.FUND_FROZEN_ID is
'资金冻结解冻记录ID';

comment on column TBL_FUND_FROZEN_HISTORY.CREATE_DATE is
'创建时间';

--==============================================================
-- Table: TBL_FUND_FROZEN_RECORD
--==============================================================
create table TBL_FUND_FROZEN_RECORD
(
   ID                   NUMBER(32)                 not null,
   TRADE_FLOW_ID        VARCHAR(60)            not null,
   ACCOUNT_NO           VARCHAR(32),
   INITIATOR            VARCHAR(16),
   CREATE_DATE          TIMESTAMP,
   OPERATE_TYPE         VARCHAR(32)            not null,
   FROZEN_AMOUNT        NUMBER(18,2)          not null ,
   UNFROZEN_AMOUNT      NUMBER(18,2)          ,
   CREDENTIAL           VARCHAR(40)            not null,
   MODIFY_DATE          TIMESTAMP              ,
   UNFROZEN_DATE        TIMESTAMP,
   SERIAL_NUM           NUMBER(32)                 not null,
   VERSION              NUMBER(32),
   DECRIPTION           VARCHAR(200)
);

alter table TBL_FUND_FROZEN_RECORD
  add constraint PK_FROZEN_RECORD_ID primary key (ID);

comment on table TBL_FUND_FROZEN_RECORD is
'资金冻结解冻记录';

comment on column TBL_FUND_FROZEN_RECORD.ID is
'ID';

comment on column TBL_FUND_FROZEN_RECORD.TRADE_FLOW_ID is
'交易流水号';

comment on column TBL_FUND_FROZEN_RECORD.ACCOUNT_NO is
'账号';

comment on column TBL_FUND_FROZEN_RECORD.INITIATOR is
'发起方';

comment on column TBL_FUND_FROZEN_RECORD.CREATE_DATE is
'创建时间';

comment on column TBL_FUND_FROZEN_RECORD.OPERATE_TYPE is
'操作类型';

comment on column TBL_FUND_FROZEN_RECORD.FROZEN_AMOUNT is
'冻结资金';

comment on column TBL_FUND_FROZEN_RECORD.UNFROZEN_AMOUNT is
'已解冻资金';

comment on column TBL_FUND_FROZEN_RECORD.CREDENTIAL is
'授权码';

comment on column TBL_FUND_FROZEN_RECORD.MODIFY_DATE is
'最后修改时间';

comment on column TBL_FUND_FROZEN_RECORD.UNFROZEN_DATE is
'自动解冻时间';

comment on column TBL_FUND_FROZEN_RECORD.SERIAL_NUM is
'顺序号';

comment on column TBL_FUND_FROZEN_RECORD.VERSION is
'版本号';

comment on column TBL_FUND_FROZEN_RECORD.DECRIPTION is
'备注信息';

--==============================================================
-- Index: IND_FUND_ACCNT_NO
--==============================================================
create index IND_FUND_ACCNT_NO on TBL_FUND_FROZEN_RECORD (
   ACCOUNT_NO           ASC
);

--==============================================================
-- Index: IND_FUND_CRED
--==============================================================
create unique index IND_FUND_CRED on TBL_FUND_FROZEN_RECORD (
   CREDENTIAL           ASC
);

--==============================================================
-- Table: TBL_QUOTA_LIMIT_CHANGE_RULE
--==============================================================
create table TBL_QUOTA_LIMIT_CHANGE_RULE
(
   ID                   NUMBER(32)                 not null,
   DIRECTION            VARCHAR(32)            not null,
   QUOTA_TYPE           VARCHAR(32)            not null,
   TRADE_TYPE           VARCHAR(32)            not null
);

alter table TBL_QUOTA_LIMIT_CHANGE_RULE
  add constraint PK_LIMIT_CHANGE_RULE_ID primary key (ID);

comment on table TBL_QUOTA_LIMIT_CHANGE_RULE is
'额度限制变更规则';

comment on column TBL_QUOTA_LIMIT_CHANGE_RULE.ID is
'ID';

comment on column TBL_QUOTA_LIMIT_CHANGE_RULE.DIRECTION is
'变更方向';

comment on column TBL_QUOTA_LIMIT_CHANGE_RULE.QUOTA_TYPE is
'额度类型';

comment on column TBL_QUOTA_LIMIT_CHANGE_RULE.TRADE_TYPE is
'交易类型';

--==============================================================
-- Table: TBL_SELF_CHECK_FAILURE_DETAIL
--==============================================================
create table TBL_SELF_CHECK_FAILURE_DETAIL
(
   ID                   NUMBER(32)                 not null,
   ACCOUNT_NO           VARCHAR(32),
   TASK_NO              VARCHAR(16),
   EVE_ACCOUNT_HIS_DESC VARCHAR(200),
   ACCOUNT_HIS_DESC     VARCHAR(200),
   DESCRIBE             VARCHAR(100)
);

alter table TBL_SELF_CHECK_FAILURE_DETAIL
  add constraint PK_CHECK_FAILURE_DETAIL_ID primary key (ID);

comment on table TBL_SELF_CHECK_FAILURE_DETAIL is
'自检明细表';

comment on column TBL_SELF_CHECK_FAILURE_DETAIL.ID is
'ID';

comment on column TBL_SELF_CHECK_FAILURE_DETAIL.ACCOUNT_NO is
'账号';

comment on column TBL_SELF_CHECK_FAILURE_DETAIL.TASK_NO is
'自检任务号';

comment on column TBL_SELF_CHECK_FAILURE_DETAIL.EVE_ACCOUNT_HIS_DESC is
'前一条帐户历史的摘要信息';

comment on column TBL_SELF_CHECK_FAILURE_DETAIL.ACCOUNT_HIS_DESC is
'帐户历史的摘要信息';

comment on column TBL_SELF_CHECK_FAILURE_DETAIL.DESCRIBE is
'描述信息';

--==============================================================
-- Table: TBL_SELF_CHECK_TASK
--==============================================================
create table TBL_SELF_CHECK_TASK
(
   ID                   NUMBER(32)                 not null,
   TASK_NO              VARCHAR(16),
   START_DATE           TIMESTAMP,
   END_DATE             TIMESTAMP,
   CHECK_TYPE           VARCHAR(16),
   STATUS               VARCHAR(16),
   FAILURE_NUM          INT,
   SUCCESS_ACCOUNT_NUM  NUMBER(32),
   CREATE_DATE          TIMESTAMP,
   REMARK               VARCHAR(200)
);

alter table TBL_SELF_CHECK_TASK
  add constraint PK_TBL_SELF_CHECK_TASK_ID primary key (ID);

comment on table TBL_SELF_CHECK_TASK is
'自检任务表';

comment on column TBL_SELF_CHECK_TASK.ID is
'ID';

comment on column TBL_SELF_CHECK_TASK.TASK_NO is
'任务号';

comment on column TBL_SELF_CHECK_TASK.START_DATE is
'自检范围起至时间';

comment on column TBL_SELF_CHECK_TASK.END_DATE is
'自检范围终止时间';

comment on column TBL_SELF_CHECK_TASK.CHECK_TYPE is
'自检类型';

comment on column TBL_SELF_CHECK_TASK.STATUS is
'任务状态';

comment on column TBL_SELF_CHECK_TASK.FAILURE_NUM is
'失败笔数';

comment on column TBL_SELF_CHECK_TASK.SUCCESS_ACCOUNT_NUM is
'成功帐户数';

comment on column TBL_SELF_CHECK_TASK.CREATE_DATE is
'创建时间';

comment on column TBL_SELF_CHECK_TASK.REMARK is
'备注信息';

--==============================================================
-- Table: TBL_TRANSACTION_FLOW
--==============================================================
create table TBL_TRANSACTION_FLOW
(
   ID                   NUMBER(32)                 not null,
   FLOW_ID              VARCHAR(60)            not null,
   ACCOUNT_NO           VARCHAR(32),
   INITIATOR            VARCHAR(16),
   TRADE_TYPE           VARCHAR(32)            not null,
   CREATE_DATE          TIMESTAMP,
   DESCRIPTION          VARCHAR(200)
);

alter table TBL_TRANSACTION_FLOW
  add constraint PK_TBL_TRANSACTION_FLOW_ID primary key (ID);

comment on table TBL_TRANSACTION_FLOW is
'交易流水';

comment on column TBL_TRANSACTION_FLOW.ID is
'ID';

comment on column TBL_TRANSACTION_FLOW.FLOW_ID is
'流水号';

comment on column TBL_TRANSACTION_FLOW.ACCOUNT_NO is
'账号';

comment on column TBL_TRANSACTION_FLOW.INITIATOR is
'发起方';

comment on column TBL_TRANSACTION_FLOW.TRADE_TYPE is
'交易类型';

comment on column TBL_TRANSACTION_FLOW.CREATE_DATE is
'创建时间';

comment on column TBL_TRANSACTION_FLOW.DESCRIPTION is
'描述';

--==============================================================
-- Index: INDEX_TRANS_FLOW
--==============================================================
create unique index INDEX_TRANS_FLOW on TBL_TRANSACTION_FLOW (
   FLOW_ID              ASC
);


--SEQ

-- 账户 
drop sequence SEQ_ACCOUNT_ACCOUNTID

create sequence SEQ_ACCOUNT_ACCOUNTID
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 账户管理
drop sequence SEQ_ACCOUNT_MANAGEMENT

create sequence SEQ_ACCOUNT_MANAGEMENT
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 账户快照
drop sequence SEQ_ACCOUNT_SNAPSHOT

create sequence SEQ_ACCOUNT_SNAPSHOT
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 交易流水
drop sequence SEQ_ACCOUNT_TRANSACTIONFLOW

create sequence SEQ_ACCOUNT_TRANSACTIONFLOW
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 账户自检任务
drop sequence SEQ_ACCOUNT_SELFCHECKTASK

create sequence SEQ_ACCOUNT_SELFCHECKTASK
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 自检失败细节
drop sequence SEQ_ACCOUNT_SELFAILDET

create sequence SEQ_ACCOUNT_SELFAILDET
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 交易限制变化规则
drop sequence SEQ_ACCOUNT_LIMITCHANGERULE

create sequence SEQ_ACCOUNT_LIMITCHANGERULE
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 资金冻结记录
drop sequence SEQ_ACCOUNT_FUNDFROZENRECORD

create sequence SEQ_ACCOUNT_FUNDFROZENRECORD
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 资金冻结历史
drop sequence SEQ_ACCOUNT_FUNDFROZENHISTORY

create sequence SEQ_ACCOUNT_FUNDFROZENHISTORY
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 账户交易类型
drop sequence SEQ_ACCOUNT_TRADETYPE

create sequence SEQ_ACCOUNT_TRADETYPE
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 账户流水号
drop sequence SEQ_ACCOUNT_SERIALNUM

create sequence SEQ_ACCOUNT_SERIALNUM
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 账户历史
drop sequence SEQ_ACCOUNT_ACCOUNTHISTORY

create sequence SEQ_ACCOUNT_ACCOUNTHISTORY
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;

-- 账户冻结记录
drop sequence SEQ_ACCOUNT_FROZENRECORD

create sequence SEQ_ACCOUNT_FROZENRECORD
minvalue 100000000000
maxvalue 9999999999999
start with 100000000000
increment by 1
cache 20;
