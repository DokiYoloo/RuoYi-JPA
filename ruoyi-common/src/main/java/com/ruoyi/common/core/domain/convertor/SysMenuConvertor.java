package com.ruoyi.common.core.domain.convertor;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.dto.SysMenuDTO;
import com.ruoyi.common.core.domain.entity.dto.SysRoleDTO;
import com.ruoyi.common.utils.bean.BeanUtils;

/**
 * @author DokiYolo
 * @since 2023-04-14
 */
public class SysMenuConvertor {

    public static SysMenuDTO toDTO(SysMenu sysMenu) {
        SysMenuDTO sysMenuDTO = new SysMenuDTO();
        BeanUtils.copyBeanProp(sysMenuDTO, sysMenu);
        return sysMenuDTO;
    }

    public static SysMenu toPO(SysMenuDTO sysMenuDTO) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyBeanProp(sysMenu, sysMenuDTO);
        return sysMenu;
    }

}
