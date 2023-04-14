package com.ruoyi.common.core.domain.convertor;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.dto.SysRoleDTO;
import com.ruoyi.common.core.domain.entity.dto.SysUserDTO;
import com.ruoyi.common.utils.bean.BeanUtils;

/**
 * @author DokiYolo
 * @since 2023-04-14
 */
public class SysRoleConvertor {

    public static SysRoleDTO toDTO(SysRole sysRole) {
        SysRoleDTO sysRoleDTO = new SysRoleDTO();
        BeanUtils.copyBeanProp(sysRoleDTO, sysRole);
        return sysRoleDTO;
    }

    public static SysRole toPO(SysRoleDTO sysRoleDTO) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyBeanProp(sysRole, sysRoleDTO);
        return sysRole;
    }

}
