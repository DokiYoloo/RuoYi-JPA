package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.QueryFenix;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.dto.SysLogininforDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 参数配置Repository
 *
 * @author DokiYolo
 */
@Repository
public interface SysLogininforRepository extends JpaRepository<SysLogininfor, Long> {
    /**
     * 查询系统登录日志
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @QueryFenix
    Page<SysLogininfor> findPaged(SysLogininforDTO logininfor, Pageable pageable);
}
