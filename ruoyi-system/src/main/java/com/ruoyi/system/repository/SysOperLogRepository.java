package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.QueryFenix;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.domain.dto.SysOperLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 操作日志记录Repository
 *
 * @author DokiYolo
 */
@Repository
public interface SysOperLogRepository extends JpaRepository<SysOperLog, Long> {
    @QueryFenix
    Page<SysOperLog> findPaged(SysOperLogDTO operLog, Pageable pageable);
}
