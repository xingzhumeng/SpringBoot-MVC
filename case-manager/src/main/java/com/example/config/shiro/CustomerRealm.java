package com.example.config.shiro;

import com.example.entity.SysUser;
import com.example.service.ISysPermissionService;
import com.example.service.ISysUserService;
import javax.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import java.util.Set;

/**
 * 自定义Realm
 */
public class CustomerRealm extends AuthorizingRealm {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysPermissionService sysPermissionService;

    /**
     * 登录认证
     * @param authenticationToken subject.login()方法中传递的token，包含用户名和密码
     * @return AuthenticationInfo对象包含数据库中的用户名、盐、密码信息，shiro将进行密码匹配校验
     * @throws AuthenticationException 抛出可能出现的各种异常，在subject.login()执行时捕获
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // System.out.println("登录认证");
        String username = (String)authenticationToken.getPrincipal(); // 用户名
        // 根据用户名查询用户
        SysUser sysUser = sysUserService.getSysUserByUsername(username);
        // System.out.println(sysUser);
        if (null == sysUser || null == sysUser.getPassword() || sysUser.getPassword().isEmpty()) {
            // 验证失败，抛出UnknownAccountException异常：用户名不存在
            return null;
        }

        /*
         * 方法接收四个参数：
         * 1. 用户名
         * 2. 密码
         * 3. 盐值，必须是ByteSource类型
         * 4. Realm名字，不用管
         * shiro会将这里的AuthenticationInfo与方法的参数AuthenticationToken中的密码进行匹配校验
         * 过程中可能抛出IncorrectCredentialsException和AuthenticationException(这里是自定义异常SaltInvalidateNumberException)
         */
        return new SimpleAuthenticationInfo(username, sysUser.getPassword(),
                ByteSource.Util.bytes(sysUser.getSalt().getBytes()), this.getClass().getName());
    }

    /**
     * 请求授权
     * 用户在访问加了@RequiresPermissions注解的Controller层方法时，被权限拦截器拦截后转到此处处理
     * @param principalCollection 用户信息
     * @return 方法需要返回用户的所有权限集合，Shiro自己判断是否拥有权限
     * 返回null或返回的集合不具有方法注解上标识的权限，则会抛出AuthorizationException异常
     * 需要在Controller层捕获该异常，以响应用户403状态
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // System.out.println("获取权限");
        // 可以获取到用户名
        String username = (String)principalCollection.getPrimaryPrincipal();
        // 根据用户名查询权限集合
        Set<String> permissions = sysPermissionService.getPermissionsByUsername(username);
        permissions.remove("");
        permissions.remove(null);
        // System.out.println(username + "权限:" +permissions);
        SimpleAuthorizationInfo authorizationInfo =
                new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(permissions);
        // return null，会抛出AuthorizationException异常
        // return的authorizationInfo中不包含请求方法指定的权限，也会抛出AuthorizationException异常
        return authorizationInfo;
    }
}