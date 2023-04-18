package com.ruoyi.common.core.domain.convertor;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.dto.SysDictDataDTO;
import com.ruoyi.common.utils.bean.BeanUtils;

/**
 * @author DokiYolo
 * @since 2023-04-18
 */
public class SysDictDataConvertor {

    public static SysDictDataDTO toDTO(SysDictData sysDictData) {
        SysDictDataDTO sysDictDataDTO = new SysDictDataDTO();
        BeanUtils.copyBeanProp(sysDictDataDTO, sysDictData);
        return sysDictDataDTO;
    }

    public static SysDictData toPO(SysDictDataDTO sysDictDataDTO) {
        SysDictData sysDictData = new SysDictData();
        BeanUtils.copyBeanProp(sysDictData, sysDictDataDTO);
        return sysDictData;
    }

}
