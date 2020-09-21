# dili-gateway-parent
分支: master
# 1 网关
##  1.1 GIT
网关和路由使用同一个git地址
http://git3.nong12.com/middle-platform/dili-gateway-parent.git

## 1.2 域名和端口
域名:gateway.diligrp.com
端口:8285

## 1.3 dili-gateway.properties配置
**已经完成线上配置**
```
server.port=8285
redis.enable=true

spring.application.name=dili-gateway

# 跨域配置
spring.cloud.gateway.globalcors.cors-configurations.'[/**]'.allowed-origins="*"
spring.cloud.gateway.globalcors.cors-configurations.'[/**]'.allowed-methods[0]=GET
spring.cloud.gateway.globalcors.cors-configurations.'[/**]'.allowed-methods[1]=POST
spring.cloud.gateway.globalcors.cors-configurations.'[/**]'.allowed-headers="*"
spring.cloud.gateway.globalcors.cors-configurations.'[/**]'.allow-credentials=true

#spring cloud
#nacos.discovery
spring.cloud.nacos.discovery.group=MICROSERVICE
spring.cloud.nacos.discovery.service=gateway-service

#logback
logging.config=classpath:conf/logback-springboot.xml

spring.cloud.gateway.enabled=true
spring.cloud.sentinel.enabled=true
spring.cloud.sentinel.filter.enabled=false
spring.cloud.sentinel.eager=true
spring.cloud.sentinel.transport.heartbeat-interval-ms=2000

#在使用 Endpoint 特性之前需要在 Maven 中添加 spring-boot-starter-actuator 依赖，并在配置中允许 Endpoints 的访问。
#暴露的 endpoint 路径为 /actuator/sentinel
management.endpoints.web.exposure.include=*

#spring.cloud.sentinel.transport.dashboard=localhost:8088
spring.cloud.sentinel.datasource.ds.nacos.server-addr=nacos.diligrp.com:8848
spring.cloud.sentinel.datasource.ds.nacos.dataId=dili-gateway-sentinel.json
spring.cloud.sentinel.datasource.ds.nacos.data-type=json
spring.cloud.sentinel.datasource.ds.nacos.groupId=SENTINEL_GROUP
spring.cloud.sentinel.datasource.ds.nacos.namespace=54c39cfe-d1c4-4022-a94b-a3486c5927fc
spring.cloud.sentinel.datasource.ds.nacos.ruleType=gw_flow
spring.cloud.sentinel.scg.fallback.response-body={"code":403,"message":"服务开启限流保护,请稍后再试!"}
spring.cloud.sentinel.scg.fallback.mode=response

spring.cloud.sentinel.datasource.ds2.nacos.server-addr=nacos.diligrp.com:8848
spring.cloud.sentinel.datasource.ds2.nacos.dataId=dili-gateway-sentinel-system.json
spring.cloud.sentinel.datasource.ds2.nacos.data-type=json
spring.cloud.sentinel.datasource.ds2.nacos.groupId=SENTINEL_GROUP
spring.cloud.sentinel.datasource.ds2.nacos.namespace=54c39cfe-d1c4-4022-a94b-a3486c5927fc
spring.cloud.sentinel.datasource.ds2.nacos.ruleType=system

# Redis数据库索引（默认为0）
spring.redis.database=1
# Redis服务器地址
spring.redis.host=192.168.41.13
# Redis服务器连接密码（默认为空）
#spring.redis.password=
# Redis服务器连接端口
spring.redis.port=6379

# Lettuce
# 连接池最大连接数（使用负值表示没有限制）
spring.redis.lettuce.pool.max-active=8
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.lettuce.pool.max-wait=10000
# 连接池中的最大空闲连接
spring.redis.lettuce.pool.max-idle=8
# 连接池中的最小空闲连接
spring.redis.lettuce.pool.min-idle=0
# 关闭超时时间
spring.redis.lettuce.shutdown-timeout=100
```
# 2 路由
## 2.1 域名和端口
域名:route.diligrp.com
端口:8286

## 2.2 建表脚本
```sql
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
```


## 2.3 dynamic-route.properties配置
**已经完成线上配置**
```
server.port=8286
spring.application.name=dynamic-route
#server.tomcat.connection-timeout=1
#spring.mvc.async.request-timeout=1

#druid监控
druidFilter.enable=false
#web配置,listener, filter, servlet
web.enable=true
#beetl
beetl.enable=true
quartz.enabled=false
#swagger:http://host:port/swagger-ui.html
swagger.enable=false

spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.druid.maxActive=20
spring.datasource.druid.initialSize=1
spring.datasource.druid.maxWait=60000
spring.datasource.druid.minIdle=1
spring.datasource.druid.timeBetweenEvictionRunsMillis=60000
spring.datasource.druid.minEvictableIdleTimeMillis=300000
spring.datasource.druid.validationQuery=select 1
spring.datasource.druid.testWhileIdle=true
spring.datasource.druid.testOnBorrow=false
spring.datasource.druid.testOnReturn=false
spring.datasource.druid.poolPreparedStatements=true
spring.datasource.druid.maxOpenPreparedStatements=20
spring.datasource.druid.filters=stat,slf4j
spring.datasource.druid.connectionProperties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
#spring.datasource.druid.useGlobalDataSourceStat=true

spring.datasource.druid.filter.wall.enabled=true
spring.datasource.druid.filter.wall.config.enabled=true
spring.datasource.druid.filter.wall.config.multiStatementAllow=true
spring.datasource.druid.filter.wall.config.noneBaseStatementAllow=true

#mybatis
mybatis.mapperLocations=classpath*:mapper/*.xml
mybatis.typeAliasesPackage=com.dili.dr.domain
mybatis.configLocation=classpath:conf/mybatis-config.xml

#mybatis mapper
#mapper.mappers[0]=com.dili.ss.base.MyMapper
mapper.not-empty=false
mapper.identity=MYSQL
mapper.enable-method-annotation=true
mapper.safe-delete=true
mapper.safe-update=true
#mybatis pagehelper
pagehelper.helperDialect=mysql
pagehelper.reasonable=true
pagehelper.supportMethodsArguments=true
pagehelper.params=count=countSql

swagger.basePackage=com.dili.dr.controller

#spring cloud
spring.cloud.nacos.discovery.server-addr=nacos.diligrp.com:8848
spring.cloud.nacos.discovery.group=MICROSERVICE
spring.cloud.nacos.discovery.namespace=54c39cfe-d1c4-4022-a94b-a3486c5927fc
spring.cloud.nacos.discovery.service=dynamic-route

#feign 配置
feign.okhttp.enabled=true
feign.client.config.default.connectTimeout=1000
feign.client.config.default.readTimeout=15000

#logback
logging.config=classpath:conf/logback-springboot.xml

#用户未登录页面
error.page.noLogin=error/uapNoLogin

#配置统一权限登录页(选配)
error.page.loginPage=https://uap.diligrp.com/login/toLogin.html
#用于RPC
uap.contextPath=https://uap.diligrp.com
project.serverPath=https://route.diligrp.com:8286
```