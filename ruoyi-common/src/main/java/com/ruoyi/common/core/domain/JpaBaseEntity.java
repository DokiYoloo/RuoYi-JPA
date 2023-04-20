package com.ruoyi.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/**
 * The base class for all entity.
 *
 * @author DokiYolo
 */
@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JpaBaseEntity {

    @Column(name = "create_time", updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Date createTime;

    @Column(name = "update_time",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Date updateTime;

    /**
     * 创建者
     */
    @ColumnDefault("''")
    @Column(name = "create_by", length = 64)
    private String createBy;

    /**
     * 更新者
     */
    @ColumnDefault("''")
    @Column(name = "update_by", length = 64)
    private String updateBy;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date();
    }

    @PrePersist
    public void prePersist() {
        Date current = new Date();
        this.updateTime = current;
        this.createTime = current;
    }

}
