ALTER TABLE user_apply_records ADD COLUMN reason varchar(50);
ALTER TABLE user_apply_records ADD COLUMN reason_desc varchar(50);

alter table user_apply_records add index i_uar_reason (reason);