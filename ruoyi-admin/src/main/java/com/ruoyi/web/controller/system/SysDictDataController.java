package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.common.core.domain.convertor.SysDictDataConvertor;
import com.ruoyi.common.core.domain.entity.dto.SysDictDataDTO;
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
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.SysDictDataService;
import com.ruoyi.system.service.SysDictTypeService;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
    private final SysDictDataService dictDataService;
    private final SysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo<SysDictDataDTO> list(SysDictDataDTO dictData) {
        Page<SysDictData> paged = dictDataService.selectDictDataPaged(dictData);
        return getDataTable(paged.map(SysDictDataConvertor::toDTO));
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictDataDTO dictData) {
        Page<SysDictDataDTO> paged = dictDataService.selectDictDataPaged(dictData)
                .map(SysDictDataConvertor::toDTO);
        ExcelUtil<SysDictDataDTO> util = new ExcelUtil<>(SysDictDataDTO.class);
        util.exportExcel(response, paged.getContent(), "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public ResponseEntity<SysDictDataDTO> getInfo(@PathVariable Long dictCode) {
        SysDictData sysDictData = dictDataService.selectDictDataById(dictCode);
        return ResponseEntity.successful(SysDictDataConvertor.toDTO(sysDictData));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public ResponseEntity<List<SysDictDataDTO>> dictType(@PathVariable String dictType) {
        List<SysDictDataDTO> data = dictTypeService.selectDictDataByType(dictType)
                .stream().map(SysDictDataConvertor::toDTO).collect(Collectors.toList());
        return ResponseEntity.successful(data);
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Void> add(@Validated @RequestBody SysDictDataDTO dict) {
        return ResponseEntity.deduce(() -> dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> edit(@Validated @RequestBody SysDictDataDTO dict) {
        return ResponseEntity.deduce(() -> dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public ResponseEntity<Void> remove(@PathVariable Long[] dictCodes) {
        return ResponseEntity.deduce(() -> dictDataService.deleteDictDataByIds(dictCodes));
    }
}
