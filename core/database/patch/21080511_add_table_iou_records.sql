CREATE TABLE `iou_alert_records` (
  `record_id` int(11) not null AUTO_INCREMENT,
  `iou_code` varchar(255) not null ,
  `creation_date` timestamp not null default CURRENT_TIMESTAMP,
  `message_type` int(10) NOT NULL,
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB ;

alter table iou_alert_records add index i_iou_alert_records_icode (iou_code);
alter table iou_alert_records add index i_iou_alert_records_uid_cdate (creation_date);