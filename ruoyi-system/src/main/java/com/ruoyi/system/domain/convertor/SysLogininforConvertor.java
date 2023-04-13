package com.ruoyi.system.domain.convertor;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.dto.SysLogininforDTO;

/**
 * 系统访问记录Convertor
 *
 * @author DokiYolo
 */
public class SysLogininforConvertor {

    public static SysLogininforDTO toDTO(SysLogininfor logininfor) {
        SysLogininforDTO logininforDTO = new SysLogininforDTO();
        BeanUtils.copyBeanProp(logininforDTO, logininfor);
        return logininforDTO;
    }

    public static SysLogininfor toPO(SysLogininforDTO logininforDTO) {
        SysLogininfor logininfor = new SysLogininfor();
        BeanUtils.copyBeanProp(logininfor, logininforDTO);
        return logininfor;
    }

}
