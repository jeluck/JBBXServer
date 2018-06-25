-- 权限表描述
insert into mgt_permissions (permission_id, description) values (1, '系统权限');
insert into mgt_permissions (permission_id, description) values (2, '组织管理权限 ');
insert into mgt_permissions (permission_id, description) values (3, '经营报表');
insert into mgt_permissions (permission_id, description) values (4, '员工报表');
insert into mgt_permissions (permission_id, description) values (5, '充值');
insert into mgt_permissions (permission_id, description) values (6, '账户设置');
insert into mgt_permissions (permission_id, description) values (7, '渠道后台');
insert into mgt_permissions (permission_id, description) values (8, '渠道管理');
insert into mgt_permissions (permission_id, description) values (9, '帮帮导流');
insert into mgt_permissions (permission_id, description) values (10, '分配');
insert into mgt_permissions (permission_id, description) values (11, '电销号码处理');
insert into mgt_permissions (permission_id, description) values (12, '初审');
insert into mgt_permissions (permission_id, description) values (13, '复审');
insert into mgt_permissions (permission_id, description) values (14, '打借条');
insert into mgt_permissions (permission_id, description) values (15, '放款记账');
insert into mgt_permissions (permission_id, description) values (16, '贷后管理');
insert into mgt_permissions (permission_id, description) values (17, '催收');

-- 角色表
insert into mgt_roles (role_id, description) values (1, '系统管理员');
insert into mgt_roles (role_id, description) values (2, '组织管理员');
insert into mgt_roles (role_id, description) values (3, '公司财务');
insert into mgt_roles (role_id, description) values (4, '市场员');
insert into mgt_roles (role_id, description) values (5, '分配员');
insert into mgt_roles (role_id, description) values (6, '初审员');
insert into mgt_roles (role_id, description) values (7, '复审员');
-- insert into mgt_roles (role_id, description) values (8, '审核员');
insert into mgt_roles (role_id, description) values (9, '放款员');
insert into mgt_roles (role_id, description) values (10, '催收员');
insert into mgt_roles (role_id, description) values (11, '渠道号');

-- 角色权限表
-- 系统管理员
insert into mgt_role_permissions(role_id, permission_id) values(1, 1);
-- 组织管理员
insert into mgt_role_permissions(role_id, permission_id) values(2, 2);
insert into mgt_role_permissions(role_id, permission_id) values(2, 6);
-- 公司财务
insert into mgt_role_permissions(role_id, permission_id) values(3, 3);
insert into mgt_role_permissions(role_id, permission_id) values(3, 4);
insert into mgt_role_permissions(role_id, permission_id) values(3, 5);
-- 市场员
insert into mgt_role_permissions(role_id, permission_id) values(4, 7);
insert into mgt_role_permissions(role_id, permission_id) values(4, 8);
-- 分配员
insert into mgt_role_permissions(role_id, permission_id) values(5, 10);
insert into mgt_role_permissions(role_id, permission_id) values(5, 11);
-- 初审员
insert into mgt_role_permissions(role_id, permission_id) values(6, 12);
-- 复审员
insert into mgt_role_permissions(role_id, permission_id) values(7, 13);
insert into mgt_role_permissions(role_id, permission_id) values(7, 14);
insert into mgt_role_permissions(role_id, permission_id) values(7, 16);
-- 审核员
-- insert into mgt_role_permissions(role_id, permission_id) values(8, 12);
-- insert into mgt_role_permissions(role_id, permission_id) values(8, 13);
-- insert into mgt_role_permissions(role_id, permission_id) values(8, 14);
-- insert into mgt_role_permissions(role_id, permission_id) values(8, 16);
-- 放款员
insert into mgt_role_permissions(role_id, permission_id) values(9, 15);
insert into mgt_role_permissions(role_id, permission_id) values(9, 14);
insert into mgt_role_permissions(role_id, permission_id) values(9, 16);
-- 催收员
insert into mgt_role_permissions(role_id, permission_id) values(10, 17);

-- 流量基本配置
insert into mgt_dflow_base(dflow_id, jexl_script, jexl_desc, price, creation_date) values(1, null, '随机', 400, now());
insert into mgt_dflow_base(dflow_id, jexl_script, jexl_desc, price, creation_date) values(2, 'if (score>=580){return true} else {return false}', '580以上', 800, now());
insert into mgt_dflow_base(dflow_id, jexl_script, jexl_desc, price, creation_date) values(3, 'if (score>=600){return true} else {return false}', '600以上', 1000, now());
insert into mgt_dflow_base(dflow_id, jexl_script, jexl_desc, price, creation_date) values(4, 'if (score>=630){return true} else {return false}', '630以上', 1000, now());


insert into `mgt_iou_platforms` (  `iou_platform_id`, `platform_name`, `description`) values (1, '借帮帮', null);
insert into `mgt_iou_platforms` ( `iou_platform_id`, `platform_name`, `description`) values ( 2, '借贷宝', null);
insert into `mgt_iou_platforms` ( `iou_platform_id`, `platform_name`, `description`) values ( 3, '今借到', null);
insert into `mgt_iou_platforms` ( `iou_platform_id`, `platform_name`, `description`) values ( 4, '无忧', null);
insert into `mgt_iou_platforms` ( `iou_platform_id`, `platform_name`, `description`) values ( 5, '米房', null);
insert into `mgt_iou_platforms` ( `iou_platform_id`, `platform_name`, `description`) values ( 6, '友凭证', null);
insert into `mgt_iou_platforms` ( `iou_platform_id`, `platform_name`, `description`) values ( 99 , '其他', null);
