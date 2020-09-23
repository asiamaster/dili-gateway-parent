package com.dili.dr.boot;

import com.dili.dr.glossary.RouteConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
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

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange loggerTopicExchange() {
        return new TopicExchange(RouteConstant.MQ_DR_TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public Queue routingQueue() {
        return new Queue(RouteConstant.MQ_DR_ROUTING_QUEUE, true, false, false);
    }

    @Bean
    public Binding keyBinding() {
        return BindingBuilder.bind(routingQueue()).to(loggerTopicExchange()).with(RouteConstant.MQ_DR_ROUTING_KEY);
    }

}
