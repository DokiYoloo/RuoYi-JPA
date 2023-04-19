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
    @ColumnDefault("''")
    @Column(length = 100)
    private String configName;

    /**
     * 参数键名
     */
    @ColumnDefault("''")
    @Column(length = 100)
    private String configKey;

    /**
     * 参数键值
     */
    @ColumnDefault("''")
    @Column(length = 500)
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @ColumnDefault("'N'")
    @Column(columnDefinition = "char(1)")
    private String configType;

}
