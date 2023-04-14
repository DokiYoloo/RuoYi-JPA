package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.JpaBaseEntity;
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
 * 岗位表 sys_post
 *
 * @author ruoyi
 */
@Table
@Entity
@Getter
@Setter
@ToString
public class SysPost extends JpaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位序号
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long postId;

    /**
     * 岗位编码
     */
    @Column(length = 64)
    private String postCode;

    /**
     * 岗位名称
     */
    @Column(length = 50)
    private String postName;

    /**
     * 岗位排序
     */
    @Column
    private Integer postSort;

    /**
     * 状态（0正常 1停用）
     */
    @Column(length = 2)
    private String status;

}
