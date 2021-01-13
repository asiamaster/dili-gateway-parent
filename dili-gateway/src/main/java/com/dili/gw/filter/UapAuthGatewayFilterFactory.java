package com.dili.gw.filter;

import com.dili.uap.sdk.config.ManageConfig;
import com.dili.uap.sdk.service.AuthService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * UAP鉴权过滤器工厂
 * @description:
 * @author: WM
 * @time: 2020/8/18 11:24
 */
@Component
public class UapAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Resource
    private AuthService authService;
    @Resource
    private ManageConfig manageConfig;

    public UapAuthGatewayFilterFactory() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config)
    {
        return new UapAuthGatewayFilter(authService, manageConfig);
    }
}
