ALTER TABLE users ADD COLUMN role_id INT;

ALTER TABLE users ADD COLUMN source_id varchar(30);
ALTER TABLE users ADD COLUMN platform varchar(30);


/*==============================================================*/
/* Table: china_region_codes   区域划分                                         				*/
/*==============================================================*/
CREATE TABLE `china_region_codes` (
  `code` varchar(6) NOT NULL COMMENT '代码',
  `province` varchar(20)   COMMENT '省',
  `city` varchar(20)  COMMENT '市、区',
  `area` varchar(20)  COMMENT '县',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*==============================================================*/
/* Table: roles                                                 */
/*==============================================================*/
CREATE TABLE roles
(
   role_id              int not null AUTO_INCREMENT,
   role_name            varchar(250),
   role_description     varchar(250),
   PRIMARY KEY (role_id)
)
ENGINE InnoDB DEFAULT CHARSET=utf8;

/*==============================================================*/
/* Table: role_permissions                                      */
/*==============================================================*/
CREATE TABLE role_permissions
(
   role_id              int not null,
   permission           int not null,
   PRIMARY KEY (role_id, permission)
)
ENGINE InnoDB DEFAULT CHARSET=utf8;


-- user roles
insert into roles (role_id, role_name)
values (1, 'System Administrator');
insert into role_permissions (role_id, permission) values (1, 0);
insert into role_permissions (role_id, permission) values (1, 1);
insert into role_permissions (role_id, permission) values (1, 2);

insert into roles (role_id, role_name)
values (2, 'Organization Administrator');
insert into role_permissions (role_id, permission) values (2, 2);

update info set db_version =2;
