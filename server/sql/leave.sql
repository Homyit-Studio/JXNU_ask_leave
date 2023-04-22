/*
SQLyog Professional v12.09 (64 bit)
MySQL - 5.7.36 : Database - ask_leave
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ask_leave` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `ask_leave`;

/*Table structure for table `back_note` */

DROP TABLE IF EXISTS `back_note`;

CREATE TABLE `back_note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `back` tinyint(1) DEFAULT '1' COMMENT '是否已经返校',
  `depart` tinyint(1) unsigned DEFAULT '1' COMMENT '是否已经离校',
  `depart_time` datetime DEFAULT NULL COMMENT '离开时间',
  `back_time` datetime DEFAULT NULL COMMENT '返校时间',
  `depart_way` varchar(255) DEFAULT NULL COMMENT '离开方式',
  `back_way` varchar(255) DEFAULT NULL COMMENT '返校方式',
  `status` tinyint(1) DEFAULT '1' COMMENT '销假完成',
  `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97360 DEFAULT CHARSET=utf8mb4 COMMENT='销假条';



DROP TABLE IF EXISTS `images_note`;

CREATE TABLE `images_note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `note_id` bigint(20) DEFAULT NULL COMMENT '假条',
  `url` varchar(255) NOT NULL COMMENT '图片路径',
  `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COMMENT='假条上传的图片';


DROP TABLE IF EXISTS `leave_note`;

CREATE TABLE `leave_note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `class_id` bigint(20) DEFAULT NULL COMMENT '班级',
  `grade_id` bigint(20) DEFAULT NULL COMMENT '年级',
  `student_number` bigint(20) NOT NULL COMMENT '学号',
  `major_and_class` varchar(128) NOT NULL COMMENT '班级',
  `username` varchar(32) NOT NULL COMMENT '学院',
  `start_time` datetime NOT NULL COMMENT '起始时间',
  `examine` tinyint(1) DEFAULT '0' COMMENT '审核状态',
  `level` tinyint(1) DEFAULT NULL COMMENT '审核等级',
  `end_time` datetime NOT NULL COMMENT '截止时间',
  `days` varchar(32) DEFAULT NULL COMMENT '请假时长',
  `depart` tinyint(1) DEFAULT '0' COMMENT '是否离开学校',
  `destination` varchar(128) NOT NULL COMMENT '目的地',
  `dormitory_number` varchar(128) NOT NULL COMMENT '宿舍号',
  `leader_number` varchar(255) DEFAULT NULL COMMENT '负责人号码列表',
  `way` varchar(32) NOT NULL COMMENT '交通方式',
  `phone_number` varchar(32) NOT NULL COMMENT '手机号',
  `status` tinyint(1) DEFAULT '1' COMMENT '销假是否完成',
  `reason` varchar(255) NOT NULL COMMENT '请假理由',
  `instructor_opinion` varchar(255) DEFAULT NULL COMMENT '辅导员意见',
  `secretary_opinion` varchar(255) DEFAULT NULL COMMENT '党委书记意见',
  `dean_opinion` varchar(255) DEFAULT NULL COMMENT '院长意见',
  `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97361 DEFAULT CHARSET=utf8mb4 COMMENT='请假条';

/*Table structure for table `sys_class_student` */

DROP TABLE IF EXISTS `sys_class_student`;

CREATE TABLE `sys_class_student` (
  `class_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '班级ID',
  `student_number` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单id',
  `deleted` tinyint(1) unsigned DEFAULT '0',
  PRIMARY KEY (`class_id`,`student_number`)
) ENGINE=InnoDB AUTO_INCREMENT=1006 DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_class_student` */


/*Table structure for table `sys_class_teacher` */

DROP TABLE IF EXISTS `sys_class_teacher`;

CREATE TABLE `sys_class_teacher` (
  `class_id` bigint(20) NOT NULL COMMENT '班级ID',
  `student_number` bigint(20) NOT NULL DEFAULT '0' COMMENT '工号',
  `deleted` tinyint(1) unsigned DEFAULT '0',
  PRIMARY KEY (`class_id`,`student_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '菜单名',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';


/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `have_perms` varchar(100) DEFAULT NULL COMMENT '角色权限字符串',
  `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';



/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单id',
  `deleted` tinyint(1) unsigned DEFAULT '0',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;



/*Table structure for table `sys_student_class_info` */

DROP TABLE IF EXISTS `sys_student_class_info`;

CREATE TABLE `sys_student_class_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '班级编号',
  `capacity` int(3) DEFAULT NULL COMMENT '班级容量',
  `grade_id` bigint(20) DEFAULT NULL COMMENT '年级',
  `major_and_class` varchar(128) NOT NULL COMMENT '班级名称',
  `high_user` varchar(32) DEFAULT NULL COMMENT '辅导员',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=462 DEFAULT CHARSET=utf8mb4 COMMENT='班级信息表';


/*Table structure for table `sys_student_user` */

DROP TABLE IF EXISTS `sys_student_user`;

CREATE TABLE `sys_student_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `grade_id` bigint(20) DEFAULT NULL COMMENT '年级id',
  `class_id` bigint(20) DEFAULT NULL COMMENT '班级id',
  `role` tinyint(1) DEFAULT '0' COMMENT '角色',
  `high_user` varchar(32) DEFAULT NULL COMMENT '上级',
  `username` varchar(32) NOT NULL COMMENT '申请人姓名',
  `id_number` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `nation` varchar(32) DEFAULT NULL COMMENT '民族',
  `student_number` bigint(20) NOT NULL COMMENT '学号',
  `sex` tinyint(1) DEFAULT '1' COMMENT '用户性别（0男，1女，2未知）',
  `native_place` varchar(64) DEFAULT NULL COMMENT '籍贯',
  `home_address` varchar(255) DEFAULT NULL COMMENT '家庭住址',
  `major_and_class` varchar(32) DEFAULT NULL COMMENT '专业班级',
  `phone_number` varchar(32) DEFAULT NULL COMMENT '手机号',
  `parent_number` varchar(32) DEFAULT NULL COMMENT '家长联系方式',
  `building_number` varchar(32) DEFAULT NULL COMMENT '楼栋号码',
  `dormitory_number` varchar(32) DEFAULT NULL COMMENT '宿舍号',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `student_number` (`student_number`)
) ENGINE=InnoDB AUTO_INCREMENT=12974 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


/*Table structure for table `sys_user_log` */

DROP TABLE IF EXISTS `sys_user_log`;

CREATE TABLE `sys_user_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) DEFAULT NULL COMMENT '姓名',
  `student_number` bigint(20) DEFAULT NULL COMMENT '账号',
  `module_code` varchar(255) NOT NULL DEFAULT '' COMMENT '操作模块',
  `type` tinyint(2) NOT NULL COMMENT '操作类型',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `operate_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `content` varchar(255) NOT NULL DEFAULT '' COMMENT '操作内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;


/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `student_number` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色id',
  `deleted` tinyint(1) unsigned DEFAULT '0',
  PRIMARY KEY (`student_number`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=652923199810232224 DEFAULT CHARSET=utf8mb4;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
