package com.dili.dr.service.impl;

import com.dili.dr.domain.GatewayRouteDefinition;
import com.dili.dr.domain.GatewayRoutes;
import com.dili.dr.mapper.GatewayRoutesMapper;
import com.dili.dr.rpc.DynamicRouteRpc;
import com.dili.dr.service.GatewayRoutesService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class GatewayRoutesServiceImpl extends BaseServiceImpl<GatewayRoutes, Long> implements GatewayRoutesService {

    @Autowired
    private DynamicRouteRpc dynamicRouteRpc;

    public GatewayRoutesMapper getActualDao() {
        return (GatewayRoutesMapper)getDao();
    }

    @PostConstruct
    public void init() {
        dynamicRouteRpc.load(list(null));
    }

    @Override
    @Transactional
    public int insertSelective(GatewayRoutes gatewayRoutes) {
        int count = super.insertSelective(gatewayRoutes);
        dynamicRouteRpc.add(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes));
        return count;
    }

    @Override
    @Transactional
    public int updateSelective(GatewayRoutes gatewayRoutes) {
        int count = super.updateSelective(gatewayRoutes);
        dynamicRouteRpc.update(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes));
        return count;
    }

    @Override
    @Transactional
    public int delete(String routeId) {
        GatewayRoutes gatewayRoutes = new GatewayRoutes();
        gatewayRoutes.setRouteId(routeId);
        int count = super.deleteByExample(gatewayRoutes);
        dynamicRouteRpc.del(routeId);
        return count;
    }

    /**
     * 将GatewayRoutes转换成GatewayRouteDefinition
     * @param gatewayRoutes
     * @return
     */
    private GatewayRouteDefinition gatewayRoutes2GatewayRouteDefinition(GatewayRoutes gatewayRoutes){
        GatewayRouteDefinition gatewayRouteDefinition = new GatewayRouteDefinition();
        gatewayRouteDefinition.setId(gatewayRoutes.getRouteId());
        gatewayRouteDefinition.setPredicates(gatewayRoutes.getPredicateDefinition());
        gatewayRouteDefinition.setFilters(gatewayRoutes.getFilterDefinition());
        gatewayRouteDefinition.setUri(gatewayRoutes.getRouteUri());
        gatewayRouteDefinition.setOrder(gatewayRoutes.getRouteOrder());
        return gatewayRouteDefinition;
    }

    @Override
    public BaseOutput updateEnable(Long id, Boolean enable) {
        GatewayRoutes gatewayRoutes = new GatewayRoutes();
        gatewayRoutes.setId(id);
        if (enable) {
            gatewayRoutes.setEnabled(true);
        } else {
            gatewayRoutes.setEnabled(false);
        }
        getActualDao().updateByPrimaryKeySelective(gatewayRoutes);
        return BaseOutput.success();
    }
}
