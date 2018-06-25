
ALTER TABLE ious ADD COLUMN location varchar(100);


CREATE TABLE user_apply_records (
  user_id int(11) NOT NULL COMMENT '申请人ID',
  target_user_id int(11) NOT NULL COMMENT '被申请人ID',
  creation_date timestamp(3) not null comment '创建时间',
  PRIMARY KEY (`user_id`,`target_user_id`),
  CONSTRAINT `fk_uah_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `fk_uah_target_user_id` FOREIGN KEY (`target_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

update info set db_version =3;


CREATE TABLE user_apply_records_history (
  user_id int(11) NOT NULL COMMENT '申请人ID',
  target_user_id int(11) NOT NULL COMMENT '被申请人ID',
  creation_date timestamp(3) not null comment '创建时间',
  PRIMARY KEY (`user_id`,`target_user_id`),
  CONSTRAINT `fk_uah_user_h_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `fk_uah_target_user_h_id` FOREIGN KEY (`target_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;