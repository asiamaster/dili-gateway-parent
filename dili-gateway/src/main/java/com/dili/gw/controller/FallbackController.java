package com.dili.gw.controller;

import com.dili.ss.domain.BaseOutput;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 失效备援
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {
    /**
     *
     * @return
     */
    @RequestMapping("")
    public BaseOutput<String> fallback(ServerHttpRequest request){
        return BaseOutput.failure("服务已熔断, URL:" + request.getHeaders().get("gatewayUrl"));
    }
}
