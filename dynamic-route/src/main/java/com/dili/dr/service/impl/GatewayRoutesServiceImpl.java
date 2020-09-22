package com.dili.dr.service.impl;

import com.dili.dr.domain.GatewayRouteDefinition;
import com.dili.dr.domain.GatewayRoutes;
import com.dili.dr.mapper.GatewayRoutesMapper;
import com.dili.dr.rpc.DynamicRouteRpc;
import com.dili.dr.service.GatewayRoutesService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
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

    @Override
    @PostConstruct
    public void init() {
        dynamicRouteRpc.load(list(null));
    }

    @Override
    @Transactional
    public BaseOutput<String> insertSelectiveAndRefreshRoute(GatewayRoutes gatewayRoutes) {
        int count = super.insertSelective(gatewayRoutes);
        BaseOutput<String> output = dynamicRouteRpc.add(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes));
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.UNSUPPORTED_MEDIA_TYPE, output.getMessage());
        }
        return output;
    }

    @Override
    @Transactional
    public BaseOutput<String> updateSelectiveAndRefreshRoute(GatewayRoutes gatewayRoutes) {
        int count = super.updateSelective(gatewayRoutes);
        BaseOutput<String> output = dynamicRouteRpc.update(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes));
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.UNSUPPORTED_MEDIA_TYPE, output.getMessage());
        }
        return output;
    }

    @Override
    @Transactional
    public BaseOutput<String> deleteAndRefreshRoute(String routeId) {
        GatewayRoutes gatewayRoutes = new GatewayRoutes();
        gatewayRoutes.setRouteId(routeId);
        super.deleteByExample(gatewayRoutes);
        BaseOutput<String> output = dynamicRouteRpc.del(routeId);
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.UNSUPPORTED_MEDIA_TYPE, output.getMessage());
        }
        return output;
    }

    @Override
    @Transactional
    public BaseOutput<String> updateEnableAndRefreshRoute(Long id, Boolean enable) {
        GatewayRoutes gatewayRoutes = get(id);
        gatewayRoutes.setEnabled(enable);
        updateSelective(gatewayRoutes);
        return enable ? dynamicRouteRpc.add(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes)) : dynamicRouteRpc.del(gatewayRoutes.getRouteId());
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

}
