package com.dili.gw.component;

import com.alibaba.fastjson.JSONArray;
import com.dili.gw.domain.GatewayRoutes;
import com.dili.gw.glossary.RouteConstant;
import com.dili.gw.service.DynamicRouteService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * <B>路由MQ消息监听器</B>
 *
 * @author wm
 * @date 2020/9/23
 */
@Component
@Slf4j
public class RoutingListener {

    @Autowired
    private DynamicRouteService dynamicRouteService;

    /**
     * 业务日志消息监听器
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = RouteConstant.MQ_DR_ROUTING_QUEUE, concurrency = "10")
    public void businessLogger(Channel channel, Message message) {
        log.info("收到消息: " + message);
        try {
            String data = new String(message.getBody(), "UTF-8");
            List<GatewayRoutes> gatewayRoutes = JSONArray.parseArray(data, GatewayRoutes.class);
            dynamicRouteService.reload(gatewayRoutes);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("转换对象: {} 出错 {}", message, e);
            // redelivered = true, 表明该消息是重复处理消息
            Boolean redelivered = message.getMessageProperties().getRedelivered();
            try {
                if (redelivered) {
                    /**
                     * 1. 对于重复处理的队列消息做补偿机制处理
                     * 2. 从队列中移除该消息，防止队列阻塞
                     */
                    // 消息已重复处理失败, 扔掉消息
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                    log.error("消息 {} 重新处理失败，扔掉消息", message);
                } else {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                }
            } catch (IOException ex) {
                log.error("消息 {} 重放回队列失败 {}", message, ex);
            }
        }
    }

}
