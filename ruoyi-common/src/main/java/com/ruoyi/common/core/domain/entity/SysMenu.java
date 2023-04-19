package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.JpaBaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 菜单权限表 sys_menu
 *
 * @author ruoyi
 */
@Entity
@Table
@Getter
@Setter
@ToString
public class SysMenu extends JpaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long menuId;

    /**
     * 菜单名称
     */
    @Column(length = 50)
    private String menuName;

    /**
     * 父菜单名称
     * TODO 看看有没有用
     */
    @Transient
    private String parentName;

    /**
     * 父菜单ID
     */
    @Column
    private Long parentId;

    /**
     * 显示顺序
     */
    @Column(columnDefinition = "int(4)")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @ColumnDefault("''")
    @Column(length = 200)
    private String path;

    /**
     * 组件路径
     */
    @Column(length = 200)
    private String component;

    /**
     * 路由参数
     */
    @Column(length = 200)
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    @ColumnDefault("1")
    @Column(columnDefinition = "int(1)")
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @ColumnDefault("0")
    @Column(columnDefinition = "int(1)")
    private String isCache;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @ColumnDefault("''")
    @Column(columnDefinition = "char(1)")
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    @ColumnDefault("'0'")
    @Column(columnDefinition = "char(1)")
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @ColumnDefault("'0'")
    @Column(columnDefinition = "char(1)")
    private String status;

    /**
     * 权限字符串
     */
    @Column(length = 100)
    private String perms;

    /**
     * 菜单图标
     */
    @ColumnDefault("'#'")
    @Column(length = 100)
    private String icon;

}
