package com.example.config.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义会话管理器
 */

/**
 * 自定义会话管理器
 */
public class CustomerSessionManager extends DefaultWebSessionManager {
    /**
     * 将用户的sessionId以请求头的方式，token:90536d3c-d10a-42d4-9ec1-b9b2827527f0
     * 重写该方法的原因在于，父类中是从Cookie中取得SessionId, 但是有自己的规则
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        System.out.println("session管理：判断用户是否登录");
        // 从请求头中获取SessionId
        String sessionId = WebUtils.toHttp(request).getHeader("token");
        if (null != sessionId) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());
            return sessionId;
        } else {
            // 没有按照自定义的规则，直接调用父类的方法
            return super.getSessionId(request, response);
        }
    }
}
