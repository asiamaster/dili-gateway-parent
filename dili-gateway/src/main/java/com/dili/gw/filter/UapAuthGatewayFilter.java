package com.dili.gw.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.gw.consts.GatewayConsts;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.config.ManageConfig;
import com.dili.uap.sdk.constant.SessionConstants;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

/**
 * UAP鉴权过滤器
 */
public class UapAuthGatewayFilter implements GatewayFilter, Ordered {

    static Logger logger = LoggerFactory.getLogger(UapAuthGatewayFilter.class);

    private AuthService authService;

    private ManageConfig manageConfig;

    public UapAuthGatewayFilter(AuthService authService, ManageConfig manageConfig){
        this.authService = authService;
        this.manageConfig = manageConfig;
    }
    /**
     * 全局过滤器 核心方法
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.debug("request = {}", JSONArray.toJSONString( exchange.getRequest()) );
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        String gatewayRequestUri = headers.getFirst(GatewayConsts.GATEWAY_REQUEST_URI);
        if(manageConfig.isExclude(gatewayRequestUri)){
            ServerHttpRequest host = exchange.getRequest().mutate().build();
            ServerWebExchange build = exchange.mutate().request(host).build();
            return chain.filter(build);
        }
        //获取header的参数
        String accessToken = headers.getFirst(SessionConstants.ACCESS_TOKEN_KEY);
        String refreshToken = headers.getFirst(SessionConstants.REFRESH_TOKEN_KEY);
        if(accessToken == null || refreshToken == null){
            //验证不通过
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(getWrapData()))));
        }
        UserTicket userTicket = authService.getUserTicket(accessToken, refreshToken);
        if(userTicket == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(getWrapData()))));
        }
        ServerHttpRequest host = exchange.getRequest().mutate()
                .header("accessToken", accessToken)
                .header("refreshToken", refreshToken)
                .header("userId", userTicket.getId().toString()).build();
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
//      return response.setComplete();
    }

    /**
     * 构建响应数据
     * @return
     */
    private byte[] getWrapData() {
        BaseOutput baseOutput = BaseOutput.failure(ResultCode.FORBIDDEN, "鉴权失败");
        return JSONObject.toJSONString(baseOutput).getBytes();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
}