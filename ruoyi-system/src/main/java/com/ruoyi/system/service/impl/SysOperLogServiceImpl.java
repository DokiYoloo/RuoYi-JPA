package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.domain.convertor.SysOperLogConvertor;
import com.ruoyi.system.domain.dto.SysOperLogDTO;
import com.ruoyi.system.repository.SysOperLogRepository;
import com.ruoyi.system.service.ISysOperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 操作日志 服务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysOperLogServiceImpl implements ISysOperLogService {
    private final SysOperLogRepository operLogRepo;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLogDTO operLog) {
        SysOperLog sysOperLog = SysOperLogConvertor.toPO(operLog);
        sysOperLog.setCreateBy(getUsername());
        operLogRepo.save(sysOperLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public Page<SysOperLog> selectOperLogPaged(SysOperLogDTO operLog) {
        Pageable pageable = operLog.buildPageable();
        return operLogRepo.findPaged(operLog, pageable);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     */
    @Override
    public void deleteOperLogByIds(Long[] operIds) {
        operLogRepo.deleteAllById(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return operLogRepo.findById(operId).orElse(null);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        operLogRepo.deleteAll();
    }
}
