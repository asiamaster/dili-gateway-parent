package com.dili.gw.rpc;

import com.dili.gw.domain.GatewayRoutes;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.GET;
import com.dili.ss.retrofitful.annotation.Restful;

import java.util.List;

/**
 * 路由服务
 */
@Restful("${route.contextPath}")
public interface RouteRpc {
    /**
     * 查询所有路由
     * @return
     */
    @GET("/api/route/list")
    BaseOutput<List<GatewayRoutes>> list();


}
