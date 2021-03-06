package com.dili;

import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 由MyBatis Generator工具自动生成
 */
@SpringBootApplication
@ComponentScan(basePackages={"com.dili.ss", "com.dili.dr", "com.dili.uap.sdk", "com.dili.commons.rabbitmq"})
@MapperScan(basePackages = {"com.dili.dr.mapper", "com.dili.ss.dao"})
@RestfulScan({"com.dili.uap.sdk.rpc"})
@EnableDiscoveryClient
@EnableFeignClients
public class RouteApplication {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(RouteApplication.class, args);
    }


}
