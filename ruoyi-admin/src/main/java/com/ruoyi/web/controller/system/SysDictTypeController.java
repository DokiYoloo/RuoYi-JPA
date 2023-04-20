package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.common.core.domain.convertor.SysDictTypeConvertor;
import com.ruoyi.common.core.domain.entity.dto.SysDictTypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.SysDictTypeService;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {
    private final SysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo<SysDictTypeDTO> list(SysDictTypeDTO dictType) {
        Page<SysDictType> paged = dictTypeService.selectDictTypePaged(dictType);
        return getDataTable(paged.map(SysDictTypeConvertor::toDTO));
    }

    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictTypeDTO dictType) {
        Page<SysDictTypeDTO> paged = dictTypeService.selectDictTypePaged(dictType)
                .map(SysDictTypeConvertor::toDTO);
        ExcelUtil<SysDictTypeDTO> util = new ExcelUtil<>(SysDictTypeDTO.class);
        util.exportExcel(response, paged.getContent(), "字典类型");
    }

    /**
     * 查询字典类型详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public ResponseEntity<SysDictTypeDTO> getInfo(@PathVariable Long dictId) {
        SysDictType dictType = dictTypeService.selectDictTypeById(dictId);
        return ResponseEntity.successful(SysDictTypeConvertor.toDTO(dictType));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Void> add(@Validated @RequestBody SysDictTypeDTO dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return ResponseEntity.failed("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return ResponseEntity.deduce(() -> dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> edit(@Validated @RequestBody SysDictTypeDTO dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return ResponseEntity.failed("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return ResponseEntity.deduce(() -> dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public ResponseEntity<Void> remove(@PathVariable Long[] dictIds) {
        return ResponseEntity.deduce(() -> dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 刷新字典缓存
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public ResponseEntity<Void> refreshCache() {
        return ResponseEntity.deduce(dictTypeService::resetDictCache);
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public ResponseEntity<List<SysDictTypeDTO>> optionselect() {
        List<SysDictTypeDTO> dictTypes = dictTypeService.selectDictTypeAll()
                .stream().map(SysDictTypeConvertor::toDTO).collect(Collectors.toList());
        return ResponseEntity.successful(dictTypes);
    }
}
