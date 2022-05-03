CREATE DATABASE ask_leave;
USE ask_leave;

CREATE TABLE `sys_student_user` (
                                    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `student_number` BIGINT(20) NOT NULL  COMMENT '学号',
                                    `password` VARCHAR(64) NOT NULL  COMMENT '密码',
                                    `phone_number` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
                                    `username`  VARCHAR(32) NOT NULL COMMENT '申请人姓名',
                                    `sex` TINYINT(1) DEFAULT NULL DEFAULT '1' COMMENT '用户性别（0男，1女，2未知）',
                                    `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `deleted` TINYINT(1) UNSIGNED DEFAULT '0' COMMENT '是否删除',
                                    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                            `menu_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '菜单名',
                            `perms` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
                            `deleted` TINYINT(1) UNSIGNED DEFAULT '0' COMMENT '是否删除',
                            PRIMARY KEY (`id`)
) ENGINE=INNODB  DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
                            `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                            `name` VARCHAR(128) DEFAULT NULL,
                            `have_perms` VARCHAR(100) DEFAULT NULL COMMENT '角色权限字符串',
                            `deleted` TINYINT(1) UNSIGNED DEFAULT '0' COMMENT '是否删除',
                            PRIMARY KEY (`id`)
) ENGINE=INNODB  DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
                                 `role_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                                 `menu_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '菜单id',
                                 PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
                                 `user_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                 `role_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '角色id',
                                 PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `leave_note` (
                              `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `student_number` BIGINT(20) NOT NULL COMMENT '学号',
                              `major_and_class` VARCHAR(128) NOT NULL COMMENT '班级',
                              `start_time` DATETIME  NOT NULL COMMENT '起始时间',
                              `end_time` DATETIME NOT NULL COMMENT '截止时间',
                              `leave` TINYINT(1) UNSIGNED DEFAULT '0' COMMENT '是否离开学校',
                              `destination` VARCHAR(128) NOT NULL COMMENT '目的地',
                              `dormitory_number` VARCHAR(128) NOT NULL COMMENT '宿舍号',
                              `way` VARCHAR(32) NOT NULL  COMMENT '交通方式',
                              `phone_number` VARCHAR(32) NOT NULL COMMENT '手机号',
                              `reason`  VARCHAR(255) NOT NULL COMMENT '请假理由',
                              `instructor_opinion` VARCHAR(255)  COMMENT '辅导员意见',
                              `institute_opinion` VARCHAR(255)  COMMENT '党委书记意见',
                              `dean_opinion` VARCHAR(255)  COMMENT '院长意见',
                              `deleted` TINYINT(1) UNSIGNED DEFAULT '0' COMMENT '是否删除',
                              PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='请假条';