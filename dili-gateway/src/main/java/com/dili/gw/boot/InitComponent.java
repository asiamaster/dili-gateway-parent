package com.dili.gw.boot;

import com.dili.gw.domain.GatewayRoutes;
import com.dili.gw.rpc.RouteRpc;
import com.dili.gw.service.DynamicRouteService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化组件
 */
@Component
public class InitComponent implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DynamicRouteService dynamicRouteService;

    @Autowired
    private RouteRpc routeRpc;
    /**
     * 初始化处理网关API
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        BaseOutput<List<GatewayRoutes>> output = routeRpc.list();
        if(output.isSuccess()) {
            dynamicRouteService.reload(output.getData());
        }
    }


//    @PostConstruct
//    public void init(){
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
//    }

}
