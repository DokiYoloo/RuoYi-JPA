package com.ruoyi.web.controller.monitor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.system.domain.convertor.SysLogininforConvertor;
import com.ruoyi.system.domain.dto.SysLogininforDTO;
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
import com.ruoyi.framework.web.service.SysPasswordService;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.service.ISysLogininforService;

/**
 * 系统访问记录
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController {
    private final ISysLogininforService logininforService;
    private final SysPasswordService passwordService;

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public TableDataInfo<SysLogininforDTO> list(SysLogininforDTO logininfor) {
        Page<SysLogininfor> paged = logininforService.selectLogininforList(logininfor);
        return getDataTable(paged.map(SysLogininforConvertor::toDTO));
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogininforDTO logininfor) {
        List<SysLogininforDTO> list = logininforService.selectLogininforList(logininfor)
                .map(SysLogininforConvertor::toDTO).getContent();
        ExcelUtil<SysLogininforDTO> util = new ExcelUtil<>(SysLogininforDTO.class);
        util.exportExcel(response, list, "登录日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public ResponseEntity<Void> remove(@PathVariable Long[] infoIds) {
        return ResponseEntity.deduce(() -> logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public ResponseEntity<Void> clean() {
        return ResponseEntity.deduce(logininforService::cleanLogininfor);
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:unlock')")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public ResponseEntity<Void> unlock(@PathVariable("userName") String userName) {
        return ResponseEntity.deduce(() -> passwordService.clearLoginRecordCache(userName));
    }
}
