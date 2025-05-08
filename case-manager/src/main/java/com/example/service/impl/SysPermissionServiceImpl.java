package com.example.service.impl;

import com.example.entity.SysPermission;
import com.example.mapper.SysPermissionMapper;
import com.example.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission>
		implements ISysPermissionService {

	/* 根据用户名获取权限集合 */
	@Override
	public Set<String> getPermissionsByUsername(String username) {
		return getBaseMapper().getPermissionsByUsername(username);
	}

	/* 根据用户名获取菜单集合 */
	@Override
	public Map<String, Object> getMenuTreeByUsername(String username) {
		SysPermissionMapper sysPermissionMapper = getBaseMapper();
		// 获取用户的所有菜单和目录
		List<SysPermission> sysPermissions = sysPermissionMapper.selectMenusByUsername(username);
		// 构建目录菜单树
		List<SysPermission> menuList = toMenuTree(0, sysPermissions);
		// 去除其中"M"标识的目录，这部分不用在生成前端router路由对象
		sysPermissions.removeIf(sysPermission -> "M".equals(sysPermission.getType()));
		// 获取所有的path集合
		Map<String, Object> data = new HashMap<>();
		// 目录菜单集合，数据是树形结构
		data.put("menus", menuList);
		// 菜单集合，不包含目录，数据是直列集合，用于前端生成动态路由/动态标签等
		data.put("routers", sysPermissions);
		// path关键字集合，数据是直列集合，用于前端生成对应的路由path
		data.put("paths", getPathList(sysPermissions));
		return data;
	}

	/* 构建菜单树 */
	private List<SysPermission> toMenuTree(Integer parent, List<SysPermission> sysPermissions) {
		List<SysPermission> menuTree = new ArrayList<>();
		for (SysPermission sysPermission : sysPermissions) {
			if (parent.equals(sysPermission.getParent())) {
				sysPermission.setChildren(toMenuTree(sysPermission.getId(), sysPermissions));
				menuTree.add(sysPermission);
			}
		}
		// 构造的树结构，已破坏原本集合的顺序，需要重新排序，使用id升序
		menuTree.sort(Comparator.comparingInt(SysPermission::getId));
		return menuTree;
	}

	/* 获取path集合 */
	private List<String> getPathList(List<SysPermission> sysPermissions) {
		List<String> pathList = new ArrayList<>();
		if (sysPermissions != null) {
			for (SysPermission sysPermission : sysPermissions) {
				pathList.add(sysPermission.getPath());
			}
		}
		return pathList;
	}
}
