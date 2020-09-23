package com.dili.gw.glossary;

/**
 * 路由常量
 */
public interface RouteConstant {

    //=================MQ 相关配置信息 ============//
    /**
     * MQ 交换机配置
     */
    String MQ_DR_TOPIC_EXCHANGE = "dili.dr.topicExchange";
    /**
     * MQ 动态路由Queue
     */
    String MQ_DR_ROUTING_QUEUE = "dili.dr.routingQueue";
    /**
     * MQ 动态路由Key
     */
    String MQ_DR_ROUTING_KEY = "dili.dr.routingKey";
}
