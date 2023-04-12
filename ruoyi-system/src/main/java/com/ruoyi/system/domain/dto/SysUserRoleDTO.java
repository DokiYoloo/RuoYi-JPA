package com.ruoyi.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户和角色关联DTO
 *
 * @author DokiYolo
 */
@Getter
@Setter
@ToString
public class SysUserRoleDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
