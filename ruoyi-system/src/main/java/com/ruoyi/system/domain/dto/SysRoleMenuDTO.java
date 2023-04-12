package com.ruoyi.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色和菜单关联DTO
 *
 * @author DokiYolo
 */
@Getter
@Setter
@ToString
public class SysRoleMenuDTO {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
