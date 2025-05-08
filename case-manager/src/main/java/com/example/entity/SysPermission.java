package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父id
     */
    private Integer parent;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    private String type;

    /**
     * 路由组件
     */
    private String component;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysPermission> children;

    /**
     * 后端接口的权限标识
     */
    private String permission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<SysPermission> getChildren() {
        return children;
    }

    public void setChildren(List<SysPermission> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "SysPermission{" +
        "id = " + id +
        ", parent = " + parent +
        ", name = " + name +
        ", type = " + type +
        ", component = " + component +
        ", path = " + path +
        ", permission = " + permission +
        "}";
    }
}
