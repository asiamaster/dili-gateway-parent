package com.dili.dr.service.impl;

import com.dili.dr.domain.GatewayRouteDefinition;
import com.dili.dr.domain.GatewayRoutes;
import com.dili.dr.mapper.GatewayRoutesMapper;
import com.dili.dr.rpc.DynamicRouteRpc;
import com.dili.dr.service.GatewayMsgService;
import com.dili.dr.service.GatewayRoutesService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Lists;
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
    private GatewayMsgService gatewayMsgService;

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
            gatewayMsgService.reload(list);
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
            gatewayMsgService.reload(list);
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
        BaseOutput<String> baseOutput = dynamicRouteRpc.validateWithDuplicate(Lists.newArrayList(gatewayRoutes));
        if(!baseOutput.isSuccess()) {
            return BaseOutput.failure("新增失败:"+baseOutput.getMessage());
        }
        super.insertSelective(gatewayRoutes);
        gatewayMsgService.add(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes));
        return BaseOutput.success();
    }

    @Override
    @Transactional
    public BaseOutput<String> updateSelectiveAndRefreshRoute(GatewayRoutes gatewayRoutes) {
        BaseOutput<String> baseOutput = dynamicRouteRpc.validate(Lists.newArrayList(gatewayRoutes));
        if(!baseOutput.isSuccess()) {
            return BaseOutput.failure("修改失败:"+baseOutput.getMessage());
        }
        super.updateSelective(gatewayRoutes);
        gatewayMsgService.update(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes));
        return BaseOutput.success();
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
        //如果是禁用状态，则直接返回成功，不用再通知网关停用该路由
        if(!gatewayRoutes1.getEnabled()){
            return BaseOutput.success();
        }
        gatewayMsgService.del(routeId);
        return BaseOutput.success();
    }

    @Override
    @Transactional
    public BaseOutput<String> updateEnableAndRefreshRoute(Long id, Boolean enable) {
        GatewayRoutes gatewayRoutes = get(id);
        gatewayRoutes.setEnabled(enable);
        updateSelective(gatewayRoutes);
        if(enable){
            gatewayMsgService.add(gatewayRoutes2GatewayRouteDefinition(gatewayRoutes));
        }else{
            gatewayMsgService.del(gatewayRoutes.getRouteId());
        }
        return BaseOutput.success();
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
