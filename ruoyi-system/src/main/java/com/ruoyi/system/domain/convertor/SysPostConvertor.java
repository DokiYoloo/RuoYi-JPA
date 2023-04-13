package com.ruoyi.system.domain.convertor;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.dto.SysPostDTO;

/**
 * 岗位Convertor
 *
 * @author DokiYolo
 */
public class SysPostConvertor {

    public static SysPostDTO toDTO(SysPost sysPost) {
        SysPostDTO sysPostDTO = new SysPostDTO();
        BeanUtils.copyBeanProp(sysPostDTO, sysPost);
        return sysPostDTO;
    }

    public static SysPost toPO(SysPostDTO sysPostDTO) {
        SysPost sysPost = new SysPost();
        BeanUtils.copyBeanProp(sysPost, sysPostDTO);
        return sysPost;
    }

}
