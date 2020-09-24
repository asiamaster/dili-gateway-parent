package com.dili.dr.boot;

import org.springframework.amqp.core.TopicExchange;
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
     * 同步路由Key
     */
    public static final String MQ_DR_ROUTING_KEY = "dili.dr.routingKey";

    /**
     * 新增路由Key
     */
    public static final String MQ_DR_ADD_KEY = "dili.dr.addKey";
    /**
     * 修改路由Key
     */
    public static final String MQ_DR_UPDATE_KEY = "dili.dr.updateKey";
    /**
     * 删除路由Key
     */
    public static final String MQ_DR_DELETE_KEY = "dili.dr.deleteKey";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange drTopicExchange() {
        return new TopicExchange(MQ_DR_TOPIC_EXCHANGE, true, false);
    }
}
