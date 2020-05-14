package com.dili.gw.utils;

import com.dili.gw.domain.GatewayFilterDefinition;
import com.dili.gw.domain.GatewayPredicateDefinition;
import com.dili.gw.domain.GatewayRouteDefinition;
import com.dili.gw.domain.GatewayRoutes;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 路由定义工具类
 */
public class RouteDefinitionUtils {

    /**
     * 把传递进来的参数转换成路由对象
     * @param gatewayRoutes
     * @return
     */
    public static RouteDefinition assembleRouteDefinition(GatewayRoutes gatewayRoutes) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gatewayRoutes.getRouteId());
        definition.setOrder(gatewayRoutes.getRouteOrder());

        //设置断言
        List<PredicateDefinition> pdList=new ArrayList<>();
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList = gatewayRoutes.getPredicateDefinition();
        if(gatewayPredicateDefinitionList != null) {
            for (GatewayPredicateDefinition gpDefinition : gatewayPredicateDefinitionList) {
                PredicateDefinition predicate = new PredicateDefinition();
                predicate.setArgs(gpDefinition.getArgs());
                predicate.setName(gpDefinition.getName());
                pdList.add(predicate);
            }
        }
        definition.setPredicates(pdList);

        //设置过滤器
        List<FilterDefinition> filters = new ArrayList();
        List<GatewayFilterDefinition> gatewayFilters = gatewayRoutes.getFilterDefinition();
        if(gatewayFilters != null) {
            for (GatewayFilterDefinition filterDefinition : gatewayFilters) {
                FilterDefinition filter = new FilterDefinition();
                filter.setName(filterDefinition.getName());
                filter.setArgs(filterDefinition.getArgs());
                filters.add(filter);
            }
        }
        definition.setFilters(filters);

        URI uri = null;
        if(gatewayRoutes.getRouteUri().trim().startsWith("http")){
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRoutes.getRouteUri()).build().toUri();
        }else{
            // uri为 lb://consumer-service 时使用下面的方法
            uri = URI.create(gatewayRoutes.getRouteUri());
        }
        definition.setUri(uri);
        return definition;
    }

    /**
     * 把传递进来的参数转换成路由对象
     * @param gwDefinition
     * @return
     */
    public static RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gwDefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gwDefinition.getId());
        definition.setOrder(gwDefinition.getOrder());

        //设置断言
        List<PredicateDefinition> pdList=new ArrayList<>();
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList = gwDefinition.getPredicates();
        for (GatewayPredicateDefinition gpDefinition: gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }
        definition.setPredicates(pdList);

        //设置过滤器
        List<FilterDefinition> filters = new ArrayList();
        List<GatewayFilterDefinition> gatewayFilters = gwDefinition.getFilters();
        if(gatewayFilters != null) {
            for (GatewayFilterDefinition filterDefinition : gatewayFilters) {
                FilterDefinition filter = new FilterDefinition();
                filter.setName(filterDefinition.getName());
                filter.setArgs(filterDefinition.getArgs());
                filters.add(filter);
            }
        }
        definition.setFilters(filters);

        URI uri = null;
        if(gwDefinition.getUri().startsWith("http")){
            uri = UriComponentsBuilder.fromHttpUrl(gwDefinition.getUri()).build().toUri();
        }else{
            // uri为 lb://consumer-service 时使用下面的方法
            uri = URI.create(gwDefinition.getUri());
        }
        definition.setUri(uri);
        return definition;
    }
}
