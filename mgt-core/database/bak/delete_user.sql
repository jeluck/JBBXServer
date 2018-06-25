delete from mgt_user_apply_records where user_id =10087 ;

delete from mgt_user_loan_record_details 
where loan_id in (select loan_id from mgt_user_loan_records where user_id =10087 );

delete from mgt_user_loan_records where user_id =10087 ;

delete from mgt_user_keys where user_id =10087 ;

delete from mgt_users where user_id =10087 ;