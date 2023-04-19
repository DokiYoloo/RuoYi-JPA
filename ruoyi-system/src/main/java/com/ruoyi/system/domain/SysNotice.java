package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.JpaBaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

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
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long noticeId;

    /**
     * 公告标题
     */
    @Column(length = 50, nullable = false)
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    @Column(columnDefinition = "char(1)", nullable = false)
    private String noticeType;

    /**
     * 公告内容
     */
    @Column(columnDefinition = "longblob")
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    @ColumnDefault("'0'")
    @Column(columnDefinition = "char(1)")
    private String status;

}
