package com.dili.gw.service;

import com.dili.gw.domain.GatewayRoutes;
import com.dili.gw.utils.RouteDefinitionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 动态路由服务
 */
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionRepository routeDefinitionWriter;
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 加载路由信息
     * @return
     */
    public void load(List<GatewayRoutes> gatewayRoutes) {
        gatewayRoutes.forEach(t ->{
            add(RouteDefinitionUtils.assembleRouteDefinition(t));
        });
    }

    /**
     * 列出所有缓存中的路由
     * @return
     */
    public Flux<RouteDefinition> listAll(){
//        routeDefinitionWriter.getRouteDefinitions().subscribe(routeDefinition -> {
//            List<PredicateDefinition> predicates = routeDefinition.getPredicates();
//            StringBuilder predicatesStringBuilder = new StringBuilder();
//            predicates.forEach(t -> {
//                predicatesStringBuilder.append(t.getName()).append(":").append(t.getArgs());
//            });
//            System.out.println(String.format("路由信息,id:%s,uri:%s,order:%s",
//                    routeDefinition.getId(),
//                    routeDefinition.getUri(),
//                    routeDefinition.getOrder()));
//        });
        return routeDefinitionWriter.getRouteDefinitions();
    }

    /**
     * 增加路由
     * @param definition
     * @return
     */
    public String add(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        notifyChanged();
        return "success";
    }

    /**
     * 更新路由
     * @param definition
     * @return
     */
    public String update(RouteDefinition definition) {
        try {
            delete(definition.getId());
        } catch (Exception e) {
            return "update fail,not find route  routeId: "+definition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            notifyChanged();
            return "success";
        } catch (Exception e) {
            return "update route  fail";
        }
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    public void delete(String id) {
//        return this.routeDefinitionWriter.delete(Mono.just(id)).then(Mono.defer(() -> {
//            return Mono.just(ResponseEntity.ok().build());
//        })).onErrorResume((t) -> {//predicate
//            return t instanceof NotFoundException;
//        }, (t) -> {//fallback
//            return Mono.just(ResponseEntity.notFound().build());
//        });
        this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        notifyChanged();
    }

    /**
     * 通知修改
     */
    private void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
