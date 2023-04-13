package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.QueryFenix;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.dto.SysNoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 公告Repository
 *
 * @author DokiYolo
 * @since 2023-04-13
 */
@Repository
public interface SysNoticeRepository extends JpaRepository<SysNotice, Long> {
    /**
     * 查询公告
     *
     * @param notice 公告信息
     * @return 公告
     */
    @QueryFenix
    Page<SysNotice> findPaged(SysNoticeDTO notice, Pageable pageable);
}
