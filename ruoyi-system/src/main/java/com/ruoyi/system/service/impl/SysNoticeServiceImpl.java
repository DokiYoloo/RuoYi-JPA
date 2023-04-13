package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.convertor.SysNoticeConvertor;
import com.ruoyi.system.domain.dto.SysNoticeDTO;
import com.ruoyi.system.repository.SysNoticeRepository;
import com.ruoyi.system.service.ISysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 公告 服务层实现
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl implements ISysNoticeService {
    private final SysNoticeRepository noticeRepo;

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId) {
        return noticeRepo.findById(noticeId).orElse(null);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public Page<SysNotice> selectNoticePaged(SysNoticeDTO notice) {
        Pageable pageable = notice.buildPageable();
        return noticeRepo.findPaged(notice, pageable);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     */
    @Override
    public void insertNotice(SysNoticeDTO notice) {
        SysNotice sysNotice = SysNoticeConvertor.toPO(notice);
        sysNotice.setCreateBy(getUsername());
        noticeRepo.save(sysNotice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     */
    @Override
    public void updateNotice(SysNoticeDTO notice) {
        SysNotice sysNotice = SysNoticeConvertor.toPO(notice);
        sysNotice.setUpdateBy(getUsername());
        noticeRepo.save(sysNotice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     */
    @Override
    public void deleteNoticeById(Long noticeId) {
        noticeRepo.deleteById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     */
    @Override
    public void deleteNoticeByIds(Long[] noticeIds) {
        noticeRepo.deleteAllByIdInBatch(Arrays.asList(noticeIds));
    }
}
