CREATE TABLE `system_properties` (
  `name` varchar(50) NOT NULL COMMENT '属性名',
  `value` varchar(5000) DEFAULT NULL COMMENT '属性值',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

update info set db_version =5;

update info set props_version =1;
