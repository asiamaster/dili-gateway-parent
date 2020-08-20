package com.dili.gw.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.gw.uap.UserRedis;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
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

    private UserRedis userRedis;

    public UapAuthGatewayFilter(UserRedis userRedis){
        this.userRedis = userRedis;
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
        //获取header的参数
        String sessionId = request.getHeaders().getFirst("sessionId");
        if (StringUtils.isNotBlank(sessionId)){
            Long sessionUserId = userRedis.getSessionUserId(sessionId);
            if(sessionUserId != null) {
                ServerHttpRequest host = exchange.getRequest().mutate().header("sessionUserId", sessionUserId.toString()).build();
                ServerWebExchange build = exchange.mutate().request(host).build();
                return chain.filter(build);
            }
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(getWrapData()))));
        }
        else{
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(getWrapData()))));
//            return response.setComplete();
        }
    }

    private byte[] getWrapData() {
        BaseOutput baseOutput = BaseOutput.failure(ResultCode.FORBIDDEN, "鉴权失败");
        return JSONObject.toJSONString(baseOutput).getBytes();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public UserRedis getUserRedis() {
        return userRedis;
    }

    public void setUserRedis(UserRedis userRedis) {
        this.userRedis = userRedis;
    }
}