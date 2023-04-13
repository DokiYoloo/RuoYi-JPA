package com.ruoyi.system.domain.convertor;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.domain.dto.SysConfigDTO;

/**
 * 系统参数Convertor
 *
 * @author DokiYolo
 */
public class SysConfigConvertor {

    public static SysConfigDTO toDTO(SysConfig config) {
        SysConfigDTO sysConfigDTO = new SysConfigDTO();
        BeanUtils.copyBeanProp(sysConfigDTO, config);
        return sysConfigDTO;
    }

    public static SysConfig toPO(SysConfigDTO config) {
        SysConfig sysConfig = new SysConfig();
        BeanUtils.copyBeanProp(sysConfig, config);
        return sysConfig;
    }

}
