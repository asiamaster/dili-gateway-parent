package com.dili.dr.service;

import com.alibaba.fastjson.JSON;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.dr.boot.RabbitMQConfig;
import com.dili.dr.domain.GatewayRoutes;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * MQ消息服务
 */
@Component
@ConditionalOnClass(RabbitTemplate.class)
public class MsgService {

    @Autowired
    private RabbitMQMessageService rabbitMQMessageService;

    /**
     * 发送消息到MQ
     * @param gatewayRoutes
     */
    public void send(List<GatewayRoutes> gatewayRoutes) {
        if (Objects.nonNull(gatewayRoutes)) {
            rabbitMQMessageService.send(RabbitMQConfig.MQ_DR_TOPIC_EXCHANGE,  RabbitMQConfig.MQ_DR_ROUTING_KEY, JSON.toJSONString(gatewayRoutes));
        }
    }

}
