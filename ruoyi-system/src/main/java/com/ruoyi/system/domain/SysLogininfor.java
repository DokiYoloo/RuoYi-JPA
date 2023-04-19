package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.JpaBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 系统访问记录表 sys_logininfor
 *
 * @author ruoyi
 */
@Entity
@Table(indexes = {
        @Index(columnList = "status"),
        @Index(columnList = "loginTime")
})
@Getter
@Setter
public class SysLogininfor {
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
    @ColumnDefault("''")
    @Column(length = 50)
    private String userName;

    /**
     * 登录状态 0成功 1失败
     */
    @ColumnDefault("'0'")
    @Column(columnDefinition = "char(1)")
    private String status;

    /**
     * 登录IP地址
     */
    @ColumnDefault("''")
    @Column(length = 128)
    private String ipaddr;

    /**
     * 登录地点
     */
    @ColumnDefault("''")
    @Column(length = 250)
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @ColumnDefault("''")
    @Column(length = 50)
    private String browser;

    /**
     * 操作系统
     */
    @ColumnDefault("''")
    @Column(length = 50)
    private String os;

    /**
     * 提示消息
     */
    @ColumnDefault("''")
    @Column(length = 300)
    private String msg;

    /**
     * 访问时间
     */
    @Column(columnDefinition = "DATETIME")
    private Date loginTime;

}
