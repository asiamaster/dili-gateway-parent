package com.dili.dr.boot;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <B>RabbitMQ的相关配置信息</B>
 *
 * @author wangmi
 * @date 2020/9/23
 */
@Configuration
public class RabbitMQConfig {

    public static final String MQ_DR_TOPIC_EXCHANGE = "dili.dr.topicExchange";
    /**
     * MQ 路由Key
     */
    public static final String MQ_DR_ROUTING_KEY = "dili.dr.routingKey";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange drTopicExchange() {
        return new TopicExchange(MQ_DR_TOPIC_EXCHANGE, true, false);
    }
}
