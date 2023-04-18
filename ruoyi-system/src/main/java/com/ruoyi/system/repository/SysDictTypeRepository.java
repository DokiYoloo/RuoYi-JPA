package com.ruoyi.system.repository;

import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.entity.dto.SysDictTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysDictTypeRepository extends JpaRepository<SysDictType, Long> {
    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    Page<SysDictType> findDictTypePaged(SysDictTypeDTO dictType, Pageable pageable);

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Query("from SysDictType where dictId = ?1")
    SysDictType findDictTypeById(Long dictId);

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    SysDictType findFirstByDictType(String dictType);
}
