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
import java.util.Date;

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
public class SysUser extends JpaBaseEntity {
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
    @Column
    private Long deptId;

    /**
     * 用户账号
     */
    @Column(length = 30, nullable = false)
    private String userName;

    /**
     * 用户昵称
     */
    @Column(length = 30, nullable = false)
    private String nickName;

    /**
     * 用户邮箱
     */
    @Column(length = 50)
    private String email;

    /**
     * 手机号码
     */
    @Column(length = 20)
    private String phonenumber;

    /**
     * 用户性别
     */
    @ColumnDefault("'0'")
    @Column(columnDefinition = "char(1)")
    private String sex = "0";

    /**
     * 用户头像
     */
    @Column(length = 150)
    private String avatar;

    /**
     * 密码
     */
    @Column(length = 100)
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ColumnDefault("'0'")
    @Column(columnDefinition = "char(1)")
    private String status = "0";

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ColumnDefault("'0'")
    @Column(columnDefinition = "char(1)")
    private String delFlag;

    /**
     * 最后登录IP
     */
    @Column(length = 128)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Column(columnDefinition = "DATETIME")
    private Date loginDate;

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
