USE jbbmgtserver;

-- Drop all exist tables
DROP TABLE IF EXISTS mgt_info;
DROP TABLE IF EXISTS mgt_channels;
DROP TABLE IF EXISTS mgt_role_permissions;
DROP TABLE IF EXISTS mgt_permissions;
DROP TABLE IF EXISTS mgt_account_roles;
DROP TABLE IF EXISTS mgt_roles;
DROP TABLE IF EXISTS mgt_account_roles;
DROP TABLE IF EXISTS mgt_account_keys;
DROP TABLE IF EXISTS mgt_account_login_log;
DROP TABLE IF EXISTS mgt_user_apply_records;
DROP TABLE IF EXISTS mgt_account_op_log;
DROP TABLE IF EXISTS mgt_user_loan_records;
DROP TABLE IF EXISTS mgt_organizations;
DROP TABLE IF EXISTS mgt_accounts;
DROP TABLE IF EXISTS mgt_users;
DROP TABLE IF EXISTS mgt_dflow_base;
DROP TABLE IF EXISTS mgt_account_dflow_settings;
DROP TABLE IF EXISTS mgt_data_td_preloan_report;
DROP TABLE IF EXISTS mgt_data_club_preloan_report;


/*==============================================================*/
/* Table: info                                                  */
/*==============================================================*/
CREATE TABLE mgt_info
(
   db_version int,
   props_version int
)
ENGINE InnoDB;



/*==============================================================*/
/* Table: mgt_channels      渠道表                               */
/*==============================================================*/
CREATE TABLE mgt_channels
(
   channel_code varchar(20) not null comment '渠道编号',
   channel_name varchar(50) not null comment '渠道名',
   channel_url varchar(500) comment '渠道URL',
   service_qq varchar(20) comment '客服QQ',
   service_wechat varchar(50) comment '客服微信二维码',
   status tinyint not null default 0 comment '渠道状态,1-表示删除;2-表示冻结',
   mode int comment '结算模式：CPA, CPS, CPA+CPS',
   receive_mode int comment '进件模式：1 注册；2 进件',
   source_phone_number varchar(20) comment '渠道方账户',
   source_password varchar(200) comment '渠道方账户密码',
   creator int not null  comment '创建人',
   creation_date timestamp(3) comment '创建时间',
   qq_required tinyint not null default 1 comment 'QQ是否必填',
   wechat_required tinyint not null default 1 comment '微信是否必填',
   zhima_required tinyint not null default 1 comment '芝麻分是否必填',
   idcard_info_required tinyint not null default 1 comment '姓名/身份证号是否必须',
   idcard_back_required tinyint not null default 1 comment '身份证反面是否必填',
   idcard_rear_required tinyint not null default 1 comment '身份证正面是否必填',
   header_required tinyint not null default 1 comment '手持身份证照片是否必填',
   mobile_contract1_required tinyint not null default 1 comment '第一联系人是否必填',
   mobile_contract2_required tinyint not null default 1 comment '第二联系人是否必填',
   mobile_service_info_required tinyint not null default 1 comment '服务商认证是否必填',
   cpa_price int not null default 0 comment 'CPA价格',
   cps_price int not null default 0 comment 'CPS价格，百分数1.5% 填入150',
   is_hidden tinyint not null default 0 comment '是否隐藏渠道： 0-不隐藏，1-隐藏',
   PRIMARY KEY (channel_code)
)
ENGINE InnoDB;
alter table mgt_channels add index i_mgt_channels_creator (creator);
alter table mgt_channels add index i_mgt_channels_name (channel_name);
alter table mgt_channels add index i_mgt_channels_sphonenumber (source_phone_number);


/*==============================================================*/
/* Table: mgt_users      流量用户表                               */
/*==============================================================*/
CREATE TABLE mgt_users
(
   user_id int not null auto_increment,
   username varchar(50) comment '姓名',
   phone_number varchar(20) not null comment '手机号',
   idcard varchar(20) comment '证件号',
   ip_address varchar(20) comment '申请时IP地址',
   zhima_score int comment '芝麻分',
   qq varchar(20) comment 'qq号',
   wechat varchar(50) comment '微信号',
   channel_code varchar(20) not null comment '来源渠道',
   realname_verified tinyint comment '是否实名认证',
   mobile_verified tinyint comment '是否运营商认证',
   zhima_verified tinyint  comment '是否芝麻认证',
   creation_date timestamp(3) not null comment '创建时间',
   PRIMARY KEY (user_id)
)
ENGINE InnoDB;
alter table mgt_users add index i_mgt_users_phone_number (phone_number);
alter table mgt_users add index i_mgt_users_idcard (idcard);
alter table mgt_users add index i_mgt_users_username (username);
alter table mgt_users add index i_mgt_users_zhima_score (zhima_score);
alter table mgt_users add index i_mgt_users_channel_code (channel_code);


/*==============================================================*/
/* Table: mgt_organizations    组织表                           */
/*==============================================================*/
CREATE TABLE mgt_organizations
(
   org_id int not null auto_increment,
   name varchar(50) comment '组织名',
   description varchar(50) comment '组织描述',
   deleted tinyint not null default 0  comment '是否被删除',
   PRIMARY KEY (org_id)
)
ENGINE InnoDB;

/*==============================================================*/
/* Table: mgt_permissions     权限基表                            */
/*==============================================================*/
CREATE TABLE mgt_permissions
(
   permission_id int not null auto_increment,
   description varchar(50) comment '描述',
   PRIMARY KEY (permission_id)
)
ENGINE InnoDB;

/*==============================================================*/
/* Table: mgt_roles     角色基表                            */
/*==============================================================*/
CREATE TABLE mgt_roles
(
   role_id int not null auto_increment,
   description varchar(50) comment '描述',
   PRIMARY KEY (role_id)
)
ENGINE InnoDB;

/*==============================================================*/
/* Table: mgt_role_permissions      角色权限表                    */
/*==============================================================*/
CREATE TABLE mgt_role_permissions
(
   role_id int not null comment '角色ID',
   permission_id int not null comment '权限ID',
   PRIMARY KEY (role_id, permission_id)
)
ENGINE InnoDB;
alter table mgt_role_permissions add constraint fk_mgt_role_p_pid foreign key (permission_id) references mgt_permissions(permission_id);
alter table mgt_role_permissions add constraint fk_mgt_role_p_rid foreign key (role_id) references mgt_roles(role_id);

/*==============================================================*/
/* Table: mgt_accounts      管理账户表                            */
/*==============================================================*/
CREATE TABLE mgt_accounts
(
   account_id int not null auto_increment,
   username varchar(50) not null unique comment '登录名',
   nikename varchar(50) comment '妮称',
   phone_number varchar(20) comment '手机号',
   jbb_user_id int comment 'JBB用户ID',
   password varchar(500) comment '密码',
   org_id int comment '组织ID',
   is_deleted tinyint not null default 0 comment '是否删除',
   creator int comment '创建人',
   creation_date timestamp(3) comment '创建时间',
   PRIMARY KEY (account_id)
)
ENGINE InnoDB;
alter table mgt_accounts add index i_mgt_acc_username (username);
alter table mgt_accounts add index i_mgt_acc_phone_number (phone_number);
alter table mgt_accounts add index i_mgt_acc_org_id (org_id);


/*==============================================================*/
/* Table: mgt_account_dependencies     账户的上下级流程关系                          */
/*==============================================================*/
CREATE TABLE mgt_account_dependencies
(
   account_id int not null comment '账户',
   dep_account_id int not null comment '依赖账户',
   dep_relation int not null default 1 comment '依赖关系。 1 - 上一级， 2 - 下一级'
)
ENGINE InnoDB;
alter table mgt_account_dependencies add index i_mgt_acc_deps_accid (account_id);
alter table mgt_account_dependencies add index i_mgt_acc_deps_dep_accid (dep_account_id);
alter table mgt_account_dependencies add index i_mgt_acc_deps_dep_r (dep_relation);


/*==============================================================*/
/* Table: mgt_user_roles      用户角色表                    */
/*==============================================================*/
CREATE TABLE mgt_account_roles
(
   account_id int not null comment '用户ID',
   role_id int not null comment '角色ID',
   PRIMARY KEY (account_id, role_id)
)
ENGINE InnoDB;



/*==============================================================*/
/* Table: user keys     API_KEY表                               */
/*==============================================================*/
CREATE TABLE mgt_account_keys
(
   user_key varchar(250) not null comment '账户API_KEY',
   account_id int comment '用户Id',
   expiry timestamp(3) NOT NULL DEFAULT '2030-12-30 00:00:00' comment '过期时间',
   is_deleted tinyint not null default 0,
   PRIMARY KEY (account_id)
)
ENGINE InnoDB;
alter table mgt_account_keys add constraint fk_mgt_acc_keys_accid foreign key (account_id) references mgt_accounts(account_id);
alter table mgt_account_keys add index fk_mgt_acc_keys_key (user_key);

/*==============================================================*/
/* Table: mgt_account_login_log     账户登录日志表                             */
/*==============================================================*/
CREATE TABLE mgt_account_login_log
(
   account_id int comment '用户Id',
   login_date timestamp(3) NOT NULL comment '登录时间',
   ip_address varchar(50) comment '登录IP',
   province varchar(30) comment '省份',
   city varchar(30) comment '城市',
   detail varchar(500) comment '接口返回详情'
)
ENGINE InnoDB;
alter table mgt_account_login_log add index i_mgt_login_accid (account_id);
alter table mgt_account_login_log add index i_mgt_login_date (login_date);
alter table mgt_account_login_log add index i_mgt_login_ip (ip_address);


/*==============================================================*/
/* Table: mgt_user_apply_records     用户申请记录表                */
/*==============================================================*/
CREATE TABLE mgt_user_apply_records
(
   apply_id int not null auto_increment comment '申请ID',
   user_id int comment '用户ID',
   org_id int comment '组织ID',
   creation_date timestamp not null default CURRENT_TIMESTAMP() comment '创建时间',
   status int(4) not null default 0 comment '用户状态',
   assign_acc_id int comment '分配人员',
   assing_date timestamp comment '分配时间',
   init_acc_id int comment '初配人员',
   init_amount int comment '初审金额',
   init_mark varchar(100) comment '初审标记',
   init_date timestamp comment '初审通过时间',
   final_acc_id int comment '复配人员',
   final_amount int comment '复审金额',
   final_mark varchar(100) comment '复审标记',
   final_date timestamp comment '复审通过时间',
   loan_acc_id int comment '打款人员',
   loan_amount int comment '打款金额',
   load_date timestamp comment '打款时间',
   PRIMARY KEY (apply_id)
)
ENGINE InnoDB;
alter table mgt_user_apply_records add constraint fk_mgt_uar_oid foreign key (org_id) references mgt_organizations(org_id);
alter table mgt_user_apply_records add constraint fk_mgt_uar_uid foreign key (user_id) references mgt_users(user_id);
alter table mgt_user_apply_records add index i_mgt_uar_uid (user_id);
alter table mgt_user_apply_records add index i_mgt_uar_oid (org_id);
alter table mgt_user_apply_records add index i_mgt_uar_status (status);
alter table mgt_user_apply_records add index i_mgt_uar_aaccid (assign_acc_id);
alter table mgt_user_apply_records add index i_mgt_uar_adate (assing_date);
alter table mgt_user_apply_records add index i_mgt_uar_iaccid (init_acc_id);
alter table mgt_user_apply_records add index i_mgt_uar_idate (init_date);
alter table mgt_user_apply_records add index i_mgt_uar_faccid (final_acc_id);
alter table mgt_user_apply_records add index i_mgt_uar_fdate (final_date);
alter table mgt_user_apply_records add index i_mgt_uar_laccid (loan_acc_id);
alter table mgt_user_apply_records add index i_mgt_uar_ldate (load_date);
alter table mgt_user_apply_records add index i_mgt_uar_cdata (creation_date);

/*==============================================================*/
/* Table: mgt_account_op_log     账户操作日志                      */
/*==============================================================*/
CREATE TABLE mgt_account_op_log
(
   log_id int auto_increment comment '日志编号', 
   account_id int comment '操作账户ID',
   apply_id int comment '用户申请ID',
   op_type int(4) not null  comment '操作类型',
   op_date timestamp comment '操作时间',
   op_reason varchar(250) comment '操作理由',
   op_comment varchar(250) comment '操作记录',
   PRIMARY KEY (log_id)
)
ENGINE InnoDB;
alter table mgt_account_op_log add index i_mgt_account_op_log_accid (account_id);
alter table mgt_account_op_log add index i_mgt_account_op_log_applyid (apply_id);
alter table mgt_account_op_log add index i_mgt_account_op_log_optype (op_type);
alter table mgt_account_op_log add index i_mgt_account_op_log_opdate (op_date);

/*==============================================================*/
/* Table: mgt_user_loan_records     贷款记录表                      */
/*==============================================================*/
CREATE TABLE mgt_user_loan_records
( 
   loan_id varchar(50) comment '贷款记录编号，如果为借帮帮记录，则为借帮帮借条编号',
   apply_id int comment '申请借款ID',
   account_id int comment '出借人 ',
   user_id int comment '借款人',
   status int(4) not null default 0 comment '款项状态',
   iou_platform_id int DEFAULT 0 COMMENT '打借条平台：0其他;1 借帮帮；2借贷宝',
   borrowing_amount int not null comment '借款金额',
   annual_rate int DEFAULT NULL COMMENT '年化利率, 记录整数 ， 比如1.5%，记为150',
   borrowing_date date comment '借款时间',
   borrowing_days int comment '借款天数',
   repayment_date date comment '到期时间',
   PRIMARY KEY (loan_id)
)
ENGINE InnoDB;
alter table mgt_user_loan_records add index i_mgt_loanr_accid (account_id);
alter table mgt_user_loan_records add index i_mgt_loanr_uid (user_id);
alter table mgt_user_loan_records add index i_mgt_loanr_apply_id (apply_id);
alter table mgt_user_loan_records add constraint fk_mgt_loanr_accid foreign key (account_id) references mgt_accounts(account_id);
alter table mgt_user_loan_records add constraint fk_mgt_loanr_uid foreign key (user_id) references mgt_users(user_id);
alter table mgt_user_loan_records add index i_mgt_loanr_status (status);
alter table mgt_user_loan_records add index i_mgt_loanr_bdate (borrowing_date);
alter table mgt_user_loan_records add index i_mgt_loanr_rdate (repayment_date);
alter table mgt_user_loan_records add index i_mgt_loanr_bdays (borrowing_days);

/*==============================================================*/
/* Table: mgt_user_loan_record_details     贷款收支流水表                      */
/*==============================================================*/
CREATE TABLE mgt_user_loan_record_details
( 
   loan_id varchar(50) not null comment '贷款记录编号，如果为借帮帮记录，则为借帮帮借条编号',
   op_type int not null comment '类型：1 打款；2 还入；3 展期费用',
   amount int not null default 0 comment '金额(分)',
   account_id int not null comment '操作人',
   op_date timestamp(3) NOT NULL comment '流水时间'
)
ENGINE InnoDB;
alter table mgt_user_loan_record_details add index i_mgt_lrd_loan_id (loan_id);
alter table mgt_user_loan_record_details add index i_mgt_lrd_loan_op_type (op_type);
alter table mgt_user_loan_record_details add index i_mgt_lrd_loan_accid (account_id);
alter table mgt_user_loan_record_details add index i_mgt_lrd_loan_op_date (op_date);

/*==============================================================*/
/* Table: mgt_dflow_base     流量定价基表                      */
/*==============================================================*/
CREATE TABLE mgt_dflow_base
(
   dflow_id int auto_increment comment '编号', 
   jexl_script varchar(1000) comment '过滤表达式',
   jexl_desc varchar(500) not null comment '过滤描述',
   price int not null  comment '价格',
   creation_date timestamp(3) comment '创建时间',
   is_deleted tinyint not null default 0 comment '是否被删除',
   PRIMARY KEY (dflow_id)
)
ENGINE InnoDB;


/*==============================================================*/
/* Table: mgt_account_dflow_settings     账户流量设置                      */
/*==============================================================*/
CREATE TABLE mgt_account_dflow_settings
(
   org_id int not null comment '机构ID', 
   is_closed tinyint not null default 0 comment '开关：0关闭；1开启',
   dflow_id int comment '流量dflow编号',
   min_value int comment '流量最小值',
   max_value int comment '流量最大值',
   PRIMARY KEY (org_id)
)
ENGINE InnoDB;


/*==============================================================*/
/* Table: mgt_tele_marketing    电销号码批次表                    */
/*==============================================================*/
CREATE TABLE mgt_tele_marketing
(
   batch_id int auto_increment comment '批次号', 
   account_id int not null comment '导入账户ID', 
   cnt int  not null default 0  comment '导入数量',
   valid_cnt int not null default 0 comment '可用数',
   price int comment '成本',
   status int not null default 0 comment '状态：0-导入；1-处理中；2-处理完成',
   creation_date timestamp(3) comment '导入时间',
   PRIMARY KEY (batch_id)
)
ENGINE InnoDB;
alter table mgt_tele_marketing add index i_mgt_telem_accid (account_id);
alter table mgt_tele_marketing add index i_mgt_telem_cdata (creation_date);

/*==============================================================*/
/* Table: mgt_tele_marketing_detail   电销号码明细表                 */
/*==============================================================*/
CREATE TABLE mgt_tele_marketing_detail
(
   id int auto_increment comment '明细ID', 
   batch_id int not null comment '批次号', 
   telephone varchar(20) not null comment '电话号码', 
   username varchar(50) comment '姓名', 
   area varchar(20)  comment '归属地', 
   number_type varchar(20) comment '运营商类型', 
   charges_status varchar(20) comment '是否扣费', 
   last_date timestamp(3) comment '最近使用时间',
   status int not null default 5 comment '状态：0：空号；1：实号；2：停机；3:库无；4:沉默号；5：导入; 6: 重复号；7：不符合电话格式',
   update_date timestamp(3) comment '更新时间',
   PRIMARY KEY (id)
)
ENGINE InnoDB;
alter table mgt_tele_marketing_detail add index i_mgt_tele_detail (batch_id);
alter table mgt_tele_marketing_detail add index i_mgt_tele_telephone (telephone);


/*==============================================================*/
/* Table: mgt_mobile_wool_check   电话羊毛党检测                */
/*==============================================================*/
CREATE TABLE mgt_mobile_wool_check
(
   tradeNo varchar(50) not null comment '交易号', 
   mobile varchar(20) not null comment '手机号', 
   tag varchar(200)  comment '标签', 
   ip_address varchar(50) comment '提交IP地址', 
   charges_status varchar(20) comment '是否扣费', 
   status varchar(20) comment '状态：w1：白名单；w2：疑似白名单；B1：黑名单；B2:疑似黑名单；N:未找到',
   update_date timestamp(3) comment '更新时间',
   PRIMARY KEY (tradeNo)
)
ENGINE InnoDB;
alter table mgt_mobile_wool_check add index i_mgt_mobile_wool_mobile (mobile);
alter table mgt_mobile_wool_check add index i_mgt_mobile_wool_udate (update_date);

/*==============================================================*/
/* Table: mgt_tele_marketing_init   电销号码初审记录表               */
/*==============================================================*/

CREATE TABLE mgt_tele_marketing_init
(
   id int not null comment '明细ID', 
   assign_account_id int comment '分配员', 
   init_account_id int comment '分配的初审员', 
   op_commet varchar(50) comment '初审员标记', 
   op_commet_flag tinyint default 0 comment '是否标记', 
   assign_date timestamp(3) comment '分配时间',
   update_date timestamp(3) comment '最近一次处理时间',
   PRIMARY KEY (id)
)
ENGINE InnoDB;
alter table mgt_tele_marketing_init add index i_mgt_tele_init_iaccid (init_account_id);
alter table mgt_tele_marketing_init add index i_mgt_tele_init_aaccid (assign_account_id);
alter table mgt_tele_marketing_init add index i_mgt_tele_init_cflag (op_commet_flag);




/*==============================================================*/
/* 以下为数据获取相关表                   */
/*==============================================================*/
/*==============================================================*/
/* Table: mgt_data_td_preloan_report  多头借贷风控    */
/*==============================================================*/
CREATE TABLE mgt_data_td_preloan_report
( 
   report_id varchar(30) comment '报告编号 ',
   user_id int comment '用户ID',
   apply_id int comment '申请借款ID',
   req varchar(2000) comment '请求数据',
   rsp varchar(65535) comment '返回数据',
   apply_date timestamp not null default current_timestamp comment '申请时间',
   report_date timestamp null default null comment '报告时间',
   final_score int comment '最终得分',
   final_decision varchar(200) comment '最终得分说明',
   status int(4) not null default 0 comment '请求状态 0-提交； 99-获取成功； 2-处理中；3-获取失败',
   PRIMARY KEY (report_id)
)
ENGINE InnoDB;
alter table mgt_data_td_preloan_report add index i_mgt_preloan_report_uid (user_id);
alter table mgt_data_td_preloan_report add index i_mgt_preloan_report_aid (apply_id);
alter table mgt_data_td_preloan_report add index i_mgt_preloan_report_rdata (report_date);


/*==============================================================*/
/* Table: mgt_data_club_preloan_report  数聚魔盒报告    */
/*==============================================================*/
CREATE TABLE mgt_data_club_preloan_report
( 
   task_id varchar(100) not null comment '报告编号 ',
   user_id int comment '用户ID',
   status int not null default 0 comment '状态 0-提交； 99-获取成功； 1 - 创建 2-超时；3-失败',
   channel_type varchar(50) comment '渠道类型',
   channel_code varchar(50) comment '渠道编号',
   channel_src varchar(50) comment '渠道数据源',
   channel_attr varchar(50) comment '渠道属性',
   real_name varchar(50) comment '真实姓名',
   identity_code varchar(50) comment '证件号',
   created_time varchar(20) comment '任务创建时间',
   user_name varchar(50) comment '用户名',
   PRIMARY KEY (task_id)
)
ENGINE InnoDB;
alter table mgt_data_club_preloan_report add index i_mgt_clubreport_status (status);
alter table mgt_data_club_preloan_report add index i_mgt_clubreport_uid (user_id);
alter table mgt_data_club_preloan_report add index i_mgt_clubreport_ctype (channel_type);
alter table mgt_data_club_preloan_report add index i_mgt_clubreport_ccode (channel_code);
alter table mgt_data_club_preloan_report add index i_mgt_clubreport_rname (real_name);
alter table mgt_data_club_preloan_report add index i_mgt_clubreport_icode (identity_code);
alter table mgt_data_club_preloan_report add index i_mgt_clubreport_ctime (created_time);

alter table mgt_org_recharge_detail add params varchar(500) null default null;

alter table mgt_account_op_log modify column op_date timestamp(3);

/*==============================================================*/
/* Table: mgt_user_contants  用户联系人信息    */
/*==============================================================*/
CREATE TABLE mgt_user_contants (
	user_id INT NOT NULL COMMENT '用户ID',
	phone_number VARCHAR (20) COMMENT '手机号',
	username VARCHAR (50) COMMENT '姓名',
	vcard VARCHAR (2000) COMMENT 'vcard文本',
	json_str VARCHAR (2000) COMMENT '手机读取内容，存储的json文本',
	PRIMARY KEY (user_id, phone_number, username)
)
