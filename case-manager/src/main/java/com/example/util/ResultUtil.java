package com.example.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应结果工具类
 */
public class ResultUtil {
    /* 成功状态码 */
    private static final Integer SUCCESS = 200;
    /* 错误状态码 */
    private static final Integer ERROR = 500;
    /* 失败状态码 */
    private static final Integer FAIL = -1;
    /* 重复登录 */
    private static final Integer IS_AUTHENTICATED = 201;
    private static final String IS_AUTHENTICATED_MSG = "您已经登录了";
    /* 未登录 */
    private static final Integer UN_AUTHENTICATED = 401;
    private static final String UN_AUTHENTICATED_MSG = "您尚未登录";
    /* 无权限 */
    private static final Integer UN_AUTHORIZED = 403;
    private static final String UN_AUTHORIZED_MSG = "您没有权限访问该资源";

    /**
     * 成功响应，状态码200
     *
     * @return
     */
    public static Map<String, Object> success() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", SUCCESS);
        return result;
    }

    /**
     * 成功响应，状态码200，携带msg或data
     *
     * @return
     */
    public static Map<String, Object> success(String msg, Object data) {
        Map<String, Object> result = success();
        if (msg != null && !msg.isEmpty()) {
            result.put("msg", msg);
        }
        if (data != null) {
            result.put("data", data);
        }
        return result;
    }

    /**
     * 失败响应，状态码500，携带msg
     *
     * @return
     */
    public static Map<String, Object> fail(String msg) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", FAIL);
        result.put("msg", msg);
        return result;
    }

    /**
     * 异常响应，状态码500，携带msg
     *
     * @return
     */
    public static Map<String, Object> error(String msg) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", ERROR);
        result.put("msg", msg);
        return result;
    }

    /**
     * 重复登录提示，状态码201，携带msg
     *
     * @return
     */
    public static Map<String, Object> isAuthenticated() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", IS_AUTHENTICATED);
        result.put("msg", IS_AUTHENTICATED_MSG);
        return result;
    }

    /**
     * 认证提示，状态码401，携带msg
     *
     * @return
     */
    public static Map<String, Object> unAuthenticated() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", UN_AUTHENTICATED);
        result.put("msg", UN_AUTHENTICATED_MSG);
        return result;
    }

    /**
     * 权限异常，状态码403，携带msg和path
     *
     * @return
     */
    public static Map<String, Object> unAuthorized(String path) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", UN_AUTHORIZED);
        result.put("msg", UN_AUTHORIZED_MSG);
        if (path != null && !path.isEmpty()) {
            result.put("path", path);
        }
        return result;
    }
}
