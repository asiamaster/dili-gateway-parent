package com.dili.dr.rpc;

import com.dili.dr.domain.GatewayRoutes;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.ReqParam;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;

import java.util.List;

@Restful("${gateway.contextPath}")
public interface DynamicRouteRpc {
    @POST("/api/route/add")
    BaseOutput<String> add(@VOBody GatewayRoutes gatewayRoutes);

    @POST("/api/route/update")
    BaseOutput<String> update(@VOBody GatewayRoutes gatewayRoutes);

    @POST("/api/route/del")
    BaseOutput<String> del(@ReqParam("id") String id);

    @POST("/api/route/load")
    BaseOutput<String> load(@VOBody List<GatewayRoutes> gatewayRoutes);
}
