package com.dili.gw.boot;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 初始化组件
 */
@Component
public class InitComponent {

    /**
     * 初始化处理网关API
     */
    @PostConstruct
    public void init(){
//        Set<ApiDefinition> definitions = new HashSet<>();
//        ApiDefinition api1 = new ApiDefinition("service_api")
//                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
//                    add(new ApiPathPredicateItem().setPattern("/assets-service/**"));
//                    add(new ApiPathPredicateItem().setPattern("/settlement-service/**"));
//                    add(new ApiPathPredicateItem().setPattern("/customer-service/**")
//                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
//                }});
//        ApiDefinition api2 = new ApiDefinition("all_api")
//                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
//                    add(new ApiPathPredicateItem().setPattern("/**")
//                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
//                }});
//        definitions.add(api1);
//        definitions.add(api2);
//        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}
