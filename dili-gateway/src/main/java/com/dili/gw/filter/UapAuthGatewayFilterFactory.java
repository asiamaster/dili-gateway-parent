package com.dili.gw.filter;

import com.dili.gw.uap.UserRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * UAP鉴权过滤器工厂
 * @description:
 * @author: WM
 * @time: 2020/8/18 11:24
 */
@Component
public class UapAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Autowired
    private UserRedis userRedis;

    public UapAuthGatewayFilterFactory() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config)
    {
        return new UapAuthGatewayFilter(userRedis);
    }
}
