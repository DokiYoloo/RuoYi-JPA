package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.JpaBaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 通知公告表 sys_notice
 *
 * @author ruoyi
 */
@Table
@Entity
@Getter
@Setter
@ToString
public class SysNotice extends JpaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;

}
