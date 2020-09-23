package com.dili.gw.uap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Session超时配置
 */
@RefreshScope
@Component
public class DynaSessionConstants {

    // sessionId - SessionData的Redis 过期时间(秒)
    // sessionId - userId和UserIdSessionData的Redis 过期时间(秒)
    // 默认为30分钟
    private Long SESSION_TIMEOUT;


    /**
     * 获取session超时时间
     * @return
     */
    public Long getSessionTimeout() {
        return SESSION_TIMEOUT;
    }

    /**
     * 设置session超时时间
     * @param sessionTimeout
     */
    @Value("${uap.sessionTimeout:1800}")
    public void setSessionTimeout(Long sessionTimeout) {
        SESSION_TIMEOUT = sessionTimeout;
    }

}