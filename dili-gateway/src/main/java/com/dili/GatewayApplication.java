package com.dili;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * 由MyBatis Generator工具自动生成
 */
@SpringBootApplication
@ComponentScan(basePackages={"com.dili.ss", "com.dili.gw"})
@EnableDiscoveryClient
//@EnableFeignClients
public class GatewayApplication {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * 自定义路由
     * @param builder
     * @return
     */
//    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes().route(predicateSpec ->
                predicateSpec.path("/customer/**").uri("lb://customer-service").id("customer-service")
        ).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


}
