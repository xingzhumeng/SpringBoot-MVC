package com.example.mapper;

import com.example.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
	/* 根据用户名获取权限集合 */
	Set<String> getPermissionsByUsername(String username);

	/* 根据用户名获取菜单集合 */
	List<SysPermission> selectMenusByUsername(String username);
}
