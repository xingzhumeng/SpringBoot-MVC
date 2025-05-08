package com.example.service;

import com.example.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
public interface ISysPermissionService extends IService<SysPermission> {
	/* 根据用户名获取权限集合 */
	Set<String> getPermissionsByUsername(String username);

	/* 根据用户名获取菜单集合 */
	Map<String, Object> getMenuTreeByUsername(String username);
}
