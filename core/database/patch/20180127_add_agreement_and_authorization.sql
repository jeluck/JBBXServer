-- drop table if exists
DROP TABLE IF EXISTS agreement;
DROP TABLE IF EXISTS user_privs;
/*==============================================================*/
/* Table: agreement   协议表                                              					*/
/*==============================================================*/
CREATE TABLE `agreement` (
  `userid` int(11) NOT NULL COMMENT '用户id',
  `agreement` varchar(20) NOT NULL COMMENT '协议',
  `version` varchar(20) NOT NULL COMMENT '版本',
  `read_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '阅读时间',
  PRIMARY KEY (`userid`,`agreement`,`version`),
  CONSTRAINT `fk_authorization_userId_users_userId` FOREIGN KEY (`userid`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*==============================================================*/
/* Table: authorization   权限表                                 */
/*==============================================================*/
CREATE TABLE user_privs (
  apply_user_id int(11) NOT NULL COMMENT '申请人ID',
  user_id int(11) NOT NULL COMMENT '授权方ID',
  priv_name varchar(20) NOT NULL COMMENT '权限：qq, wechat,phone,info',
  priv_value tinyint(4) DEFAULT '0' COMMENT '权限值：0无权限，1有权限',
  update_date datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (apply_user_id, user_id, priv_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
