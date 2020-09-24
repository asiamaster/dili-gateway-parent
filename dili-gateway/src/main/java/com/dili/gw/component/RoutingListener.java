package com.dili.gw.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.gw.domain.GatewayRouteDefinition;
import com.dili.gw.domain.GatewayRoutes;
import com.dili.gw.service.DynamicRouteService;
import com.dili.gw.utils.RouteDefinitionUtils;
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
     * 路由同步消息监听器
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitMQConfig.MQ_DR_ROUTING_QUEUE}")
    public void synchronousListener(Channel channel, Message message) {
        try {
            String data = new String(message.getBody(), "UTF-8");
            log.info("解析消息内容: " + data);
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

    /**
     * 新增路由消息监听器
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitMQConfig.MQ_DR_ADD_QUEUE}")
    public void addListener(Channel channel, Message message) {
        try {
            String data = new String(message.getBody(), "UTF-8");
            log.info("解析消息内容: " + data);
            GatewayRouteDefinition gatewayRouteDefinition = JSONObject.parseObject(data, GatewayRouteDefinition.class);
            String errorMsg = this.dynamicRouteService.add(RouteDefinitionUtils.assembleRouteDefinition(gatewayRouteDefinition));
            if(errorMsg != null){
                throw new Exception(errorMsg);
            }
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

    /**
     * 修改路由消息监听器
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitMQConfig.MQ_DR_UPDATE_QUEUE}")
    public void updateListener(Channel channel, Message message) {
        try {
            String data = new String(message.getBody(), "UTF-8");
            log.info("解析消息内容: " + data);
            GatewayRouteDefinition gatewayRouteDefinition = JSONObject.parseObject(data, GatewayRouteDefinition.class);
            String errorMsg = this.dynamicRouteService.update(RouteDefinitionUtils.assembleRouteDefinition(gatewayRouteDefinition));
            if(errorMsg != null){
                throw new Exception(errorMsg);
            }
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

    /**
     * 删除路由消息监听器
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "#{rabbitMQConfig.MQ_DR_DELETE_QUEUE}")
    public void deleteListener(Channel channel, Message message) {
        try {
            String data = new String(message.getBody(), "UTF-8");
            log.info("解析消息内容: " + data);
            //因为获取的data两边有引号，所以需要用substring去掉
            String errorMsg = this.dynamicRouteService.delete(data.substring(1, data.length()-1).trim());
            if(errorMsg != null){
                throw new Exception(errorMsg);
            }
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
