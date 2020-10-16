/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : z

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 17/09/2020 23:55:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_member
-- ----------------------------
DROP TABLE IF EXISTS `sys_member`;
CREATE TABLE `sys_member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态 PROHIBIT：禁用   NORMAL：正常',
  PRIMARY KEY (`member_id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_member
-- ----------------------------
BEGIN;
INSERT INTO `sys_member` VALUES (1, 'admin', '$2a$10$5T851lZ7bc2U87zjt/9S6OkwmLW62tLeGLB2aCmq3XRZHA7OI7Dqa', 'NORMAL');
INSERT INTO `sys_member` VALUES (2, 'user', '$2a$10$szHoqQ64g66PymVJkip98.Fap21Csy8w.RD8v5Dhq08BMEZ9KaSmS', 'NORMAL');
COMMIT;

-- ----------------------------
-- Table structure for sys_member_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_member_menu`;
CREATE TABLE `sys_member_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色与权限关系表';

-- ----------------------------
-- Records of sys_member_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_member_menu` VALUES (1, 1, 1);
INSERT INTO `sys_member_menu` VALUES (2, 1, 2);
INSERT INTO `sys_member_menu` VALUES (3, 1, 3);
INSERT INTO `sys_member_menu` VALUES (4, 2, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_member_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_member_role`;
CREATE TABLE `sys_member_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户与角色关系表';

-- ----------------------------
-- Records of sys_member_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_member_role` VALUES (1, 1, 1);
INSERT INTO `sys_member_role` VALUES (2, 2, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名称',
  `permission` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限标识',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, '查看用户信息', 'sys:user:info');
INSERT INTO `sys_menu` VALUES (2, '查看所有权限', 'sys:menu:info');
INSERT INTO `sys_menu` VALUES (3, '查看所有角色', 'sys:role:info');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, 'ADMIN');
INSERT INTO `sys_role` VALUES (2, 'USER');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色与权限关系表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (1, 1, 1);
INSERT INTO `sys_role_menu` VALUES (2, 1, 2);
INSERT INTO `sys_role_menu` VALUES (3, 1, 3);
INSERT INTO `sys_role_menu` VALUES (4, 2, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
