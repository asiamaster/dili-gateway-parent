package com.dili.dr.service;

import com.dili.dr.domain.GatewayRoutes;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 网关路由服务
 * 数据库和缓存操作
 */
public interface GatewayRoutesService extends BaseService<GatewayRoutes, Long> {

    /**
     * 初始化数据源
     */
    void init();
    /**
     * 新增路由，并更新缓存
     * @param gatewayRoutes
     * @return
     */
    BaseOutput<String> insertSelectiveAndRefreshRoute(GatewayRoutes gatewayRoutes);

    /**
     * 更新路由，并更新缓存
     * @param gatewayRoutes
     * @return
     */
    BaseOutput<String> updateSelectiveAndRefreshRoute(GatewayRoutes gatewayRoutes);

    /**
     * 根据路由id删除
     * @param routeId
     * @return
     */
    BaseOutput<String> deleteAndRefreshRoute(String routeId);

    /**
     * 启/禁用
     * @param id
     * @param enable
     * @return
     */
    BaseOutput<String> updateEnableAndRefreshRoute(Long id, Boolean enable);
}
