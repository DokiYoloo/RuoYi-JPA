package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.domain.dto.SysOperLogDTO;
import org.springframework.data.domain.Page;

/**
 * 操作日志 服务层
 *
 * @author ruoyi
 */
public interface ISysOperLogService {
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    void insertOperlog(SysOperLogDTO operLog);

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    Page<SysOperLog> selectOperLogPaged(SysOperLogDTO operLog);

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    void deleteOperLogByIds(Long[] operIds);

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    SysOperLog selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
