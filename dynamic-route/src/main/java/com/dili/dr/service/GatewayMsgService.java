package com.dili.dr.service;

import com.alibaba.fastjson.JSON;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.dr.boot.RabbitMQConfig;
import com.dili.dr.domain.GatewayRouteDefinition;
import com.dili.dr.domain.GatewayRoutes;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 网关MQ消息服务
 */
@Component
@ConditionalOnClass(RabbitTemplate.class)
public class GatewayMsgService {

    @Autowired
    private RabbitMQMessageService rabbitMQMessageService;

    /**
     * 同步所有路由信息到网关
     * @param gatewayRoutes
     */
    public void reload(List<GatewayRoutes> gatewayRoutes) {
        if (Objects.nonNull(gatewayRoutes)) {
            rabbitMQMessageService.send(RabbitMQConfig.MQ_DR_TOPIC_EXCHANGE,  RabbitMQConfig.MQ_DR_ROUTING_KEY, JSON.toJSONString(gatewayRoutes));
        }
    }

    /**
     * 新增路由
     * @param gatewayRouteDefinition
     * @return
     */
    public void add(GatewayRouteDefinition gatewayRouteDefinition){
        if (Objects.nonNull(gatewayRouteDefinition)) {
            rabbitMQMessageService.send(RabbitMQConfig.MQ_DR_TOPIC_EXCHANGE,  RabbitMQConfig.MQ_DR_ADD_KEY, JSON.toJSONString(gatewayRouteDefinition));
        }
    }

    /**
     * 修改路由
     * @param gatewayRouteDefinition
     * @return
     */
    public void update(GatewayRouteDefinition gatewayRouteDefinition){
        if (Objects.nonNull(gatewayRouteDefinition)) {
            rabbitMQMessageService.send(RabbitMQConfig.MQ_DR_TOPIC_EXCHANGE,  RabbitMQConfig.MQ_DR_UPDATE_KEY, JSON.toJSONString(gatewayRouteDefinition));
        }
    }

    /**
     * 根据routeId删除路由
     * @param routeId
     * @return
     */
    public void del(String routeId){
        if (StringUtils.isNotBlank(routeId)) {
            rabbitMQMessageService.send(RabbitMQConfig.MQ_DR_TOPIC_EXCHANGE,  RabbitMQConfig.MQ_DR_DELETE_KEY, JSON.toJSONString(routeId));
        }
    }
}
