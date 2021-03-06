package com.dili.gw.service;

import com.dili.gw.domain.GatewayRoutes;
import com.dili.gw.utils.RouteDefinitionUtils;
import com.dili.ss.exception.AppException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态路由服务
 */
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionRepository routeDefinitionWriter;
    private ApplicationEventPublisher publisher;

    protected static final Logger log = LoggerFactory.getLogger(DynamicRouteService.class);

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 重新加载路由信息
     * @return
     */
    public void reload(List<GatewayRoutes> gatewayRoutes) {
        clear();
        load(gatewayRoutes);
    }

    /**
     * 加载路由信息
     * @return
     */
    public void load(List<GatewayRoutes> gatewayRoutes) {
        gatewayRoutes.forEach(t ->{
//            add(RouteDefinitionUtils.assembleRouteDefinition(t));
            //忽略停用的路由
            if(!t.getEnabled()){
                return;
            }
            RouteDefinition routeDefinition = RouteDefinitionUtils.assembleRouteDefinition(t);
            String s = validRouteDefinition(routeDefinition);
            if(s != null){
                throw new AppException("网关加载失败:"+s);
            }
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        });
        if(CollectionUtils.isNotEmpty(gatewayRoutes)) {
            notifyChanged();
        }
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
        try {
            String s = validRouteDefinition(definition);
            if(s != null){
                return s;
            }
            Flux<RouteDefinition> routeDefinitions = routeDefinitionWriter.getRouteDefinitions();
            routeDefinitions.collectList().subscribe(t -> {
                if(!t.isEmpty()) {
                    t.stream().forEach(item -> {
                        if(item.getId().equals(definition.getId())){
                            throw new AppException("路由id["+item.getId()+"]已经存在");
                        }
                    });
                }
            });
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            notifyChanged();
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * 批量验证路由配置，包括验重
     * @param definitions
     * @return
     */
    public String validateWithDuplicate(List<RouteDefinition> definitions) {
        Flux<RouteDefinition> routeDefinitions = routeDefinitionWriter.getRouteDefinitions();
        for(RouteDefinition routeDefinition : definitions) {
            String s = validDuplicate(routeDefinitions, routeDefinition);
            if (s != null) {
                return s;
            }
            s = validRouteDefinition(routeDefinition);
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    /**
     * 批量验证路由配置
     * @param definitions
     * @return
     */
    public String validate(List<RouteDefinition> definitions) {
        for(RouteDefinition routeDefinition : definitions) {
            String s = validRouteDefinition(routeDefinition);
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    /**
     * 验证路由是否和内存中重复
     * @param routeDefinitions
     * @param routeDefinition
     * @return
     */
    private String validDuplicate(Flux<RouteDefinition> routeDefinitions, RouteDefinition routeDefinition){
        List<RouteDefinition> routeDefinitionList = new ArrayList<>();
        routeDefinitions.collectList().subscribe(t -> {
            if(!t.isEmpty()) {
                routeDefinitionList.addAll(t);
            }
        });
        for (RouteDefinition definition : routeDefinitionList) {
            if(definition.getId().equals(routeDefinition.getId())){
                return "路由id["+definition.getId()+"]已经存在";
            }
        }
        return null;
    }

    /**
     * 清空路由
     * @return
     */
    public void clear() {
        try {
            Flux<RouteDefinition> routeDefinitions = routeDefinitionWriter.getRouteDefinitions();
            routeDefinitions.collectList().subscribe(t -> {
                if(!t.isEmpty()) {
                    t.stream().forEach(item -> {
                        this.routeDefinitionWriter.delete(Mono.just(item.getId())).subscribe();
                    });
                    notifyChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新路由
     * @param definition
     * @return error msg
     */
    public String update(RouteDefinition definition) {
        String s = validRouteDefinition(definition);
        if(s != null){
            return s;
        }
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();
            notifyChanged();
        } catch (Exception e) {
            log.warn("update warn, not find route  routeId: "+definition.getId());
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
//            Flux<Route> routes = routeDefinitionRouteLocator.getRoutes();
//            System.out.println(routes.collectList().block(Duration.ZERO));
            notifyChanged();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "update route  fail";
        }
    }

    /**
     * 路由配置验证
     * @param definition
     * @return
     */
    private String validRouteDefinition(RouteDefinition definition){
        RouteDefinitionRouteLocator routeDefinitionRouteLocator = ((AnnotationConfigReactiveWebServerApplicationContext) publisher).getBean(RouteDefinitionRouteLocator.class);
        try {
            Method convertToRoute = RouteDefinitionRouteLocator.class.getDeclaredMethod("convertToRoute", RouteDefinition.class);
            convertToRoute.setAccessible(true);
            convertToRoute.invoke(routeDefinitionRouteLocator, definition);
            return null;
        } catch (Exception e) {
            return "路由定义转换异常:"+ e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
        }
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    public String delete(String id) {
//        return this.routeDefinitionWriter.delete(Mono.just(id)).then(Mono.defer(() -> {
//            return Mono.just(ResponseEntity.ok().build());
//        })).onErrorResume((t) -> {//predicate
//            return t instanceof NotFoundException;
//        }, (t) -> {//fallback
//            return Mono.just(ResponseEntity.notFound().build());
//        });
        try {
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            notifyChanged();
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * 通知修改AnnotationConfigReactiveWebServerApplicationContext
     */
    private void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
