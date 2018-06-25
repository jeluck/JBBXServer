insert users(user_id, phone_number, user_name, nick_name, password) values(1000000, '18575511205', 'VincentTang', 'VT','$argon2d$v=19$m=512,t=2048,p=1$8lBaabEQ9UqlDBsDvZ8pig$MelmhwDB/+guhKqoqqb+HFEBGSoO+sk+/Hki80QHVzY');

-- ious测试数据
INSERT INTO `ious`(`iou_code`,`borrower_id`,`lender_id`,`borrowing_amount`,`annual_rate`,`purpose`) VALUE('TESTIOU201801201218',1000000,1000002,30000,0.12,'医疗保健')

-- iou_followers测试数据
INSERT INTO `iou_followers`(`user_id`,`iou_code`) VALUES(1000000,'TESTIOU201801201218')

-- intention_record测试数据
INSERT INTO `iou_intentional_users`(`user_id`,`iou_code`) VALUES(1000000,'TESTIOU201801201218')

-- operation_record测试数据
INSERT INTO `iou_operation_records`(`iou_code`,`from_user`,`to_user`,`op_name`,`params`) VALUES('TESTIOU201801201218',1000000,1000002,'修改借条',NULL)

-- agreement表测试数据
INSERT INTO `agreement`(`user_id`,`agreement`,`version`) VALUES(1000000,'注册协议','1.0')

-- authorization表测试数据
INSERT INTO `authorization`(`key`) VALUES('1000000:1000003')