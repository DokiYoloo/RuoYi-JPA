package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.dto.SysDictDataDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author DokiYolo
 */
@Repository
public interface SysDictDataRepository extends FenixJpaRepository<SysDictData, Long> {
    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    Page<SysDictData> findDictDataPaged(SysDictDataDTO dictData, Pageable pageable);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Query("from SysDictData where dictType = ?1 and status = '0' order by dictSort desc")
    List<SysDictData> findByType(String dictType);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Query("select dictLabel from SysDictData where dictType = ?1 and dictValue = ?2")
    SysDictData findByTypeAndValue(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Query("from SysDictData where dictCode = ?1")
    SysDictData findDictDataById(Long dictCode);

    /**
     * 查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据
     */
    @Query("select count(d) from SysDictData d where d.dictType = ?1")
    int countDictDataByType(String dictType);

    /**
     * 同步修改字典类型
     *
     * @param oldDictType 旧字典类型
     * @param newDictType 新旧字典类型
     * @return 结果
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update sys_dict_data set dict_type = ?2 where dict_type = ?1")
    void updateDictDataType(String oldDictType, String newDictType);
}
