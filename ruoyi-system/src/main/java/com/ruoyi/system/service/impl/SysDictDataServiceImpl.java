package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.convertor.SysDictDataConvertor;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.dto.SysDictDataDTO;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.repository.SysDictDataRepository;
import com.ruoyi.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {
    @Autowired
    private SysDictDataRepository dictDataRepo;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public Page<SysDictData> selectDictDataPaged(SysDictDataDTO dictData) {
        Pageable pageable = dictData.buildPageable();
        return dictDataRepo.findDictDataPaged(dictData, pageable);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return Optional.ofNullable(dictDataRepo.findByTypeAndValue(dictType, dictValue))
                .map(SysDictData::getDictLabel)
                .orElse(null);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return dictDataRepo.findDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            dictDataRepo.deleteById(dictCode);
            List<SysDictData> dictDatas = dictDataRepo.findByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     */
    @Override
    public void insertDictData(SysDictDataDTO data) {
        SysDictData dict = SysDictDataConvertor.toPO(data);
        dict.setCreateBy(SecurityUtils.getUsername());
        dictDataRepo.save(dict);
        List<SysDictData> dictDatas = dictDataRepo.findByType(data.getDictType());
        DictUtils.setDictCache(data.getDictType(), dictDatas);
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     */
    @Override
    public void updateDictData(SysDictDataDTO data) {
        SysDictData dict = SysDictDataConvertor.toPO(data);
        dict.setUpdateBy(SecurityUtils.getUsername());
        dictDataRepo.save(dict);
        List<SysDictData> dictDatas = dictDataRepo.findByType(data.getDictType());
        DictUtils.setDictCache(data.getDictType(), dictDatas);
    }
}
