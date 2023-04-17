package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.common.core.domain.entity.dto.SysDeptDTO;
import com.ruoyi.common.core.domain.convertor.SysDeptConvertor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
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
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    private final ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public ResponseEntity<List<SysDeptDTO>> list(SysDeptDTO dept) {
        Page<SysDept> depts = deptService.selectDeptPaged(dept);
        return ResponseEntity.successful(depts.map(SysDeptConvertor::toDTO).getContent());
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public ResponseEntity<List<SysDeptDTO>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDeptDTO> depts = deptService.selectDeptPaged(new SysDeptDTO()).map(SysDeptConvertor::toDTO).getContent();
        depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return ResponseEntity.successful(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public ResponseEntity<SysDeptDTO> getInfo(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        SysDept sysDept = deptService.selectDeptById(deptId);
        return ResponseEntity.successful(SysDeptConvertor.toDTO(sysDept));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Void> add(@Validated @RequestBody SysDeptDTO dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return ResponseEntity.failed("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return ResponseEntity.deduce(() -> deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> edit(@Validated @RequestBody SysDeptDTO dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (!deptService.checkDeptNameUnique(dept)) {
            return ResponseEntity.failed("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return ResponseEntity.failed("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return ResponseEntity.failed("该部门包含未停用的子部门！");
        }
        return ResponseEntity.deduce(() -> deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public ResponseEntity<Void> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return ResponseEntity.warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return ResponseEntity.warn("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return ResponseEntity.deduce(() -> deptService.deleteDeptById(deptId));
    }
}
