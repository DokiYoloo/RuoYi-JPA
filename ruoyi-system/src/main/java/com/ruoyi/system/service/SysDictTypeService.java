package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.entity.dto.SysDictTypeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
public interface SysDictTypeService {
    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    Page<SysDictType> selectDictTypePaged(SysDictTypeDTO dictType);

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    List<SysDictType> selectDictTypeAll();

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    SysDictType selectDictTypeById(Long dictId);

    /**
     * 批量删除字典信息
     *
     * @param dictIds 需要删除的字典ID
     */
    void deleteDictTypeByIds(Long[] dictIds);

    /**
     * 加载字典缓存数据
     */
    void loadingDictCache();

    /**
     * 清空字典缓存数据
     */
    void clearDictCache();

    /**
     * 重置字典缓存数据
     */
    void resetDictCache();

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     */
    void insertDictType(SysDictTypeDTO dictType);

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     */
    void updateDictType(SysDictTypeDTO dictType);

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    boolean checkDictTypeUnique(SysDictTypeDTO dictType);
}
