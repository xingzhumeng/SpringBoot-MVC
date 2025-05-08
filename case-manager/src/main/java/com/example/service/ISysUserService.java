package com.example.service;

import com.example.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.SysUserVo;
import org.apache.shiro.subject.Subject;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
public interface ISysUserService extends IService<SysUser> {

	/* 根据用户名获取用户信息 */
	SysUser getSysUserByUsername(String username);

	/* 执行登录认证 */
	Object login(Map<String, Object> data);

	/* 获取当前登录用户的相关信息 */
	Object getLoginUser(Subject subject);

	/* 注册 */
	Object registry(SysUserVo sysUserVo);

	/* 事务：增加一个用户并分配普通用户角色 */
	boolean addUserAndRole(SysUser sysUser);

	/* 修改密码 */
	Object updatePassword(SysUserVo sysUserVo);
}
