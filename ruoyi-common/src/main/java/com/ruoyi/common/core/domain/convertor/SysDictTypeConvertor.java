package com.ruoyi.common.core.domain.convertor;

import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.entity.dto.SysDictTypeDTO;
import com.ruoyi.common.utils.bean.BeanUtils;

/**
 * @author DokiYolo
 * @since 2023-04-18
 */
public class SysDictTypeConvertor {

    public static SysDictTypeDTO toDTO(SysDictType sysDictType) {
        SysDictTypeDTO sysDictTypeDTO = new SysDictTypeDTO();
        BeanUtils.copyBeanProp(sysDictTypeDTO, sysDictType);
        return sysDictTypeDTO;
    }

    public static SysDictType toPO(SysDictTypeDTO sysDictTypeDTO) {
        SysDictType sysDictType = new SysDictType();
        BeanUtils.copyBeanProp(sysDictType, sysDictTypeDTO);
        return sysDictType;
    }

}
