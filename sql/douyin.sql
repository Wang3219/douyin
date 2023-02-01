/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : douyin

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 01/02/2023 11:04:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `follow_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of follow
-- ----------------------------
BEGIN;
INSERT INTO `follow` (`id`, `follow_id`, `user_id`) VALUES (1, 18, 17);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `user_avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像',
  `follow_count` int DEFAULT NULL COMMENT '粉丝总数',
  `follower_count` int DEFAULT NULL COMMENT '关注总数',
  `status_code` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户状态',
  `status_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态信息',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`user_id`, `username`, `password`, `user_avatar`, `follow_count`, `follower_count`, `status_code`, `status_msg`) VALUES (17, 'b', '$2a$10$98qlKUvaqhBUFxJqsRkYi.xOSo2uk2ac.C0uQEQ91irFQNKfxqgLq', 'q', 2, 4, '1', 'fd');
INSERT INTO `user` (`user_id`, `username`, `password`, `user_avatar`, `follow_count`, `follower_count`, `status_code`, `status_msg`) VALUES (18, 'n', '$2a$10$bHP5FHLBhuI2MyeG8Hop2envxQQuStDQKWp90ahz9MR.xPE/te3hy', 'q', 3, 5, '6', 'fe');
INSERT INTO `user` (`user_id`, `username`, `password`, `user_avatar`, `follow_count`, `follower_count`, `status_code`, `status_msg`) VALUES (20, 'd', '$2a$10$ZTaNQKZXN36ADKElXll2duMY8uw4gMsG5e93b0c3TJPBPBDPkjjLK', NULL, 0, 0, NULL, NULL);
INSERT INTO `user` (`user_id`, `username`, `password`, `user_avatar`, `follow_count`, `follower_count`, `status_code`, `status_msg`) VALUES (21, 'aa', '$2a$10$.RSEmi9XdGuABuXptVPyPupzl2zTYzryFUylg4VwzkLSMbaJVZmha', NULL, 0, 0, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
