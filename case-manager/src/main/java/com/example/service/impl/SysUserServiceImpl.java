package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.SysUser;
import com.example.entity.SysUserRole;
import com.example.mapper.SysUserMapper;
import com.example.mapper.SysUserRoleMapper;
import com.example.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.util.CredentialUtils;
import com.example.util.DateUtil;
import com.example.util.ResultUtil;
import com.example.vo.SysUserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

	/* 根据用户名获取用户信息 */
	@Override
	public SysUser getSysUserByUsername(String username) {
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(SysUser::getUsername, username);
		return getOne(queryWrapper);
	}

	/* 执行登录认证 */
	@Override
	public Object login(Map<String, Object> data) {
		String username = data.get("username").toString();
		String password = data.get("password").toString();
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		if (subject.isAuthenticated()) {
			Map<String, Object> result = ResultUtil.isAuthenticated();
			result.put("sessionId", subject.getSession().getId());
			return result;
		}
		try {
			// 执行登录认证
			subject.login(token);
			Map<String, Object> result = ResultUtil.success("登录成功", null);
			result.put("sessionId", subject.getSession().getId());
			return result;
		} catch (UnknownAccountException exception) {
			return ResultUtil.fail("用户名不存在");
		} catch (IncorrectCredentialsException exception) {
			return ResultUtil.fail("密码错误");
		} catch (AuthenticationException exception) {
			return ResultUtil.fail("系统错误，请联系管理员");
		}
	}

	/* 获取当前登录用户的相关信息 */
	@Override
	public Object getLoginUser(Subject subject) {
		if (subject.isAuthenticated()) {
			Map<String, Object> data = new HashMap<>();
			String username = (String)subject.getPrincipal();
			SysUser loginUser = getSysUserByUsername(username);
			/* 真实业务中，不应该共用方法，应该在dao层就控制查询的结果集，减少数据库开销 */
			loginUser.setPassword(null); // 密码置为空
			loginUser.setSalt(null); // 盐置为空
			data.put("loginUser", loginUser);
			// 会话数据
			Map<String, Object> sessionData = new HashMap<>();
			Session session = subject.getSession();
			sessionData.put("startLogin", DateUtil.toTimeStr(session.getStartTimestamp()));
			sessionData.put("lastAccess", DateUtil.toTimeStr(session.getLastAccessTime()));
			sessionData.put("timeOut", session.getTimeout());
			data.put("session", sessionData);
			return ResultUtil.success(null, data);
		}
		return ResultUtil.error("系统异常，请联系管理员");
	}

	/* 注册 */
	@Transactional
	@Override
	public Object registry(SysUserVo sysUserVo) {
		// 校验数据
		Object validate = validate(sysUserVo);
		if (validate != null) {
			return validate;
		}
		// 判断重复
		SysUser username = getSysUserByUsername(sysUserVo.getUsername());
		if(username != null) {
			return ResultUtil.error("用户名已存在");
		}
		// 产生一个盐值
		String salt = CredentialUtils.randomSalt();
		sysUserVo.setSalt(salt);
		// 根据原密码和盐值加密
		String password = CredentialUtils.cryptPassword(sysUserVo.getPassword(), salt);
		sysUserVo.setPassword(password);
		sysUserVo.setId(null);
		// 增加一个用户，同时增加一个角色，需要使用事务
		if(addUserAndRole(sysUserVo)) {
			return ResultUtil.success("注册成功", null);
		} else {
			return ResultUtil.error("系统错误，请联系管理员");
		}
	}

	/* 事务：增加一个用户并分配普通用户角色 */
	/* 多条sql同时成功/失败，无关乎顺序 */
	@Transactional
	@Override
	public boolean addUserAndRole(SysUser sysUser) {
		boolean save = save(sysUser);
		/* 执行了save方法后，会生成数据库自增的id放置在sysUser对象中 */
		// System.out.println(sysUser.getId());
		sysUserRoleMapper.insert(new SysUserRole(sysUser.getId(), 2));
		return save;
	}

	/* 修改密码 */
	@Override
	public Object updatePassword(SysUserVo sysUserVo) {
		// 判断是否已登录
		Subject subject = SecurityUtils.getSubject();
		if (!subject.isAuthenticated()) {
			return ResultUtil.unAuthenticated();
		}
		// 原密码不能为空
		if (sysUserVo.getOldPassword() == null || sysUserVo.getOldPassword().isEmpty()) {
			return ResultUtil.error("修改密码失败，原密码错误");
		}
		// 2次密码是否一致
		Object validatePassword = validatePassword(sysUserVo);
		if (validatePassword != null) {
			return validatePassword;
		}
		// 新密码是否有变化
		if (sysUserVo.getPassword().equals(sysUserVo.getOldPassword())) {
			return ResultUtil.error("修改密码失败，新密码不能与原密码一致");
		}
		// 获取登录用户
		String username = (String)subject.getPrincipal();
		SysUser sysUser = getSysUserByUsername(username);
		// 数据库中的盐值
		String salt = sysUser.getSalt();
		String cryptPwd = CredentialUtils
				.cryptPassword(sysUserVo.getOldPassword(), salt);
		// 校验原密码是否正确
		if (!cryptPwd.equals(sysUser.getPassword())) {
			return ResultUtil.error("修改密码失败，原密码错误");
		}
		// 新密码加密
		cryptPwd = CredentialUtils
				.cryptPassword(sysUserVo.getPassword(), salt);
		sysUser.setPassword(cryptPwd);
		// 更新数据库
		// sysUser需要保证除password以外，其他属性不变
		if (updateById(sysUser)) {
			// 修改完后在服务端注销
			subject.logout();
			return ResultUtil.success("更新密码成功，请重新登录", null);
		} else {
			return ResultUtil.error("系统异常，请联系管理员");
		}
	}

	/* 校验数据 */
	private Object validate(SysUserVo sysUserVo) {
		// 用户名不能为空
		if (sysUserVo.getUsername() == null) {
			return ResultUtil.error("用户名不能为空");
		}
		// 校验账号格式：必须以字母开头，允许字母数字下划线，长度6~16个字符
		if (!sysUserVo.getUsername().matches(SysUserVo.USERNAME_REGEX)) {
			return ResultUtil.error("用户名不合规，必须以字母开头，允许字母数字下划线，长度6~16个字符");
		}
		// 校验密码
		Object validatePassword = validatePassword(sysUserVo);
		if (validatePassword != null) {
			return validatePassword;
		}
		// 用户名字不能为空
		if (sysUserVo.getRealName() == null || sysUserVo.getRealName().isEmpty()) {
			return ResultUtil.error("用户昵称不能为空");
		}
		return null;
	}

	/* 校验密码 */
	private Object validatePassword(SysUserVo sysUserVo) {
		// 密码不能为空
		if (sysUserVo.getPassword() == null) {
			return ResultUtil.error("密码不能为空");
		}
		// 确认密码不能为空
		if (sysUserVo.getRePassword() == null) {
			return ResultUtil.error("确认密码不能为空");
		}
		// 2次密码是否一致
		if (!sysUserVo.getPassword().equals(sysUserVo.getRePassword())) {
			return ResultUtil.error("请确认2次输入的密码一致");
		}
		// 密码格式：必须包含大小写字母和数字的组合，可以使用特殊字符，长度8-24个字符
		if (!sysUserVo.getPassword().matches(SysUserVo.PASSWORD_REGEX)) {
			return ResultUtil.error("密码不合规，必须包含大小写字母和数字的组合，可以使用特殊字符，长度8-24个字符");
		}
		return null;
	}


}
