package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.dto.SysNoticeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 公告 服务层
 *
 * @author ruoyi
 */
public interface SysNoticeService {
    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    SysNotice selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    Page<SysNotice> selectNoticePaged(SysNoticeDTO notice);

    /**
     * 新增公告
     *
     * @param notice 公告信息
     */
    void insertNotice(SysNoticeDTO notice);

    /**
     * 修改公告
     *
     * @param notice 公告信息
     */
    void updateNotice(SysNoticeDTO notice);

    /**
     * 删除公告信息
     *
     * @param noticeId 公告ID
     */
    void deleteNoticeById(Long noticeId);

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     */
    void deleteNoticeByIds(Long[] noticeIds);
}
