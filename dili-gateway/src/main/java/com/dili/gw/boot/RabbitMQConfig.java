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
     * 同步路由Queue
     */
    public static final String MQ_DR_ROUTING_QUEUE = "ROUTING"+IdUtils.nextId();

    /**
     * 新增路由Queue
     */
    public static final String MQ_DR_ADD_QUEUE = "ADD"+IdUtils.nextId();

    /**
     * 修改路由Queue
     */
    public static final String MQ_DR_UPDATE_QUEUE = "UPDATE"+IdUtils.nextId();

    /**
     * 删除路由Queue
     */
    public static final String MQ_DR_DELETE_QUEUE = "DELETE"+IdUtils.nextId();

//    public static final String MQ_DR_ROUTING_QUEUE = "dili.dr.routingQueue";
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

    @Bean
    public Queue routingQueue() {
        return new Queue(MQ_DR_ROUTING_QUEUE, true, false, true);
    }

    @Bean
    public Queue addQueue() {
        return new Queue(MQ_DR_ADD_QUEUE, true, false, true);
    }

    @Bean
    public Queue updateQueue() {
        return new Queue(MQ_DR_UPDATE_QUEUE, true, false, true);
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(MQ_DR_DELETE_QUEUE, true, false, true);
    }

    @Bean
    public Binding routingKeyBinding() {
        return BindingBuilder.bind(routingQueue()).to(drTopicExchange()).with(MQ_DR_ROUTING_KEY);
    }

    @Bean
    public Binding addKeyBinding() {
        return BindingBuilder.bind(addQueue()).to(drTopicExchange()).with(MQ_DR_ADD_KEY);
    }

    @Bean
    public Binding updateKeyBinding() {
        return BindingBuilder.bind(updateQueue()).to(drTopicExchange()).with(MQ_DR_UPDATE_KEY);
    }

    @Bean
    public Binding deleteKeyBinding() {
        return BindingBuilder.bind(deleteQueue()).to(drTopicExchange()).with(MQ_DR_DELETE_KEY);
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
