-- drop table user_verify_result;
CREATE TABLE user_verify_result
(
   user_id int not null comment '用户ID',
   verify_type varchar(20) not null comment '难证类型',
   verify_step int(4) not null comment '验证步骤',
   verified tinyint not null comment '是否验证通过，1是，0否',
   creation_date timestamp(3) not null comment '创建时间',
   update_date timestamp(3) not null comment '更新时间',
   PRIMARY KEY (user_id, verify_type,verify_step)
)

ENGINE InnoDB;