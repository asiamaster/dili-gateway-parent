package com.dili.gw.uap;

import com.alibaba.fastjson.JSONObject;
import com.dili.gw.uap.manager.SessionRedisManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户redis操作
 * Created by Administrator on 2016/10/18.
 */
@Service
public class UserRedis {

    @Autowired
    private ManageRedisUtil redisUtil;

    @Autowired
    private SessionRedisManager sessionRedisManager;

    @Autowired
    private DynaSessionConstants dynaSessionConstants;

    /**
     * 根据sessionId获取userId
     * @param sessionId
     * @return
     */
    public Long getSessionUserId(String sessionId) {
        String rst = redisUtil.get(SessionConstants.SESSIONID_USERID_KEY + sessionId, String.class);
        if(rst == null){
            return null;
        }
        return Long.valueOf(rst);
    }

    /**
     * 根据sessionId获取数据，支持转型为指定的clazz<br/>
     * 如果有数据则将redis超时推后dynaSessionConstants.getSessionTimeout()的时间<br/>
     *
     * @param sessionId
     * @return
     */
    public UserTicket getUser(String sessionId) {
        String sessionData = getSession(sessionId);
        if (StringUtils.isBlank(sessionData)) {
            return null;
        }
        //推迟redis session过期时间
        defer(sessionId);
//        DTO userDto = JSONObject.parseObject(JSONObject.parseObject(sessionData).get(SessionConstants.LOGGED_USER).toString(), DTO.class);
//        return DTOUtils.proxyInstance(userDto, UserTicket.class);
        //直接返回FastJSON的JDK代理对象
        UserTicket userTicket = JSONObject.parseObject(JSONObject.parseObject(sessionData).get(SessionConstants.LOGGED_USER).toString(), UserTicket.class);
//        DTOUtils.bean2Instance(userTicket, UserTicket.class);
        return userTicket;

    }

    /**
     * 推迟redis session过期时间
     * @param sessionId
     */
    private void defer(String sessionId) {
        //推后SessionConstants.SESSIONID_USERID_KEY + sessionId : userId : dynaSessionConstants.getSessionTimeout()
        redisUtil.expire(SessionConstants.SESSIONID_USERID_KEY + sessionId, dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
        //先根据sessionId找到用户id
        //SessionConstants.SESSIONID_USERID_KEY + sessionId : userId : dynaSessionConstants.getSessionTimeout()
        String userId = getUserIdBySessionId(sessionId);
        //再根据userId，推后sessionId
        //推后SessionConstants.USERID_SESSIONID_KEY + userId : sessionId : dynaSessionConstants.getSessionTimeout()
        redisUtil.expire(SessionConstants.USERID_SESSIONID_KEY + userId, dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
        //推后SessionConstants.USER_SYSTEM_KEY + userId : systems : dynaSessionConstants.getSessionTimeout()
        redisUtil.expire(SessionConstants.USER_SYSTEM_KEY + userId, dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
        //推后SessionConstants.USER_MENU_URL_KEY + userId : menuUrls : dynaSessionConstants.getSessionTimeout()
        redisUtil.expire(SessionConstants.USER_MENU_URL_KEY + userId, dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
        //推后SessionConstants.USER_RESOURCE_CODE_KEY + userId ： resourceCodes : dynaSessionConstants.getSessionTimeout()
        redisUtil.expire(SessionConstants.USER_RESOURCE_CODE_KEY + userId, dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
        //推后SessionConstants.USER_DATA_AUTH_KEY + userId : userDataAuths : dynaSessionConstants.getSessionTimeout()
        redisUtil.expire(SessionConstants.USER_DATA_AUTH_KEY + userId, dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
    }
    
    /**
     * 根据sessionId获取String数据<br/>
     * 如果有数据则将redis超时推后dynaSessionConstants.getSessionTimeout()的时间
     * @param sessionId
     * @return
     */
    private String getSession(String sessionId){
        String sessionData = redisUtil.get(SessionConstants.SESSION_KEY_PREFIX + sessionId, String.class);
        if (sessionData != null) {
            redisUtil.expire(SessionConstants.SESSION_KEY_PREFIX + sessionId, dynaSessionConstants.getSessionTimeout(), TimeUnit.SECONDS);
        }
        return sessionData;
    }

    /**
     * 根据用户id获取sessionId列表
     * @param userId
     * @return
     */
    public List<String> getSessionIdsByUserId(String userId) {
        return sessionRedisManager.getSessionIdsByUserId(userId);
    }

    /**
     * 根据sessionId取用户id
     * @param sessionId
     * @return
     */
    public String getUserIdBySessionId(String sessionId){
        return sessionRedisManager.getUserIdBySessionId(sessionId);
    }

    /**
     * 根据sessionId取用户名
     * @param sessionId
     * @return
     */
    public String getUserNameBySessionId(String sessionId){
        return sessionRedisManager.getUserNameBySessionId(sessionId);
    }

    // -----------------  token -----------------------------

    /**
     * 根据token获取userId
     * @param token
     * @return
     */
    public Long getTokenUserId(String token) {
        String rst = getTokenUserIdString(token);
        if(rst == null){
            return null;
        }
        return Long.valueOf(rst);
    }

    /**
     * 根据token取用户id
     * @param token
     * @return
     */
    public String getTokenUserIdString(String token){
        return redisUtil.get(SessionConstants.TOKEN_USERID_KEY + token, String.class);
    }

    /**
     * 推迟redis token过期时间
     *
     * @param token
     */
    private void deferToken(String token) {
        // 推后SessionConstants.SESSIONID_USERID_KEY + sessionId : userId :
        redisUtil.expire(SessionConstants.TOKEN_USERID_KEY + token, dynaSessionConstants.getTokenTimeout(), TimeUnit.SECONDS);
        // 先根据sessionId找到用户id
        String userId = getTokenUserIdString(token);
        // 再根据userId，推后sessionId
        redisUtil.expire(SessionConstants.USERID_TOKEN_KEY + userId, dynaSessionConstants.getTokenTimeout(), TimeUnit.SECONDS);
        // 推后SessionConstants.USER_SYSTEM_KEY + userId : systems :
        redisUtil.expire(SessionConstants.USER_SYSTEM_TOKEN_KEY + userId, dynaSessionConstants.getTokenTimeout(), TimeUnit.SECONDS);
        // 推后SessionConstants.USER_MENU_URL_KEY + userId : menuUrls :
        redisUtil.expire(SessionConstants.USER_MENU_URL_TOKEN_KEY + userId, dynaSessionConstants.getTokenTimeout(), TimeUnit.SECONDS);
        // 推后SessionConstants.USER_RESOURCE_CODE_KEY + userId ： resourceCodes :
        redisUtil.expire(SessionConstants.USER_RESOURCE_CODE_TOKEN_KEY + userId, dynaSessionConstants.getTokenTimeout(), TimeUnit.SECONDS);
        // 推后SessionConstants.USER_DATA_AUTH_KEY + userId : userDataAuths :
        redisUtil.expire(SessionConstants.USER_DATA_AUTH_TOKEN_KEY + userId, dynaSessionConstants.getTokenTimeout(), TimeUnit.SECONDS);
    }

    public UserTicket getTokenUser(String token) {
        String sessionData = getToken(token);
        if (StringUtils.isBlank(sessionData)) {
            return null;
        }
        // 推迟redis session过期时间
        deferToken(token);
        // 直接返回FastJSON的JDK代理对象
        UserTicket userTicket = JSONObject.parseObject(JSONObject.parseObject(sessionData).get(SessionConstants.LOGGED_USER).toString(), UserTicket.class);
        return userTicket;
    }

    private String getToken(String token) {
        String sessionData = redisUtil.get(SessionConstants.TOKEN_KEY_PREFIX + token, String.class);
        if (sessionData != null) {
            redisUtil.expire(SessionConstants.TOKEN_KEY_PREFIX + token, dynaSessionConstants.getTokenTimeout(), TimeUnit.SECONDS);
        }
        return sessionData;
    }

}
