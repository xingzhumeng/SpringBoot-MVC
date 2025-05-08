package com.example.config.shiro;

import com.example.util.CredentialUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.lang.codec.CodecSupport;
import org.apache.shiro.lang.util.ByteSource;


/**
 * 自定义密码匹配器
 */

/**
 * 自定义密码匹配器
 */
public class AddSaltCredentialsMatcher extends CodecSupport
        implements CredentialsMatcher {

    /**
     * 自定义密码校验规则
     *
     * @param authenticationToken 是用户在登录的时候，构建UsernameAndPasswordToken 其中封装了 name和password
     * @param authenticationInfo  是CustomerRealm中 doGetAuthenticationInfo 中方法的返回值。
     * @return 密码匹配结果 方法结果为false，认证过程将抛出IncorrectCredentialsException异常（密码不正确）
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken,
                                      AuthenticationInfo authenticationInfo) {
        System.out.println("自定义密码校验");
        Object tokenCredentials = authenticationToken.getCredentials(); // token中的密码
        Object infoCredentials = authenticationInfo.getCredentials(); // 登录时查询到的数据库密码
        if (authenticationInfo instanceof SimpleAuthenticationInfo) {
            /*
             * 通过这种方式获取到是base64编码后的数据
             * ByteSource salt = simpleAuthenticationInfo.getCredentialsSalt();
             */
            ByteSource credentialsSalt = ((SimpleAuthenticationInfo) authenticationInfo).getCredentialsSalt();
            // isByteSource：判断类型是否属于byte[]或者char[]或者String
            if (isByteSource(credentialsSalt) && isByteSource(infoCredentials) && isByteSource(tokenCredentials)) {
                byte[] salt = toBytes(credentialsSalt);  // 获取盐值，本身还是数据库表中存储的盐值
                byte[] tokenBytes = toBytes(tokenCredentials);   // 获取到token，也就是用户提交过来的密码
                byte[] accountBytes = toBytes(infoCredentials);  // 获取到数据库密码
                // 加盐处理后的密码
                String newPwd = CredentialUtils.cryptPassword(new String(tokenBytes), new String(salt));
                System.out.println(new String(salt) + "=====" + new String(tokenBytes) + "===加密后密码：" + newPwd);
                return newPwd.equals(new String(accountBytes));
            }
        }
        return false;
    }
}
