package com.example.controller;

import com.example.util.ResultUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

/**
 * 全局的Controller层异常处理
 */
@RestControllerAdvice
public class ExceptionAdvice {

	/**
	 * 处理在controller中抛出该异常后的处理方法
	 * 捕获在校验权限时抛出的AuthorizationException异常
	 * @return 403
	 */
	@ExceptionHandler(AuthorizationException.class)
	public Object authorizationExceptionHandler(HttpServletRequest req) {
		return ResultUtil.unAuthorized(req.getServletPath());
	}
}