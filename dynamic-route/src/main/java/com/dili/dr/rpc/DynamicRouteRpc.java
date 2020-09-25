package com.dili.dr.rpc;

import com.dili.dr.domain.GatewayRouteDefinition;
import com.dili.dr.domain.GatewayRoutes;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网关路由远程服务
 */
//@Restful("${gateway.contextPath}")
@FeignClient(name = "gateway-service", url = "${DynamicRouteRpc.url:}")
public interface DynamicRouteRpc {
    /**
     * 新增路由
     * @param gatewayRouteDefinition
     * @return
     */
//    @POST("/api/route/add")
    @RequestMapping(value = "/api/route/add", method = RequestMethod.POST)
    BaseOutput<String> add(@RequestBody GatewayRouteDefinition gatewayRouteDefinition);

    /**
     * 修改路由
     * @param gatewayRouteDefinition
     * @return
     */
//    @POST("/api/route/update")
    @RequestMapping(value = "/api/route/update", method = RequestMethod.POST)
    BaseOutput<String> update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition);

    /**
     * 根据routeId删除路由
     * @param routeId
     * @return
     */
//    @POST("/api/route/del")
//    @RequestMapping(value = "/api/route/del", method = RequestMethod.DELETE)
//    BaseOutput<String> del(@RequestParam("id") String id);
    @DeleteMapping("/api/route/del/{routeId}")
    BaseOutput<String> del(@PathVariable("routeId") String routeId);

    /**
     * 验证路由
     * @param gatewayRoutes
     * @return
     */
    @RequestMapping(value = "/api/route/validate", method = RequestMethod.POST)
    BaseOutput<String> validate(@RequestBody List<GatewayRoutes> gatewayRoutes);

    /**
     * 验证路由，包括验重
     * @param gatewayRoutes
     * @return
     */
    @RequestMapping(value = "/api/route/validateWithDuplicate", method = RequestMethod.POST)
    BaseOutput<String> validateWithDuplicate(@RequestBody List<GatewayRoutes> gatewayRoutes);

    /**
     * 重新加载路由
     * @param gatewayRoutes
     * @return
     */
//    @POST("/api/route/reload")
    @RequestMapping(value = "/api/route/reload", method = RequestMethod.POST)
    BaseOutput<String> reload(@RequestBody List<GatewayRoutes> gatewayRoutes);
}
