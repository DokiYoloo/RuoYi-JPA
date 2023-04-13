package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.JpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 操作日志记录表 oper_log
 *
 * @author ruoyi
 */
@Table
@Entity
@Getter
@Setter
public class SysOperLog extends JpaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long operId;

    /**
     * 操作模块
     */
    @Column(length = 100)
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @Column
    private Integer businessType;

    /**
     * 请求方法
     */
    @Column(length = 300)
    private String method;

    /**
     * 请求方式
     */
    @Column(length = 10)
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @Column
    private Integer operatorType;

    /**
     * 操作人员
     */
    @Column(length = 100)
    private String operName;

    /**
     * 部门名称
     */
    @Column(length = 100)
    private String deptName;

    /**
     * 请求url
     */
    @Column(length = 300)
    private String operUrl;

    /**
     * 操作地址
     */
    @Column(length = 80)
    private String operIp;

    /**
     * 操作地点
     */
    @Column(length = 150)
    private String operLocation;

    /**
     * 请求参数
     */
    @Column(length = 300)
    private String operParam;

    /**
     * 返回参数
     */
    @Lob
    @Column
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    @Column
    private Integer status;

    /**
     * 错误消息
     */
    @Column(length = 2000)
    private String errorMsg;

    /**
     * 操作时间
     */
    @Column
    private Date operTime;

    /**
     * 消耗时间
     */
    @Column
    private Long costTime;

}
