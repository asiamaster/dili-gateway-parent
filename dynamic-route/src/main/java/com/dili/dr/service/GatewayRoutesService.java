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
     * 新增路由，并更新缓存
     * @param gatewayRoutes
     * @return
     */
    @Override
    int insertSelective(GatewayRoutes gatewayRoutes);

    /**
     * 更新路由，并更新缓存
     * @param gatewayRoutes
     * @return
     */
    @Override
    int updateSelective(GatewayRoutes gatewayRoutes);

    /**
     * 根据路由id删除
     * @param routeId
     * @return
     */
    int delete(String routeId);

    /**
     * 启/禁用
     * @param id
     * @param enable
     * @return
     */
    BaseOutput updateEnable(Long id, Boolean enable);
}
