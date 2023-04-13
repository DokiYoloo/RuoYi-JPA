package com.ruoyi.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    @Column(name = "create_by", length = 150)
    private String createBy;

    /**
     * 更新者
     */
    @Column(name = "update_by", length = 150)
    private String updateBy;

    /**
     * 备注
     */
    @Column(name = "remark", length = 350)
    private String remark;

    /**
     * 兼容原BaseEntity,这不是一个数据库字段
     */
    @Transient
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        return Optional.ofNullable(params)
                .orElseGet(HashMap::new);
    }

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
