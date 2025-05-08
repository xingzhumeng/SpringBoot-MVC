package com.example.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 自定义盐值位数异常：盐值必须是16个字符
 */
public class SaltInvalidateNumberException extends AuthenticationException {

    public SaltInvalidateNumberException(String message) {
        super(message);
    }
}
