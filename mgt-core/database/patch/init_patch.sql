-- 0426
ALTER TABLE mgt_data_td_preloan_report ADD COLUMN apply_id int comment '申请借款ID';

ALTER TABLE mgt_accounts ADD COLUMN is_deleted tinyint default 0 comment '是否删除';


ALTER TABLE mgt_channels ADD COLUMN  receive_mode int comment '进件模式：1 注册；2 进件';

ALTER TABLE mgt_data_club_preloan_report ADD COLUMN  user_id int comment '用户ID';
alter table mgt_data_club_preloan_report add index i_mgt_clubreport_uid (user_id);


-- 0428
ALTER TABLE mgt_account_login_log ADD COLUMN  province varchar(30) comment '省份';
ALTER TABLE mgt_account_login_log ADD COLUMN  city varchar(30) comment '城市';
ALTER TABLE mgt_account_login_log ADD COLUMN  detail varchar(500) comment '接口返回详情';

-- 0429
ALTER TABLE mgt_account_dflow_settings ADD COLUMN  is_closed tinyint not null default 0 comment '开关：0关闭；1开启';
ALTER TABLE mgt_account_op_log MODIFY COLUMN op_date timestamp;

-- 0430
ALTER TABLE mgt_user_apply_records CHANGE load_date loan_date timestamp default CURRENT_TIMESTAMP() comment '打款时间';


-- 0502
-- 创建上下游依赖表
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

-- 渠道表：针对 电销、中介添加字段。隐藏渠道在前端不显示
ALTER TABLE mgt_channels add column is_hidden tinyint not null default 0 comment '是否隐藏渠道： 0-不隐藏，1-隐藏';

-- 借款记录表
ALTER TABLE mgt_user_loan_records add column apply_id int comment '申请借款ID';
alter table mgt_user_loan_records add index i_mgt_loanr_apply_id (apply_id);



-- 充值模块表
/*==============================================================*/
/* Table: mgt_org_recharges     组织账户总表                */
/*==============================================================*/
CREATE TABLE mgt_org_recharges
(
   org_id int not null comment '组织ID',
   total_data_amount int not null default 0 comment '总充值金额',
   total_sms_amount int not null default 0 comment '总短信充值金额',
   total_data_expense int not null default 0 comment '总流量消费金额',
   total_sms_expense int not null default 0 comment '总短信消费金额',
   PRIMARY KEY (org_id)
)
ENGINE InnoDB;


-- 充值模块表
/*==============================================================*/
/* Table: mgt_org_recharge_detail     组织账户流水表                */
/*==============================================================*/
CREATE TABLE mgt_org_recharge_detail
(
   trade_no varchar(32) not null comment '交易号',
   account_id int not null comment '消耗人员accountId',
   op_type int not null comment '11 - 充值数据，12 - 充值流量， 21 - 消耗数据， 22-消耗短信',
   amount int not null default 0 comment '金额',
   status int not null default 0 comment '0 - 提交，1 - 记账成功',
   creation_date timestamp(3) comment '创建时间',
   PRIMARY KEY (trade_no)
)
ENGINE InnoDB;

-- 0503

ALTER TABLE mgt_user_apply_records add column creation_date timestamp not null default CURRENT_TIMESTAMP() comment '创建时间';
alter table mgt_user_apply_records modify final_date  timestamp null;
alter table mgt_user_apply_records modify loan_date  timestamp null;



-- 0503
/*==============================================================*/
/* Table: mgt_data_yx_report     亿象报告：借贷宝、金借到、米房、无忧   */
/*==============================================================*/
CREATE TABLE mgt_data_yx_report
(
   task_id varchar(32) not null comment '任务号',
   user_id int null comment '用户号',
   apply_id int comment '申请借款ID',
   report_type varchar(32) not null comment '报告类型。 jiedaibao - 借贷宝，mifang - 米房， wuyoujietiao- 无忧，jinjiedao -今借到',
   status int not null default 0 comment '报告状态, 0 - 提交； 99-获取成功； 1 - 失败',
   token varchar(100) not null comment '报告查询凭证',
   creation_date timestamp(3) comment '任务创建时间',
   PRIMARY KEY (task_id)
)
ENGINE InnoDB;
alter table mgt_data_yx_report add index i_mgt_data_yx_report_uid (user_id);
alter table mgt_data_yx_report add index i_mgt_data_yx_report_report_type (report_type);
alter table mgt_data_yx_report add index i_mgt_data_yx_report_applyid (apply_id);


--0504
/*用户mgt_users表  添加字段*/
ALTER TABLE mgt_users ADD COLUMN  idcard_rear varchar(50) comment '身份证正面';
ALTER TABLE mgt_users ADD COLUMN  idcard_back varchar(50) comment '身份证反面';
ALTER TABLE mgt_users ADD COLUMN  idcard_info varchar(50) comment '手持身份证';
ALTER TABLE mgt_users ADD COLUMN  contract1_relation varchar(10) comment '联系人1关系';
ALTER TABLE mgt_users ADD COLUMN  contract1_username varchar(20) comment '联系人1名字';
ALTER TABLE mgt_users ADD COLUMN  contract1_phonenumber varchar(20) comment '联系人1联系方式';
ALTER TABLE mgt_users ADD COLUMN  contract2_relation varchar(10) comment '联系人2关系';
ALTER TABLE mgt_users ADD COLUMN  contract2_username varchar(20) comment '联系人2名字';
ALTER TABLE mgt_users ADD COLUMN  contract2_phonenumber varchar(20) comment '联系人2联系方式';
ALTER TABLE mgt_users ADD COLUMN  vidoe_screen_shot varchar(50) comment '上传视频截图';

-- 0504
-- 手机号检测表
/*==============================================================*/
/* Table: mgt_mobiles     手机号检测表               */
/*==============================================================*/
CREATE TABLE mgt_mobiles
(
   id int not null auto_increment,
   phone_number varchar(20) comment '手机号',
   check_type varchar(10) comment '检测类型',
   check_result varchar(10) comment '检测结果',
   check_date timestamp(3) comment '检测时间',
   PRIMARY KEY (id)
)
ENGINE InnoDB;

-- 0505
-- 用户key表 保证用户安全
/*==============================================================*/
/* Table: mgt_user_keys      用户key表              */
/*==============================================================*/
CREATE TABLE mgt_user_keys
(
   user_key VARCHAR(250) NOT NULL COMMENT '账户API_KEY',
   user_id INT COMMENT '用户Id',
   application_id INT COMMENT '地址id',
   expiry TIMESTAMP(3) NOT NULL DEFAULT '2030-12-30 00:00:00' COMMENT '过期时间',
   is_deleted TINYINT NOT NULL DEFAULT 0,
   PRIMARY KEY (user_key)
)
ENGINE INNODB;
ALTER TABLE mgt_user_keys ADD CONSTRAINT fk_mgt_user_keys_userid FOREIGN KEY (user_id) REFERENCES mgt_users(user_id);
ALTER TABLE mgt_user_keys ADD INDEX fk_mgt_user_keys_key (user_key);


-- 0512
/*==============================================================*/
/* Table: mgt_message_code                                          */
/*==============================================================*/
CREATE TABLE mgt_message_code
(
   phone_number varchar(20) comment '手机号',
   channel_code varchar(20) comment '渠道号',
   msg_code varchar(20) comment '验证码',
   creation_date timestamp(3) not null comment '创建时间',
   expire_date timestamp(3) not null comment '失效时间',
   PRIMARY KEY (phone_number,channel_code)
)
ENGINE InnoDB;
alter table mgt_message_code add index i_mgt_message_code_code (msg_code);
alter table mgt_message_code add index i_mgt_message_code_cdate (creation_date);

-- 0511
/*==============================================================*/
/* Table: mgt_users                                          */
/*==============================================================*/
ALTER TABLE mgt_users ADD COLUMN  jingdong_verified tinyint(4) comment '京东认证';
ALTER TABLE mgt_users ADD COLUMN  si_verified tinyint(4) comment '社保认证';
ALTER TABLE mgt_users ADD COLUMN  gjj_verified tinyint(4) comment '公积金认证';
ALTER TABLE mgt_users ADD COLUMN  chsi_verified tinyint(4) comment '学信认证';

-- 0512
/*==============================================================*/
/* Table: mgt_areazones      地区表         		                    */
/*==============================================================*/
CREATE TABLE `mgt_areazones` (
  `province` int(11) NOT NULL,
  `city` int(11) NOT NULL,
  `zone` varchar(10) NOT NULL,
  `areazone` varchar(45) NOT NULL,
  PRIMARY KEY (`zone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 0512
/*==============================================================*/
/* Table: mgt_channel      渠道表         		                    */
/*==============================================================*/
ALTER TABLE mgt_channels add column taobao_required tinyint not null default 1 comment '淘宝验证';
ALTER TABLE mgt_channels add column jingdong_required tinyint not null default 1 comment '京东验证';
ALTER TABLE mgt_channels add column gjj_required tinyint not null default 1 comment '公积金验证';
ALTER TABLE mgt_channels add column sj_required tinyint not null default 1 comment '社保验证';
--0515
/*==============================================================*/
/* Table: mgt_account_dependencies        		                    */
/*==============================================================*/
DROP TABLE IF EXISTS `mgt_account_dependencies`;
CREATE TABLE `mgt_account_dependencies` (
  `account_id` int(11) NOT NULL COMMENT '账户',
  `dep_account_id` int(11) NOT NULL COMMENT '依赖账户',
  `dep_relation` int(11) NOT NULL DEFAULT '1' COMMENT '依赖关系。 1 - 上一级， 2 - 下一级',
  `role_id` int(11) NOT NULL,
  `index` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`index`),
  KEY `i_mgt_acc_deps_accid` (`account_id`),
  KEY `i_mgt_acc_deps_dep_accid` (`dep_account_id`),
  KEY `i_mgt_acc_deps_dep_r` (`dep_relation`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

--0516
ALTER TABLE mgt_accounts ADD COLUMN is_freeze tinyint default 0 comment '是否冻结';

--0518
/*==============================================================*/
/* Table: mgt_user_event_logs      用户访问记录表         		                    */
/*==============================================================*/
CREATE TABLE `mgt_user_event_logs` (
  `user_id` int(11) DEFAULT NULL COMMENT '用户Id',
  `source_id` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '渠道',
  `cookie_id` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '存储Id',
  `event_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '事件名称',
  `event_action` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '事件动作',
  `event_label` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '事作标签',
  `event_value` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '事件值',
  `creation_date` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `remote_address` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  KEY `i_event_logs_user` (`user_id`),
  KEY `i_event_logs_sid` (`source_id`),
  KEY `i_event_logs_cid` (`cookie_id`),
  KEY `i_event_logs_name` (`event_name`),
  KEY `i_event_logs_action` (`event_action`),
  KEY `i_event_logs_lable` (`event_label`),
  KEY `i_event_logs_value` (`event_value`),
  KEY `i_event_logs_cdate` (`creation_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

--0518
ALTER TABLE mgt_organizations add column count int not null default 30 comment '账号数量';



-- 0519

ALTER TABLE mgt_users ADD COLUMN  jbb_user_id int comment 'JBB ID';
ALTER TABLE mgt_users ADD COLUMN  ip_area varchar(30) comment 'IP地区';


ALTER TABLE mgt_user_loan_record_details MODIFY COLUMN  loan_id int comment '借款编号 ';
ALTER TABLE mgt_user_loan_record_details MODIFY COLUMN  op_type int(4) comment '操作opType，对应日志表';
ALTER TABLE mgt_user_loan_record_details ADD COLUMN  amount_type int(4) comment '类型：1 打款；2 还入；3 展期费用(利息)';


/*==============================================================*/
/* Table: mgt_loan_record_op_log     借款记录操作日志表                  */
/*==============================================================*/
CREATE TABLE mgt_loan_record_op_log
(
   log_id int auto_increment comment '日志编号', 
   account_id int comment '操作账户ID',
   loan_id int comment '用户申请ID',
   op_type int(4) not null  comment '操作类型',
   op_date timestamp comment '操作时间',
   op_reason varchar(250) comment '操作理由',
   op_comment varchar(250) comment '操作记录',
   PRIMARY KEY (log_id)
)
ENGINE InnoDB;
alter table mgt_loan_record_op_log add index i_mgt_loan_record_op_log_accid (account_id);
alter table mgt_loan_record_op_log add index i_mgt_loan_record_op_log_loanid (loan_id);
alter table mgt_loan_record_op_log add index i_mgt_loan_record_op_log_optype (op_type);
alter table mgt_loan_record_op_log add index i_mgt_loan_record_op_log_opdate (op_date);


drop table mgt_user_loan_records;
CREATE TABLE mgt_user_loan_records
( 
   loan_id int auto_increment comment 'loanId', 
   iou_code varchar(50) comment '贷款记录编号，如果为借帮帮记录，则为借帮帮借条编号',
   apply_id int comment '申请借款ID',
   account_id int comment '出借人 ',
   user_id int comment '借款人',
   status int(4) not null default 1 comment '款项状态',
   iou_status int(4) not null default 0 comment '借条状态, JBB平台同步',
   iou_platform_id int DEFAULT 0 COMMENT '打借条平台：0其他;1 借帮帮；2借贷宝',
   borrowing_amount int not null comment '借款金额',
   annual_rate int DEFAULT NULL COMMENT '年化利率, 记录整数 ， 比如1.5%，记为150',
   borrowing_date timestamp null default null comment '借款时间',
   borrowing_days int comment '借款天数',
   repayment_date timestamp null default null comment '到期时间',
   loan_acc_id int comment '打款人员',
   loan_amount int comment '打款金额',
   loan_date timestamp null default null comment '打款时间',
   collector_acc_id int comment '催收人员',
   creation_date timestamp  null default null comment '创建时间',
   update_date timestamp null default null comment '更新时间',
   actual_repayment_date timestamp null default null comment '实际还款时间',
   PRIMARY KEY (loan_id)
)

--0519
/*==============================================================*/
/* Table: mgt_iou_platform      借条平台基表         		                    */
/*==============================================================*/
CREATE TABLE mgt_iou_platform
(
   iou_platform_id int not null auto_increment,
	 platform_name varchar(50) comment '借条平台名称',
   description varchar(50) comment '描述',
   PRIMARY KEY (iou_platform_id)
)
ENGINE InnoDB;

-- 0521
ALTER TABLE mgt_user_loan_records ADD COLUMN  repay_amount int comment '催回金额总计';

--0521 为渠道账号添加唯一索引
ALTER TABLE mgt_channels ADD UNIQUE INDEX(source_phone_number);

--0528 为渠道表增加学信网验证
ALTER TABLE mgt_channels add column chsi_required tinyint not null default 1 comment '学信网验证';

--0528 组织添加短信签名和短信模板字段
ALTER TABLE mgt_organizations ADD COLUMN  sms_sign_name varchar(20) comment '短信签名';
ALTER TABLE mgt_organizations ADD COLUMN  sms_template_id varchar(20) comment '短信模板';

--0529 添加拒绝时间和挂起时间
ALTER TABLE mgt_user_apply_records ADD COLUMN  reject_date timestamp NULL DEFAULT NULL COMMENT '拒绝时间';
ALTER TABLE mgt_user_apply_records ADD COLUMN  hangup_date timestamp NULL DEFAULT NULL COMMENT '挂起时间';

--0601
/*==============================================================*/
/* Table: mgt_data_yx_urls      二维码表         		                    */
/*==============================================================*/
DROP TABLE IF EXISTS `mgt_data_yx_urls`;
CREATE TABLE `mgt_data_yx_urls` (
  `user_id` int(11) NOT NULL,
  `report_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `h5_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `creation_date` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  PRIMARY KEY (`user_id`,`report_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 修改mgt_user表字段
alter table mgt_users modify column contract1_relation VARCHAR(20);
alter table mgt_users modify column contract2_relation VARCHAR(20);

--修改mgt_channels字段
alter table mgt_channels  alter column qq_required set default 0;
alter table mgt_channels  alter column wechat_required set default 0;
alter table mgt_channels  alter column zhima_required set default 0;
alter table mgt_channels  alter column idcard_info_required set default 0;
alter table mgt_channels  alter column idcard_back_required set default 0;
alter table mgt_channels  alter column idcard_rear_required set default 0;
alter table mgt_channels  alter column header_required set default 0;
alter table mgt_channels  alter column mobile_contract1_required set default 0;
alter table mgt_channels  alter column mobile_contract2_required set default 0;
alter table mgt_channels  alter column mobile_service_info_required set default 0;
alter table mgt_channels  alter column taobao_required set default 0;
alter table mgt_channels  alter column jingdong_required set default 0;
alter table mgt_channels  alter column gjj_required set default 0;
alter table mgt_channels  alter column sj_required set default 0;
alter table mgt_channels  alter column chsi_required set default 0;

ALTER TABLE mgt_users ADD COLUMN  vidoe_screen_shot varchar(50) comment '上传视频截图';

ALTER TABLE mgt_user_loan_records ADD COLUMN  extention_date timestamp NULL DEFAULT NULL COMMENT '展期时间';

-- 0605
ALTER TABLE mgt_users ADD COLUMN  platform varchar(20) comment '注册平台 web, android, ios';
ALTER TABLE mgt_users ADD COLUMN  mobile_manufacture varchar(50) comment '手机型号';
ALTER TABLE mgt_users ADD COLUMN  idcard_address varchar(200) comment '身份证地址';
ALTER TABLE mgt_users ADD COLUMN  race varchar(20) comment '民族';

-- 0605
ALTER TABLE mgt_payorders CHANGE COLUMN order_no out_trade_no varchar(200);
ALTER TABLE mgt_payorders ADD COLUMN  trade_no VARCHAR(200) comment '支付宝流水号';
ALTER TABLE mgt_payorders ADD COLUMN  goods_id VARCHAR(200) comment '商品id';

-- 0606
CREATE TABLE `mgt_system_properties` (
  `name` varchar(50) NOT NULL COMMENT '属性名',
  `value` varchar(5000) DEFAULT NULL COMMENT '属性值',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 0608
ALTER TABLE mgt_data_yx_report MODIFY COLUMN  task_id varchar(100) comment '任务编号';

--0608
-- 新建表mgt_user_apply_records_prc
-- DROP TABLE IF EXISTS `mgt_user_apply_records_spc`;
CREATE TABLE `mgt_user_apply_records_spc` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `account_id` int(11) NOT NULL DEFAULT '0' COMMENT '账号Id',
  `creation_date` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '创建时间'
)

--0609
--新建表mgt_organization_relations
DROP TABLE IF EXISTS `mgt_organization_relations`;
CREATE TABLE mgt_organization_relations (
	org_id INT NOT NULL COMMENT '组织ID',
	sub_org_id INT NOT NULL COMMENT '子组织ID',
	PRIMARY KEY (`org_id`,`sub_org_id`)
)