package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.convertor.SysLogininforConvertor;
import com.ruoyi.system.domain.dto.SysLogininforDTO;
import com.ruoyi.system.repository.SysLogininforRepository;
import com.ruoyi.system.service.SysLogininforService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysLogininforServiceImpl implements SysLogininforService {
    private final SysLogininforRepository logininforRepo;

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(SysLogininforDTO logininfor) {
        SysLogininfor sysLogininfor = SysLogininforConvertor.toPO(logininfor);
        logininforRepo.save(sysLogininfor);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public Page<SysLogininfor> selectLogininforList(SysLogininforDTO logininfor) {
        Pageable pageable = logininfor.buildPageable();
        return logininforRepo.findPaged(logininfor, pageable);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     */
    @Override
    public void deleteLogininforByIds(Long[] infoIds) {
        logininforRepo.deleteAllById(Arrays.asList(infoIds));
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        logininforRepo.deleteAll();
    }
}
