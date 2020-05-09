package com.dili.dr.service.impl;

import com.dili.dr.domain.GatewayRoutes;
import com.dili.dr.mapper.GatewayRoutesMapper;
import com.dili.dr.rpc.DynamicRouteRpc;
import com.dili.dr.service.GatewayRoutesService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class GatewayRoutesServiceImpl extends BaseServiceImpl<GatewayRoutes, Long> implements GatewayRoutesService {

    @Autowired
    DynamicRouteRpc dynamicRouteRpc;

    public GatewayRoutesMapper getActualDao() {
        return (GatewayRoutesMapper)getDao();
    }

    @PostConstruct
    public void init() {
        dynamicRouteRpc.load(list(null));
    }

    @Override
    public int insertSelective(GatewayRoutes gatewayRoutes) {
        int count = super.insertSelective(gatewayRoutes);
        dynamicRouteRpc.add(gatewayRoutes);
        return count;
    }

    @Override
    public int updateSelective(GatewayRoutes gatewayRoutes) {
        int count = super.updateSelective(gatewayRoutes);
        dynamicRouteRpc.update(gatewayRoutes);
        return count;
    }

    @Override
    public int delete(String routeId) {
        GatewayRoutes gatewayRoutes = new GatewayRoutes();
        gatewayRoutes.setRouteId(routeId);
        int count = super.deleteByExample(gatewayRoutes);
        dynamicRouteRpc.del(routeId);
        return count;
    }
}
