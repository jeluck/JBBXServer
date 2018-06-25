CREATE TABLE `user_devices` (
  `object_id` varchar(255),
  `user_id` int(10) not null,
  `device_type` varchar(20) not null,
  `installation_id` varchar(255),
  `device_token` varchar(255),
  `creation_date` timestamp not null default CURRENT_TIMESTAMP,
  `update_date`  timestamp null default null,
  `status` tinyint DEFAULT '0',
  PRIMARY KEY (`object_id`)
) ENGINE=InnoDB;
alter table user_devices add index i_user_devices_uid (user_id);
alter table user_devices add index i_user_devices_status (status);