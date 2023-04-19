package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
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
 * 字典类型表 sys_dict_type
 *
 * @author ruoyi
 */
@Entity
@Table
@Getter
@Setter
@ToString
public class SysDictType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long dictId;

    /**
     * 字典名称
     */
    @Column(length = 100)
    private String dictName;

    /**
     * 字典类型
     */
    @Column(length = 100, unique = true)
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @ColumnDefault("'0'")
    @Column(columnDefinition = "char(1)")
    private String status;

}
