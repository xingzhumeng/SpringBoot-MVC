package com.example.config.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类容器
 */
@Configuration
public class ShiroConfig {

    /* 自定义全局会话超时时间 */
    @Value("${shiro.global-session-timeout}")
    private Long globalSessionTimeout;

    /* 自定义密码匹配器 */
    @Bean
    public AddSaltCredentialsMatcher addSaltCredentialsMatcher() {
        return new AddSaltCredentialsMatcher();
    }

    /* 自定义的Realm */
    @Bean
    public Realm realm(AddSaltCredentialsMatcher addSaltCredentialsMatcher) {
        CustomerRealm realm = new CustomerRealm();
        // 注入自定义密码匹配器
        realm.setCredentialsMatcher(addSaltCredentialsMatcher);
        return realm;
    }

    /* 自定义会话管理器 */
    @Bean
    public CustomerSessionManager customerSessionManager() {
        CustomerSessionManager customerSessionManager = new CustomerSessionManager();
        // 不操作后，10分钟将会话过期，退出登录
        customerSessionManager.setGlobalSessionTimeout(globalSessionTimeout);
        // 不自动存储sessionId到cookie
        customerSessionManager.setSessionIdCookieEnabled(false);
        return customerSessionManager;
    }

    /* 自定义SecurityManager */
    @Bean
    public SecurityManager securityManager(Realm realm, CustomerSessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注入自定义的Realm
        securityManager.setRealm(realm);
        // 注入自定义会话管理器
        securityManager.setSessionManager(sessionManager);
        // 有时候报org.apache.shiro.UnavailableSecurityManagerException，需要做如下操作
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /* ShiroFilterFactoryBean */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 注入自定义SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 这里可以设置，需要登录才能访问的资源，重定向到登录页
        // 但是前后端分离的项目，不是直接提供登录页，而是提供响应信息，提示前端服务为用户返回登录页
        shiroFilterFactoryBean.setLoginUrl("/no_login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(getFilterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    /* 权限过滤器链 */
    private static Map<String, String> getFilterChainDefinitionMap() {
        /* 这里必须使用LinkedHashMap保持过滤器顺序 */
        Map<String, String> filters = new LinkedHashMap<>();
 /*
      配置权限，分以下3类：
      1.未登录就可以访问的，例如，登录、注册请求，以及其他可以公开访问的资源
      2.需要登录才能访问的
      3.需要权限标识才能访问的
  */
        // 登录&注册逻辑是必须要开放的
        filters.put("/login", "anon");
        filters.put("/registry", "anon");
        // 设置一个公开资源的路径，以后如果有资源，请求path可以pub开头
        filters.put("/pub/**", "anon");
        // 需要登录才能访问，除开上述的特殊配置项，其他资源至少都需要登录才能访问
        filters.put("/**", "authc");
        return filters;
    }

    /* 以下2项配置:实现通过注解进行权限配置和校验 */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator =
                new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}