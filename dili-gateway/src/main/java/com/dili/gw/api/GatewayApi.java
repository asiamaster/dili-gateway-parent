package com.dili.gw.api;

import com.dili.gw.domain.GatewayRouteDefinition;
import com.dili.gw.domain.GatewayRoutes;
import com.dili.gw.service.DynamicRouteService;
import com.dili.gw.utils.RouteDefinitionUtils;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关控制器
 * 用于动态刷新路由、过滤器等信息
 */
@RestController
@RequestMapping("/api/route")
public class GatewayApi {

    @Autowired
    private DynamicRouteService dynamicRouteService;

    /**
     * 查询路由
     * @return
     */
    @GetMapping("/listAll")
    public Flux<RouteDefinition> listAll() {
        return dynamicRouteService.listAll().publishOn(Schedulers.elastic());
    }

    /**
     * 重新加载路由信息
     * @param gatewayRoutes
     * @return
     */
    @PostMapping("/reload")
    public BaseOutput<String> reload(@RequestBody List<GatewayRoutes> gatewayRoutes){
        try {
            dynamicRouteService.reload(gatewayRoutes);
            return BaseOutput.success();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 批量验证路由信息
     * @param gatewayRoutes
     * @return
     */
    @PostMapping("/validate")
    public BaseOutput<String> validate(@RequestBody List<GatewayRoutes> gatewayRoutes){
        String validate = dynamicRouteService.validate(convertRouteDefinition(gatewayRoutes));
        if(StringUtils.isBlank(validate)){
            return BaseOutput.success();
        }
        return BaseOutput.failure(validate);
    }

    /**
     * 批量验证路由信息
     * @param gatewayRoutes
     * @return
     */
    @PostMapping("/validateWithDuplicate")
    public BaseOutput<String> validateWithDuplicate(@RequestBody List<GatewayRoutes> gatewayRoutes){
        String validate = dynamicRouteService.validateWithDuplicate(convertRouteDefinition(gatewayRoutes));
        if(StringUtils.isBlank(validate)){
            return BaseOutput.success();
        }
        return BaseOutput.failure(validate);
    }

    /**
     * 增加路由
     * @param gatewayRouteDefinition
     * @return
     */
    @PostMapping("/add")
    public BaseOutput<String> add(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        try {
            String errorMsg = this.dynamicRouteService.add(RouteDefinitionUtils.assembleRouteDefinition(gatewayRouteDefinition));
            return errorMsg == null ? BaseOutput.success() : BaseOutput.failure(errorMsg);
        } catch (Exception e) {
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 更新路由
     * @param gatewayRouteDefinition
     * @return
     */
    @PostMapping("/update")
    public BaseOutput<String> update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        try {
            String errorMsg = this.dynamicRouteService.update(RouteDefinitionUtils.assembleRouteDefinition(gatewayRouteDefinition));
            return errorMsg == null ? BaseOutput.success() : BaseOutput.failure(errorMsg);
        } catch (Exception e) {
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 删除路由
     * @param id
     * @return
     */
    @DeleteMapping("/del/{id}")
    public BaseOutput<String> delete(@PathVariable String id) {
        String errorMsg = this.dynamicRouteService.delete(id);
        return errorMsg == null ? BaseOutput.success() : BaseOutput.failure(errorMsg);
    }

    /**
     * 类型转换
     * @param gatewayRoutes
     * @return
     */
    private List<RouteDefinition> convertRouteDefinition(List<GatewayRoutes> gatewayRoutes){
        List<RouteDefinition> routeDefinitions = new ArrayList<>(gatewayRoutes.size());
        gatewayRoutes.forEach(t->{
            routeDefinitions.add(RouteDefinitionUtils.assembleRouteDefinition(t));
        });
        return routeDefinitions;
    }
}
