package com.ruoyi.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户和岗位关联DTO
 *
 * @author DokiYolo
 */
@Getter
@Setter
@ToString
public class SysUserPostDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;
}
