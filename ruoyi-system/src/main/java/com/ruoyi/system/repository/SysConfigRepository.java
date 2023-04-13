package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.QueryFenix;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.domain.dto.SysConfigDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 参数配置 数据层
 *
 * @author DokiYolo
 */
@Repository
public interface SysConfigRepository extends JpaRepository<SysConfig, Long> {

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @QueryFenix
    Page<SysConfig> findConfigPaged(SysConfigDTO config, Pageable pageable);

    /**
     * 根据configKey查询
     *
     * @param configKey 参数配置信息
     * @return 参数配置
     */
    @Query("from SysConfig where configKey = ?1")
    SysConfig findByKey(String configKey);

}
