package com.ruoyi.common.core.domain.convertor;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.dto.SysUserDTO;
import com.ruoyi.common.utils.bean.BeanUtils;

/**
 * 系统用户Convertor
 *
 * @author DokiYolo
 */
public class SysUserConvertor {

    public static SysUserDTO toDTO(SysUser sysUser) {
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyBeanProp(sysUserDTO, sysUser);
        return sysUserDTO;
    }

    public static SysUser toPO(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyBeanProp(sysUser, sysUserDTO);
        return sysUser;
    }

}
