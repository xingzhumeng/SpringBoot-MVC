package com.example.controller;

import com.example.service.ISysPermissionService;
import com.example.service.ISysUserService;
import com.example.util.ResultUtil;
import com.example.vo.SysUserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
@RestController
public class SysUserController {

	@Autowired
	private ISysPermissionService sysPermissionService;
	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 登录
	 * @param data
	 * @return
	 */
	@PostMapping("/login")
	public Object login(@RequestBody Map<String, Object> data) {
		// System.out.println("登录");
		return sysUserService.login(data);
	}

	/**
	 * 获取登录用户的菜单数据
	 * @return
	 */
	@GetMapping("/user/menus")
	public Object getUserMenus() {
		// System.out.println("获取登录用户的菜单数据");
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			String username = (String)subject.getPrincipal();
			return ResultUtil.success(null, sysPermissionService.getMenuTreeByUsername(username));
		}
		return ResultUtil.error("系统异常，请联系管理员");
	}

	/**
	 * 获取登录的用户信息
	 * @return
	 */
	@GetMapping("/loginUser")
	public Object getUserInfo() {
		// System.out.println("获取登录用户信息");
		Subject subject = SecurityUtils.getSubject();
		return sysUserService.getLoginUser(subject);
	}

	/**
	 * 退出登录
	 * @return
	 */
	@GetMapping("/logout")
	public Object logout() {
		System.out.println("退出登录");
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		return ResultUtil.success("退出登录成功", null);
	}

	/**
	 * 注册
	 * @param sysUserVo
	 * @return
	 */
	@PostMapping("/registry")
	public Object registry(@RequestBody SysUserVo sysUserVo) {
		System.out.println("注册");
		return sysUserService.registry(sysUserVo);
	}

	/**
	 * 更新密码
	 * @param sysUserVo
	 * @return
	 */
	@PostMapping("/updatePassword")
	public Object updatePassword(@RequestBody SysUserVo sysUserVo) {
		System.out.println("修改密码");
		return sysUserService.updatePassword(sysUserVo);
	}
}
