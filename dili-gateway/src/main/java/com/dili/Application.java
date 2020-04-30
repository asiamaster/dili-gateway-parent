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
@ComponentScan(basePackages={"com.dili.ss","com.dili.gateway"})
@EnableDiscoveryClient
//@EnableFeignClients
public class Application {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes().route(predicateSpec ->
                predicateSpec.path("/customer/**").uri("lb://customer-service").id("customer-service")
        ).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        List<String> list = Lists.newArrayList();
//        list.add("create2");
//        list.add("create3");
//        Flux.create((t) -> {
//            t.next(list);
//            t.next("create1");
//            t.complete();
//        }).subscribe(System.out::println);
//
//        Flux.generate(t -> {
//            t.next("generate");
//            //注意generate中next只能调用1次
//            t.complete();
//        }).subscribe(System.out::println);

    }


}
