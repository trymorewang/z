/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : z

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2020-10-12 17:00:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `goods_name` varchar(16) DEFAULT NULL COMMENT '商品名称',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `goods_stock` int(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for miaosha_goods
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_goods`;
CREATE TABLE `miaosha_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品表',
  `goods_id` bigint(20) NOT NULL COMMENT '商品Id',
  `miaosha_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '秒杀价',
  `stock_count` int(11) NOT NULL COMMENT '库存数量',
  `version` bigint(20) NOT NULL COMMENT '版本号',
  `start_date` timestamp NULL DEFAULT NULL COMMENT '秒杀开始时间',
  `end_date` timestamp NULL DEFAULT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for miaosha_order
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_order`;
CREATE TABLE `miaosha_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `order_id` varchar(100) NOT NULL COMMENT '订单ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '1：已支付 2：未支付',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_uid_gid` (`user_id`,`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1682 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for miaosha_user
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_user`;
CREATE TABLE `miaosha_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID，手机号码',
  `mobile` bigint(11) NOT NULL COMMENT '手机号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18912341261 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

