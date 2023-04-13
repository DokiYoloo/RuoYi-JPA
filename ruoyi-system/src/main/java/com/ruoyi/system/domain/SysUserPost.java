package com.ruoyi.system.domain;

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
 * TODO change to many to one
 * 用户和岗位关联 sys_user_post
 *
 * @author ruoyi
 */
@Table
@Entity
@Getter
@Setter
@ToString
public class SysUserPost {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 岗位ID
     */
    @Column(nullable = false)
    private Long postId;
}
