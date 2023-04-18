package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.convertor.SysDictTypeConvertor;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.entity.dto.SysDictDataDTO;
import com.ruoyi.common.core.domain.entity.dto.SysDictTypeDTO;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.repository.SysDictDataRepository;
import com.ruoyi.system.repository.SysDictTypeRepository;
import com.ruoyi.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl implements ISysDictTypeService {
    private final SysDictTypeRepository dictTypeRepo;
    private final SysDictDataRepository dictDataRepo;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public Page<SysDictType> selectDictTypePaged(SysDictTypeDTO dictType) {
        Pageable pageable = dictType.buildPageable();
        return dictTypeRepo.findDictTypePaged(dictType, pageable);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return dictTypeRepo.findAll();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = dictDataRepo.findByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return dictTypeRepo.findDictTypeById(dictId);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataRepo.countDictDataByType(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            dictTypeRepo.deleteById(dictId);
            DictUtils.removeDictCache(dictType.getDictType());
        }
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        SysDictDataDTO dictData = new SysDictDataDTO();
        dictData.setStatus("0");
        Pageable pageable = dictData.buildPageable();
        Map<String, List<SysDictData>> dictDataMap = dictDataRepo.findDictDataPaged(dictData, pageable).getContent().stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     */
    @Override
    public void insertDictType(SysDictTypeDTO dict) {
        SysDictType dictType = SysDictTypeConvertor.toPO(dict);
        dictType.setCreateBy(SecurityUtils.getUsername());
        dictTypeRepo.save(dictType);
        DictUtils.setDictCache(dict.getDictType(), null);
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     */
    @Override
    @Transactional
    public void updateDictType(SysDictTypeDTO dict) {
        SysDictType dictType = SysDictTypeConvertor.toPO(dict);
        dictType.setUpdateBy(SecurityUtils.getUsername());
        SysDictType oldDict = dictTypeRepo.findDictTypeById(dict.getDictId());
        dictDataRepo.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        dictTypeRepo.save(dictType);
        List<SysDictData> dictDatas = dictDataRepo.findByType(dict.getDictType());
        DictUtils.setDictCache(dict.getDictType(), dictDatas);
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(SysDictTypeDTO dict) {
        long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        SysDictType dictType = dictTypeRepo.findFirstByDictType(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && dictType.getDictId() != dictId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
