package com.ruoyi.system.domain.convertor;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.dto.SysDeptDTO;
import com.ruoyi.common.utils.bean.BeanUtils;

/**
 * 部门Convertor
 *
 * @author DokiYolo
 * @since 2023-04-17
 */
public class SysDeptConvertor {

    public static SysDeptDTO toDTO(SysDept sysDept) {
        SysDeptDTO sysDeptDTO = new SysDeptDTO();
        BeanUtils.copyBeanProp(sysDeptDTO, sysDept);
        return sysDeptDTO;
    }

    public static SysDept toPO(SysDeptDTO sysDeptDTO) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyBeanProp(sysDept, sysDeptDTO);
        return sysDept;
    }

}
