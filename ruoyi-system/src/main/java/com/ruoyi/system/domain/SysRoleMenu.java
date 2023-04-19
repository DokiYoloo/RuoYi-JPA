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
 * 角色和菜单关联 sys_role_menu
 *
 * @author ruoyi
 */
@Entity
@Table
@Getter
@Setter
@ToString
public class SysRoleMenu {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    /**
     * 角色ID
     */
    @Column(nullable = false)
    private Long roleId;

    /**
     * 菜单ID
     */
    @Column(nullable = false)
    private Long menuId;
}
