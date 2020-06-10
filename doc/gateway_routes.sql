/*
 Navicat Premium Data Transfer

 Source Server         : 10.28.10.116
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 10.28.10.116:3306
 Source Schema         : dili-gateway

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 20/05/2020 09:25:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gateway_routes
-- ----------------------------
DROP TABLE IF EXISTS `gateway_routes`;
CREATE TABLE `gateway_routes`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `route_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由id',
  `route_uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '转发目标uri',
  `route_order` int(11) NOT NULL DEFAULT 0 COMMENT '路由顺序',
  `predicates` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '断言字符串',
  `filters` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过滤器字符串',
  `enabled` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gateway_routes
-- ----------------------------
INSERT INTO `gateway_routes` VALUES (1, 'customer-service', 'lb://customer-service', 1, '[{\"name\":\"Path\", \"args\":{\"pattern\": \"/customer-service/**\"}}]', '[{\"name\":\"StripPrefix\", \"args\": {\"_genkey_0\": \"1\"}},\r\n{\"name\":\"Hystrix\",\"args\":{\"name\":\"customer-service-fallback\",\"fallbackUri\":\"forward:/fallback\"}}]', 1, '2020-05-07 21:25:59', '2020-05-19 21:20:29', 1);
INSERT INTO `gateway_routes` VALUES (8, 'default-service', 'lb://customer-service', 10, '[{\"name\":\"Path\", \"args\":{\"pattern\": \"/api/**\"}}]', '[{\"name\":\"Hystrix\",\"args\":{\"name\":\"default-service-fallback\",\"fallbackUri\":\"forward:/fallback\"}}]', 1, '2020-05-13 22:56:19', '2020-05-19 21:29:42', 1);

SET FOREIGN_KEY_CHECKS = 1;
