package com.ruoyi.system.domain.convertor;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.domain.dto.SysOperLogDTO;

/**
 * 操作日志记录Convertor
 *
 * @author DokiYolo
 */
public class SysOperLogConvertor {

    public static SysOperLogDTO toDTO(SysOperLog sysOperLog) {
        SysOperLogDTO sysOperLogDTO = new SysOperLogDTO();
        BeanUtils.copyBeanProp(sysOperLogDTO, sysOperLog);
        return sysOperLogDTO;
    }

    public static SysOperLog toPO(SysOperLogDTO sysOperLogDTO) {
        SysOperLog sysOperLog = new SysOperLog();
        BeanUtils.copyBeanProp(sysOperLog, sysOperLogDTO);
        return sysOperLog;
    }

}
