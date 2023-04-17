package com.ruoyi.common.core.page;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 需要分页的都可以继承这个类，这个类是
 * 原RuoYi BasePage类的一个适配类。
 * 参数与其一致，为了不影响前端。
 *
 * @author DokiYolo
 * @since 2023-04-12
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class BasePageQuery extends BaseEntity {

    /**
     * 当前记录起始索引
     */
    private Integer pageNum = 1;

    /**
     * 每页显示记录数
     */
    private Integer pageSize = Integer.MAX_VALUE;

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private String isAsc = "asc";

    /**
     * perhaps a legacy issue?
     */
    public void setIsAsc(String isAsc) {
        if (StringUtils.isNotEmpty(isAsc)) {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }

    public Pageable buildPageable() {
        Sort sort = null;
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(isAsc);
        } catch (Exception e) {
            direction = Sort.Direction.ASC;
        }
        if (StringUtils.isNotEmpty(this.orderByColumn)) {
            String[] sorts = this.orderByColumn.split(",");
            sort = Sort.by(direction, sorts);
            return PageRequest.of(Math.max(this.pageNum - 1, 0), this.pageSize, sort);
        }
        return buildNoSortPageable();
    }

    public Pageable buildNativeSqlPageable() {
        Sort sort = null;
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(isAsc);
        } catch (Exception e) {
            direction = Sort.Direction.DESC;
        }
        if (StringUtils.isNotEmpty(this.orderByColumn)) {
            sort = Sort.by(direction, StringUtils.toUnderScoreCase(this.orderByColumn));
            return PageRequest.of(Math.max(this.pageNum - 1, 0), this.pageSize, sort);
        }
        return buildNoSortPageable();
    }

    public Pageable buildNoSortPageable() {
        return PageRequest.of(Math.max(this.pageNum - 1, 0), this.pageSize);
    }

    public <T extends BasePageQuery> T queryMax() {
        this.pageNum = 0;
        this.pageSize = 999999;
        return (T) this;
    }

}
