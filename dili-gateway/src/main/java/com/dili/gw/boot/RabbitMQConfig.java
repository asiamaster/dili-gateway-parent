package com.dili.gw.boot;

import com.dili.ss.sid.util.IdUtils;
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

//    /**
//     * fanout交换机
//     */
//    public static final String MQ_DR_FANOUT_EXCHANGE = "dili.dr.fanoutExchange";

    public static final String MQ_DR_TOPIC_EXCHANGE = "dili.dr.topicExchange";
    /**
     * MQ 动态路由Queue
     */
    public static final String MQ_DR_ROUTING_QUEUE = IdUtils.nextId()+"";
//    public static final String MQ_DR_ROUTING_QUEUE = "dili.dr.routingQueue";
    /**
     * MQ 路由Key
     */
    public static final String MQ_DR_ROUTING_KEY = "dili.dr.routingKey";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public Queue routingQueue() {
        return new Queue(MQ_DR_ROUTING_QUEUE, true, false, true);
    }

    @Bean
    public TopicExchange drTopicExchange() {
        return new TopicExchange(MQ_DR_TOPIC_EXCHANGE, true, false);
    }
    @Bean
    public Binding keyBinding() {
        return BindingBuilder.bind(routingQueue()).to(drTopicExchange()).with("#");
    }

//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return new FanoutExchange(MQ_DR_FANOUT_EXCHANGE);
//    }
//
//    @Bean
//    public Binding bindingExchange() {
//        return BindingBuilder.bind(routingQueue()).to(fanoutExchange());
//    }
}
