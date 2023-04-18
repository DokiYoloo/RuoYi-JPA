package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.JpaBaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 字典数据表 sys_dict_data
 *
 * @author ruoyi
 */
@Entity
@Table
@Getter
@Setter
@ToString
public class SysDictData extends JpaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long dictCode;

    /**
     * 字典排序
     */
    @Column
    private Long dictSort;

    /**
     * 字典标签
     */
    @Column(length = 100)
    private String dictLabel;

    /**
     * 字典键值
     */
    @Column(length = 100)
    private String dictValue;

    /**
     * 字典类型
     */
    @Column(length = 100)
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @Column(length = 100)
    private String cssClass;

    /**
     * 表格字典样式
     */
    @Column(length = 100)
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    @Column(columnDefinition = "char(1)")
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    @Column(columnDefinition = "char(1)")
    private String status;

}
