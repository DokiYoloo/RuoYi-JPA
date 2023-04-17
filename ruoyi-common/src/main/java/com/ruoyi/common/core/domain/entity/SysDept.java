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

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 部门表 sys_dept
 *
 * @author ruoyi
 */
@Entity
@Table
@Getter
@Setter
@ToString
public class SysDept extends BaseEntity {

    /**
     * 部门ID
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long deptId;

    /**
     * 父部门ID
     */
    @Column
    private Long parentId;

    /**
     * 祖级列表
     */
    @Column(length = 150)
    private String ancestors;

    /**
     * 部门名称
     */
    @Column(length = 50)
    private String deptName;

    /**
     * 显示顺序
     */
    @Column
    private Integer orderNum;

    /**
     * 负责人
     */
    @Column(length = 30)
    private String leader;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 30)
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    @Column(length = 2)
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Column(length = 2)
    private String delFlag;

}
