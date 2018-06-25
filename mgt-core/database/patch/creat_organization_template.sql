-- 0608
/*==============================================================*/
/* 创建组织  103 陆朝晖  瀚铂钱庄 1141775*/
/*==============================================================*/
INSERT INTO mgt_organizations(org_id,name,description,deleted,count,sms_sign_name,sms_template_id)
       VALUES('103','瀚铂钱庄','瀚铂钱庄','0','30','【借帮帮】','SMS_121165611');
INSERT INTO mgt_accounts(account_id,username,phone_number,jbb_user_id,org_id,creator,nickname,password)
       VALUES('10005','haobo','','1141775','103','1','陆朝晖'
              ,'$argon2d$v=19$m=512,t=2048,p=1$1CChY7mmA4vWrAggvMVNag$gmv3wwi53WF7YcQ8/IulSMij11uUrYUebogxiGNW6W8');
INSERT INTO mgt_account_roles(account_id,role_id)VALUES('10005','2');
INSERT INTO mgt_org_recharges (org_id,total_data_amount,total_sms_amount,total_data_expense,total_sms_expense)
       VALUES('103','10000','10000','0','0');
       
 
-- 0609
/*==============================================================*/
/* 创建组织  104 朱新宇  贷贷乐   1095681*/
/*==============================================================*/       
INSERT INTO mgt_organizations(org_id,name,description,deleted,count,sms_sign_name,sms_template_id)
       VALUES('104','贷贷乐','贷贷乐','0','30','【借帮帮】','SMS_121165611');
INSERT INTO mgt_accounts(account_id,username,phone_number,jbb_user_id,org_id,creator,nickname,password)
       VALUES('10006','Daidaile','','1095681','104','1','朱新宇'
              ,'$argon2d$v=19$m=512,t=2048,p=1$1CChY7mmA4vWrAggvMVNag$gmv3wwi53WF7YcQ8/IulSMij11uUrYUebogxiGNW6W8');
INSERT INTO mgt_account_roles(account_id,role_id)VALUES('10006','2');
INSERT INTO mgt_org_recharges (org_id,total_data_amount,total_sms_amount,total_data_expense,total_sms_expense)
       VALUES('104','10000','10000','0','0');
       