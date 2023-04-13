package com.ruoyi.web.controller.monitor;

import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.system.domain.convertor.SysOperLogConvertor;
import com.ruoyi.system.domain.dto.SysOperLogDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.service.ISysOperLogService;

/**
 * 操作日志记录
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {
    private final ISysOperLogService operLogService;

    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo<SysOperLogDTO> list(SysOperLogDTO operLog) {
        Page<SysOperLog> paged = operLogService.selectOperLogPaged(operLog);
        return getDataTable(paged.map(SysOperLogConvertor::toDTO));
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLogDTO operLog) {
        Page<SysOperLogDTO> list = operLogService.selectOperLogPaged(operLog)
                .map(SysOperLogConvertor::toDTO);
        ExcelUtil<SysOperLogDTO> util = new ExcelUtil<>(SysOperLogDTO.class);
        util.exportExcel(response, list.getContent(), "操作日志");
    }

    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public ResponseEntity<Void> remove(@PathVariable Long[] operIds) {
        return ResponseEntity.deduce(() -> operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public ResponseEntity<Void> clean() {
        return ResponseEntity.deduce(operLogService::cleanOperLog);
    }
}
