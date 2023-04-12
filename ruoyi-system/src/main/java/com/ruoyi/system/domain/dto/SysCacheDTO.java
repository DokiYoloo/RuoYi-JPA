package com.ruoyi.system.domain.dto;

import com.ruoyi.common.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 缓存信息
 *
 * @author ruoyi
 */
@Getter
@Setter
@NoArgsConstructor
public class SysCacheDTO {
    /**
     * 缓存名称
     */
    private String cacheName = "";

    /**
     * 缓存键名
     */
    private String cacheKey = "";

    /**
     * 缓存内容
     */
    private String cacheValue = "";

    /**
     * 备注
     */
    private String remark = "";

    public SysCacheDTO(String cacheName, String remark) {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public SysCacheDTO(String cacheName, String cacheKey, String cacheValue) {
        this.cacheName = StringUtils.replace(cacheName, ":", "");
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }

}
