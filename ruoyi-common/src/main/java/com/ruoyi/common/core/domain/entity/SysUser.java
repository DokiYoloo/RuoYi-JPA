package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Table
@Entity
@Getter
@Setter
@ToString
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 部门对象
     */
    private SysDept dept;

    /**
     * 角色对象
     */
    @Deprecated
    @Transient
    private List<SysRole> roles;

    /**
     * 角色组
     */
    @Deprecated
    @Transient
    private Long[] roleIds;

    /**
     * 岗位组
     */
    @Deprecated
    @Transient
    private Long[] postIds;

    /**
     * 角色ID
     */
    private Long roleId;

    public SysUser() {

    }

    public SysUser(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

}
