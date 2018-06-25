/*==============================================================*/
/* JBB Server database                                          */
/*==============================================================*/
CREATE DATABASE IF NOT EXISTS jbbserver
    CHARACTER SET utf8
    COLLATE utf8_bin;

USE jbbserver;

-- Drop all exist tables
DROP TABLE IF EXISTS advertising;
DROP TABLE IF EXISTS info;
DROP TABLE IF EXISTS loan_platform_attrs;
DROP TABLE IF EXISTS company_introduction;
DROP TABLE IF EXISTS devices;
DROP TABLE IF EXISTS user_messages;
DROP TABLE IF EXISTS message_code;
DROP TABLE IF EXISTS user_login_logs;
DROP TABLE IF EXISTS user_event_logs;
DROP TABLE IF EXISTS loan_types;
DROP TABLE IF EXISTS loan_platform_templates;
DROP TABLE IF EXISTS repayment_details;
DROP TABLE IF EXISTS billing_details;
DROP TABLE IF EXISTS billings;
DROP TABLE IF EXISTS loan_platforms;
DROP TABLE IF EXISTS user_keys;
DROP TABLE IF EXISTS users;

/*==============================================================*/
/* Table: info                                                  */
/*==============================================================*/
CREATE TABLE info
(
   db_version int,
   props_version int
)
ENGINE InnoDB;

/*==============================================================*/
/* Table: advertising                                           */
/*==============================================================*/
CREATE TABLE advertising
(
   ad_id int not null auto_increment,
   device_platform varchar(50) comment '访问平台:IOS,Android,Web',
   advertising_name varchar(50) comment '广告名称',
   pic_path varchar(50) comment '图片路径',
   content varchar(50) comment '内容',
   priority int comment '显示优先级',
   click_path varchar(200) comment '链接路径',
   creation_date timestamp(3) not null comment '创建时间',
   PRIMARY KEY (ad_id)
)
ENGINE InnoDB;
alter table advertising add index i_advertising_platform (device_platform);


/*==============================================================*/
/* Table: loan_platforms                                           */
/*==============================================================*/
CREATE TABLE loan_platforms
(
   platform_id int not null auto_increment,
   name varchar(50) comment '贷款平台名称',
   description varchar(500) comment '描述',
   min_amount varchar(20) comment '最低可借' default '100',
   max_amount varchar(20) comment '最高可借',
   amount_desc varchar(20) comment '可借金额',
   limit_time varchar(50) comment '借款期限',
   loan_rate  varchar(20) comment '借款利息',
   fastest_time  varchar(50) comment '最快放款时间',
   path  varchar(200) comment '主页地址',
   priority  varchar(20) comment '推荐优先级',
   service_telephone  varchar(50) comment '服务号码',
   is_popular tinyint comment '是否热门',
   is_latest tinyint comment '是否最新',
   is_matched tinyint comment '是否匹配',
   is_deleted tinyint comment '是否删除' default 0,
   creation_date timestamp(3) comment '创建时间',
   PRIMARY KEY (platform_id)
)
ENGINE InnoDB;
alter table loan_platforms add index i_loan_platform_name (name);
alter table loan_platforms add index i_loan_platform_is_deleted (is_deleted);
alter table loan_platforms add index i_loan_platform_is_matched (is_matched);
alter table loan_platforms add index i_loan_platform_is_latest (is_latest);
alter table loan_platforms add index i_loan_platform_is_popular (is_popular);

/*==============================================================*/
/* Table: loan_platform_attrs                                          */
/*==============================================================*/
CREATE TABLE loan_platform_attrs
(
   platform_id int not null auto_increment,
   attr_name varchar(50) comment '属性名称',
   attr_value varchar(200) comment '属性值',
   PRIMARY KEY (platform_id, attr_name)
)
ENGINE InnoDB;
alter table loan_platform_attrs add constraint fk_loan_platform_attrs_id foreign key (platform_id) references loan_platforms(platform_id);
alter table loan_platform_attrs add index i_loan_platform_attrs_name (attr_name);

/*==============================================================*/
/* Table: company_introduction                                          */
/*==============================================================*/
CREATE TABLE company_introduction
(
   id int not null auto_increment,
   corporate_name varchar(200) comment '公司名称',
   introduce varchar(200) comment '公司简介',
   about_us varchar(200) comment '公司介绍',
   qq_group varchar(200) comment 'QQ群',
   establishment_time datetime comment '创建时间',
   PRIMARY KEY (id)
)
ENGINE InnoDB;


/*==============================================================*/
/* Table: devices                                          */
/*==============================================================*/
CREATE TABLE devices
(
   equipment_number varchar(50) comment '设备号',
   creation_date timestamp(3) not null comment '创建时间' ,
   PRIMARY KEY (equipment_number)
)
ENGINE InnoDB;


/*==============================================================*/
/* Table: user_messages                                          */
/*==============================================================*/
CREATE TABLE user_messages
(
   id int auto_increment,
   user_id int comment '用户ID',
   platform_id int comment '平台ID',
   content  varchar(500) comment '短信内容',
   is_read  tinyint comment '是否已读：1已读',
   sending_number varchar(20) comment '发送号码',
   creation_date timestamp(3) comment '创建时间',
   sending_date timestamp(3) comment '发送时间',
   PRIMARY KEY (id)
)
ENGINE InnoDB;
alter table user_messages add index i_user_msgs_user (user_id);
alter table user_messages add index i_user_msgs_isread (is_read);
alter table user_messages add index i_user_msgs_cdate (creation_date);

/*==============================================================*/
/* Table: message_code                                          */
/*==============================================================*/
CREATE TABLE message_code
(
   phone_number varchar(20) comment '手机号',
   msg_code varchar(20) comment '验证码',
   creation_date timestamp(3) not null comment '创建时间',
   expire_date timestamp(3) not null comment '失效时间',
   PRIMARY KEY (phone_number)
)
ENGINE InnoDB;
alter table message_code add index i_message_code_code (msg_code);
alter table message_code add index i_message_code_cdate (creation_date);


/*==============================================================*/
/* Table: users                                                 */
/*==============================================================*/
CREATE TABLE users
(
   user_id int auto_increment,
   user_name varchar(20) comment '用户名',
   phone_number varchar(20) unique comment '手机号',
   email varchar(50) comment '邮箱',
   idcard_no varchar(20) comment '身份证号',
   is_credit_card varchar(20) comment '银行卡类别',
   identity_type tinyint comment '身分标识',
   phone_service_password varchar(200) comment '手机营业厅密码',
   bank_name  varchar(50) comment '账户所属银行',
   bank_card_no  varchar(50) comment '银行卡号',
   credit_number int comment '信用分',
   is_verified tinyint comment '是否认证',
   avatar_pic varchar(1000) comment '头像图片名',
   nick_name varchar(50) unique comment '用户昵称',
   password varchar(250) comment '密码',
   creation_date timestamp(3) not null comment '创建时间',
   PRIMARY KEY (user_id)
)
ENGINE InnoDB;
alter table users add index i_users_phone_number (phone_number);
alter table users add index i_users_cdate (creation_date);


/*==============================================================*/
/* Table: user keys                                                */
/*==============================================================*/
CREATE TABLE user_keys
(
   user_key varchar(250) not null comment '用户API_KEY',
   user_id int comment '用户Id',
   application_id smallint(6) comment 'appId',
   oauth_client_id  varchar(50) NOT NULL DEFAULT '' comment 'oauth客户端Id',
   expiry timestamp(3) NOT NULL DEFAULT '2030-12-30 00:00:00',
   is_deleted tinyint not null default 0,
   PRIMARY KEY (user_key)
)
ENGINE InnoDB;
alter table user_keys add unique(user_id,application_id,oauth_client_id);
alter table user_keys add constraint fk_user_keys_user foreign key (user_id) references users(user_id);


/*==============================================================*/
/* Table: user_login_logs                                                */
/*==============================================================*/
CREATE TABLE user_login_logs
(
   user_id int comment '用户Id',
   platform varchar(20) comment 'IOS, Android, Web',
   remote_address varchar(20) comment '登录地址',
   login_time timestamp(3) NOT NULL comment '登录时间'
)
ENGINE InnoDB;
alter table user_keys add constraint fk_user_login_logs_user foreign key (user_id) references users(user_id);

/*==============================================================*/
/* Table: user_event_logs                                                */
/*==============================================================*/
CREATE TABLE user_event_logs
(
   user_id int comment '用户Id',
   source_id varchar(20) comment '渠道',
   event_name varchar(20) comment '事件名称',
   event_action varchar(20) comment '事件动作',
   event_label varchar(20) comment '事作标签',
   event_value varchar(20) comment '事件值',
   remote_address varchar(30) comment 'IP地址',
   creation_date timestamp(3) NOT NULL comment '创建时间'
)
ENGINE InnoDB;
alter table user_event_logs add index i_event_logs_user (user_id);
alter table user_event_logs add index i_event_logs_sid (source_id);
alter table user_event_logs add index i_event_logs_name (event_name);
alter table user_event_logs add index i_event_logs_action (event_action);
alter table user_event_logs add index i_event_logs_lable (event_label);
alter table user_event_logs add index i_event_logs_value (event_value);
alter table user_event_logs add index i_event_logs_cdate (creation_date);


/*==============================================================*/
/* Table: loan_platform_templates                               */
/*==============================================================*/
CREATE TABLE loan_platform_templates
(
   platform_id int comment '贷款平台Id',
   template_name varchar(20) comment '模板名称',
   phone_number varchar(20) comment '发送号码',
   content varchar(500) comment '模板内容',
   PRIMARY KEY (platform_id, template_name)
)
ENGINE InnoDB;
alter table loan_platform_templates add constraint i_loan_pl_templates_platforms foreign key (platform_id) references loan_platforms(platform_id);
alter table loan_platform_templates add index i_loan_pl_templates_phone (phone_number);

/*==============================================================*/
/* Table: loan_types                               */
/*==============================================================*/
CREATE TABLE loan_types
(
   loan_type_id int comment '借款类型编号',
   name varchar(20) comment '借款类型',
   description varchar(200) comment '描述',
   PRIMARY KEY (loan_type_id)
);

/*==============================================================*/
/* Table: billings                                  */
/*==============================================================*/
CREATE TABLE billings
(
   billing_id int not null auto_increment,
   user_id int comment '用户ID',
   platform_id int comment '贷款平台Id',
   order_id varchar(100) comment '平台订单ID',
   name varchar(100) comment '账单名称',
   b_amount decimal(15,2) comment '借款金额',
   b_date timestamp(3) comment '借款时间',
   loan_type_id int comment '借款类型:1-一次性还本付息，2-分期',
   repeats int comment '还款期数',
   start_no int comment '开始期数',
   p_amount decimal(15,2) comment '账单总需还款额',
   p_date timestamp(3) comment '每期应还时间',
   rate double comment '年化利率',
   description varchar(100) comment '账单备注信息',
   is_deleted tinyint comment '是否删除' default 0,
   PRIMARY KEY (billing_id)
)
ENGINE InnoDB;
alter table billings add constraint fk_billings_user foreign key (user_id) references users(user_id);
alter table billings add constraint fk_billings_platform foreign key (platform_id) references loan_platforms(platform_id);
alter table billings add index i_billing_user (user_id);
alter table billings add index i_billing_isdeleted (is_deleted);


/*==============================================================*/
/* Table: billing_details                                       */
/*==============================================================*/
CREATE TABLE billing_details
(
   billing_detail_id int not null auto_increment,
   billing_id int comment '账单ID',
   current_no int comment '当前期数',
   balance decimal(15,2) comment '应还金额',
   interest decimal(15,2) comment '利息和手续费',
   return_date timestamp(3) comment '应还日期',
   payment_sum decimal(15,2) comment '累计还款金额',
   last_payment_date timestamp(3) null comment '最近一次还款时间' default null,
   status tinyint not null comment '还款状态：0-未还、1-已还、2-部分已还' default 0,
   PRIMARY KEY (billing_detail_id)
)
ENGINE InnoDB;
alter table billing_details add index i_bdetails_id (billing_detail_id);
alter table billing_details add index i_bdetails_cno (current_no);


/*==============================================================*/
/* Table: payment_details                                       */
/*==============================================================*/
CREATE TABLE repayment_details
(
   repayment_id int not null auto_increment,
   billing_detail_id int comment '账单明细ID',
   amount decimal(15,2) not null comment '还款金额',
   repay_date timestamp(3) not null comment '还款日期' ,
   PRIMARY KEY (repayment_id)
)
ENGINE InnoDB;
alter table repayment_details add index i_bdetails_id (billing_detail_id);


/*==============================================================*/
/* Table: messages                                              */
/*==============================================================*/
CREATE TABLE messages
(
   message_id           int not null AUTO_INCREMENT comment 'ID',
   from_user_id         int not null default 0 comment '发送方ID， 0则为系统发送',
   to_user_id         	int not null default 0 comment '接收方ID',
   creation_date        timestamp not null default 0,
   send_date            timestamp not null default 0,
   subject              varchar(250),
   message_type         int,
   message_text         mediumtext,
   is_sms               tinyint not null default 0,
   is_push              tinyint not null default 0,
   parameters           varchar(4000),
   is_read              tinyint not null default 0,
   is_hidden            tinyint not null default 0,
   PRIMARY KEY (message_id)
)
ENGINE InnoDB;
alter table messages add index i_messages_fuid (from_user_id);
alter table messages add index i_messages_tuid (to_user_id);



