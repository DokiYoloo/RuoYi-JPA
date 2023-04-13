package com.ruoyi.system.domain;

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
 * 参数配置表 sys_config
 *
 * @author ruoyi
 */
@Table
@Entity
@Getter
@Setter
@ToString
public class SysConfig extends JpaBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false)
    private Long configId;

    /**
     * 参数名称
     */
    @Column(length = 150)
    private String configName;

    /**
     * 参数键名
     */
    @Column(length = 200)
    private String configKey;

    /**
     * 参数键值
     */
    @Column(length = 400)
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @Column(length = 20)
    private String configType;

}
