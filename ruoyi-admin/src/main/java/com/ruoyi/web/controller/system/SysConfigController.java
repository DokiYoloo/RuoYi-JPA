package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.system.domain.convertor.SysConfigConvertor;
import com.ruoyi.system.domain.dto.SysConfigDTO;
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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    private final ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo<SysConfigDTO> list(SysConfigDTO config) {
        Page<SysConfig> page = configService.selectConfigPaged(config);
        return getDataTable(page.map(SysConfigConvertor::toDTO));
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfigDTO config) {
        List<SysConfigDTO> list = configService.selectConfigPaged(config)
                .stream().map(SysConfigConvertor::toDTO)
                .collect(Collectors.toList());
        ExcelUtil<SysConfigDTO> util = new ExcelUtil<>(SysConfigDTO.class);
        util.exportExcel(response, list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public ResponseEntity<SysConfigDTO> getInfo(@PathVariable Long configId) {
        SysConfig sysConfig = configService.selectConfigById(configId);
        if (sysConfig == null) {
            return ResponseEntity.failed("配置不存在");
        }
        return ResponseEntity.successful(SysConfigConvertor.toDTO(sysConfig));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public ResponseEntity<String> getConfigKey(@PathVariable String configKey) {
        return ResponseEntity.successful(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Void> add(@Validated @RequestBody SysConfigDTO config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return ResponseEntity.failed("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return ResponseEntity.deduce(() -> configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> edit(@Validated @RequestBody SysConfigDTO config) {
        if (config.getConfigId() == null) {
            return ResponseEntity.failed("配置不存在");
        }
        if (!configService.checkConfigKeyUnique(config)) {
            return ResponseEntity.failed("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return ResponseEntity.deduce(() -> configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public ResponseEntity<Void> remove(@PathVariable Long[] configIds) {
        return ResponseEntity.deduce(() -> configService.deleteConfigByIds(configIds));
    }

    /**
     * 刷新参数缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public ResponseEntity<Void> refreshCache() {
        return ResponseEntity.deduce(configService::resetConfigCache);
    }
}
