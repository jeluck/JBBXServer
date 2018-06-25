-- drop table if exists
DROP TABLE IF EXISTS ious;
DROP TABLE IF EXISTS iou_intentional_users;
DROP TABLE IF EXISTS iou_followers;
DROP TABLE IF EXISTS iou_operation_records;
/*==============================================================*/
/* Table: ious   借条表                                              						*/
/*==============================================================*/

CREATE TABLE `ious` (
  `iou_code` varchar(50) NOT NULL COMMENT '借条编码',
  `borrower_id` int(11) DEFAULT NULL COMMENT '借款人id',
  `lender_id` int(11) DEFAULT NULL COMMENT '出借人id',
  `borrowing_amount` decimal(15,2) DEFAULT NULL COMMENT '借款金额',
  `borrowing_date` timestamp(3) NOT NULL DEFAULT '0000-00-00 00:00:00.000' COMMENT '借款日期',
  `repayment_date` timestamp(3) NOT NULL DEFAULT '0000-00-00 00:00:00.000' COMMENT '还款日期',
  `extension_date` timestamp NULL DEFAULT NULL COMMENT '延期还款日期',
  `annual_rate` double DEFAULT NULL COMMENT '年化利率',
  `purpose` varchar(50) DEFAULT NULL COMMENT '借款用途',
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借条创建日期',
  `effective_date` timestamp NULL DEFAULT NULL COMMENT '借条生效日期',
  `status` tinyint(4) DEFAULT '0' COMMENT '借条状态：0发布，1生效，2修改申请，3 保留状态，4 保留状态，5申请延期，6出借人确认延期，7出借人拒绝延期，8申请已还，9出借人确认已还，10出借人拒绝已还',
  `last_update_status_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新状态变动时间',
  `is_lender_deleted` tinyint(4) DEFAULT '0' COMMENT '出借用户是否删除：0未删除，1已删除',
  `device` varchar(100) DEFAULT NULL COMMENT '设备厂商型号',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '借款用户是否删除：0未删除，1已删除',
  PRIMARY KEY (`iou_code`),
  KEY `fk_borrower_id_users_user_id` (`borrower_id`),
  KEY `fk_lender_id_users_user_id` (`lender_id`),
  CONSTRAINT `fk_borrower_id_users_user_id` FOREIGN KEY (`borrower_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `fk_lender_id_users_user_id` FOREIGN KEY (`lender_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*==============================================================*/
/* Table:intention_record  意向出借表                            			   		*/
/*==============================================================*/
CREATE TABLE `iou_intentional_users` (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `iou_code` varchar(50) NOT NULL COMMENT '借条编码',
  `status` tinyint(4) DEFAULT '0' COMMENT '意向状态：0有意向，1 被拒绝，2自己取消意向，3 借款方修改并选定出借人， 4 接受借条修改， 5 拒绝借条修改',
  `update_date` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '操作时间',
  PRIMARY KEY (`user_id`,`iou_code`),
  KEY `fk_intention_record_iou_iou_code` (`iou_code`),
  CONSTRAINT `fk_intention_record_iou_iou_code` FOREIGN KEY (`iou_code`) REFERENCES `ious` (`iou_code`),
  CONSTRAINT `fk_intention_record_users_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*==============================================================*/
/* Table:iou_follows   关注表                            							*/
/*==============================================================*/
CREATE TABLE `iou_followers` (
  `user_id` int(11) NOT NULL COMMENT '关注人id',
  `iou_code` varchar(50) NOT NULL COMMENT '借条编码',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：0未关注，1已关注',
  `update_date` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`user_id`,`iou_code`),
  KEY `fk_attention_record_iou_iou_code` (`iou_code`),
  CONSTRAINT `fk_attention_record_iou_iou_code` FOREIGN KEY (`iou_code`) REFERENCES `ious` (`iou_code`),
  CONSTRAINT `fk_attention_record_users_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*==============================================================*/
/* Table:iou_operation_records   操作记录                    					*/
/*==============================================================*/
CREATE TABLE `iou_operation_records` (
  `iou_code` varchar(50) NOT NULL COMMENT '借条编码',
  `from_user` int(11) DEFAULT NULL COMMENT '操作人',
  `to_user` int(11) DEFAULT NULL COMMENT '操作对象人',
  `op_name` varchar(20) NOT NULL COMMENT '操作名称',
  `op_date` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '操作时间',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
