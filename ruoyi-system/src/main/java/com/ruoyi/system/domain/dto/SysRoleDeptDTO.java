package com.ruoyi.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色和部门关联DTO
 *
 * @author DokiYolo
 */
@Getter
@Setter
@ToString
public class SysRoleDeptDTO {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;
}
