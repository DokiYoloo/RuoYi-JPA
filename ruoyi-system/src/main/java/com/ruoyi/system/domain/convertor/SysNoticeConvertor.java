package com.ruoyi.system.domain.convertor;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.dto.SysNoticeDTO;

/**
 * 通知公告Convertor
 *
 * @author DokiYolo
 */
public class SysNoticeConvertor {

    public static SysNoticeDTO toDTO(SysNotice sysNotice) {
        SysNoticeDTO sysNoticeDTO = new SysNoticeDTO();
        BeanUtils.copyBeanProp(sysNoticeDTO, sysNotice);
        return sysNoticeDTO;
    }

    public static SysNotice toPO(SysNoticeDTO sysNoticeDTO) {
        SysNotice sysNotice = new SysNotice();
        BeanUtils.copyBeanProp(sysNotice, sysNoticeDTO);
        return sysNotice;
    }

}
