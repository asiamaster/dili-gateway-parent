package com.dili.dr.service.impl;

import com.dili.dr.domain.GatewayRouteDefinition;
import com.dili.dr.domain.GatewayRoutes;
import com.dili.dr.mapper.GatewayRoutesMapper;
import com.dili.dr.rpc.DynamicRouteRpc;
import com.dili.dr.service.GatewayRoutesService;
import com.dili.dr.service.MsgService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class GatewayRoutesServiceImpl extends BaseServiceImpl<GatewayRoutes, Long> implements GatewayRoutesService {

    protected static final Logger log = LoggerFactory.getLogger(GatewayRoutesServiceImpl.class);
    @Autowired
    private DynamicRouteRpc dynamicRouteRpc;
    @Autowired
    private MsgService msgService;

    public GatewayRoutesMapper getActualDao() {
        return (GatewayRoutesMapper)getDao();
    }

    @Override
    @PostConstruct
    public void init() {
//        BaseOutput<String> output = dynamicRouteRpc.load(list(null));
//        if(!output.isSuccess()) {
//            log.error("网关启动加载失败"+output.getMessage());
//        }
        try {
            List<GatewayRoutes> list = list(null);
            BaseOutput<String> baseOutput = dynamicRouteRpc.validate(list);
            if(!baseOutput.isSuccess()) {
                log.error("网关加载失败:"+baseOutput.getMessage());
                return;
            }
            msgService.send(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BaseOutput<String> reload(){
        List<GatewayRoutes> list = list(null);

        try {
            BaseOutput<String> baseOutput = dynamicRouteRpc.validate(list);
            if(!baseOutput.isSuccess()) {
                return BaseOutput.failure("网关启动加载失败:"+baseOutput.getMessage());
            }
            msgService.send(list);
            return BaseOutput.success();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseOutput.failure(e.getMessage());
        }
    }

    @Override
    @Transactional
    public BaseOutput<String> insertSelectiveAndRefreshRoute(GatewayRoutes gatewayRoutes) {
        GatewayRoutes condition = new GatewayRoutes();
        condition.setRouteId(gatewayRoutes.getRouteId());
        List<GatewayRoutes> list = list(condition);
        if(!list.isEmpty()){
            return BaseOutput.failure("路由id[" + gatewayRoutes.getRouteId() + "]重复");
        }
        super.insertSelective(gatewayRoutes);
        BaseOutput<String> output = dynamicRouteRpc.add(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes));
        if(!output.isSuccess()){
            throw new BusinessException(ResultCode.UNSUPPORTED_MEDIA_TYPE, output.getMessage());
        }
        return output;
    }

    @Override
    @Transactional
    public BaseOutput<String> updateSelectiveAndRefreshRoute(GatewayRoutes gatewayRoutes) {
        super.updateSelective(gatewayRoutes);
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
        List<GatewayRoutes> list = list(gatewayRoutes);
        if(CollectionUtils.isEmpty(list)){
            return BaseOutput.failure("路由id["+routeId+"]不存在");
        }
        GatewayRoutes gatewayRoutes1 = list.get(0);
        delete(gatewayRoutes1.getId());
        //如果是禁用状态，则直接返回成功
        if(!gatewayRoutes1.getEnabled()){
            return BaseOutput.success();
        }
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
