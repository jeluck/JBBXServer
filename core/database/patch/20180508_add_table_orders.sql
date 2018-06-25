CREATE TABLE `orders` (
  `order_no` varchar(200) NOT NULL,
  `product_name` varchar(20) ,
  `total_fee` int(11) not null default 0,
  `IP_Address` varchar(32),
  `pay_type` varchar(10) not null ,
  `creation_date`  timestamp not null default CURRENT_TIMESTAMP,
  `pay_date` timestamp null default null,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`order_no`)
) ENGINE=InnoDB;


CREATE TABLE `user_product_count` (
  `user_id` int(11) NOT NULL,
  `product_name` varchar(20) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`product_name`)
) ENGINE=InnoDB;