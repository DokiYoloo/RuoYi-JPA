package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
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

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 角色表 sys_role
 *
 * @author ruoyi
 */
@Table
@Entity
@Getter
@Setter
@ToString
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long roleId;

    /**
     * 角色名称
     */
    @Column(length = 50)
    private String roleName;

    /**
     * 角色权限
     */
    @Column(length = 50)
    private String roleKey;

    /**
     * 角色排序
     */
    @Column
    private Integer roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    @Column(length = 2)
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    @ColumnDefault("1")
    @Column
    private boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    @ColumnDefault("1")
    @Column
    private boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    @Column(length = 2)
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Column(length = 2)
    private String delFlag;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    @Transient
    private boolean flag = false;

    public SysRole() {

    }

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

}
