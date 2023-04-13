package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.JpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 系统访问记录表 sys_logininfor
 *
 * @author ruoyi
 */
@Entity
@Table
@Getter
@Setter
public class SysLogininfor extends JpaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long infoId;

    /**
     * 用户账号
     */
    @Column(length = 80)
    private String userName;

    /**
     * 登录状态 0成功 1失败
     */
    @Column(length = 2)
    private String status;

    /**
     * 登录IP地址
     */
    @Column(length = 30)
    private String ipaddr;

    /**
     * 登录地点
     */
    @Column(length = 80)
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @Column(length = 50)
    private String browser;

    /**
     * 操作系统
     */
    @Column(length = 70)
    private String os;

    /**
     * 提示消息
     */
    @Column(length = 150)
    private String msg;

    /**
     * 访问时间
     */
    @Column
    private Date loginTime;

}
