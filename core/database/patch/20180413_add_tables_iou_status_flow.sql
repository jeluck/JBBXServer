/*==============================================================*/
/* Table: iou_status_control                                    */
/* user:  Vincent Tang                                          */
/* time: 2018-4-13                                              */
/*==============================================================*/
CREATE TABLE iou_status_control
(
   pre_status tinyint(4) not null comment '当前状态',
   new_status tinyint(4) not null comment '新状态',
   borrower_ctrl tinyint not null comment '借款方能否变动，1是，0否',
   lender_ctrl tinyint not null comment '出借方能否变动，1是，0否',
   PRIMARY KEY (pre_status, new_status)
)
ENGINE InnoDB;

insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (0,1,1,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (0,2,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (1,5,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (1,8,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (1,11,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (1,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (2,1,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (2,3,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (2,4,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (5,6,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (5,7,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (5,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (6,5,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (6,8,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (6,11,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (6,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (7,5,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (7,8,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (7,11,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (7,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (8,9,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (8,10,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (8,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (9,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (10,5,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (10,8,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (10,11,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (10,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (11,12,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (11,13,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (11,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (12,5,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (12,8,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (12,11,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (12,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (13,5,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (13,8,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (13,11,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (13,14,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (20,21,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (20,1,0,1);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (21,20,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (30,31,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (30,1,1,0);
insert into iou_status_control ( pre_status, new_status, borrower_ctrl, lender_ctrl) values (31,30,0,1);


