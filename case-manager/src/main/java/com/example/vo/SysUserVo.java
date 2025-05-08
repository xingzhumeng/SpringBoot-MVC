package com.example.vo;

import com.example.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVo extends SysUser {
	/* 用户名匹配规则：必须以字母开头，允许字母数字下划线，长度6~16个字符 */
	public static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{6,15}$";
	/* 密码匹配规则：必须包含大小写字母和数字的组合，可以使用特殊字符，长度8-24个字符 */
	public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,24}$";

	/**
	 * 旧密码
	 */
	private String oldPassword;

	/**
	 * 确认密码
	 */
	private String rePassword;
}