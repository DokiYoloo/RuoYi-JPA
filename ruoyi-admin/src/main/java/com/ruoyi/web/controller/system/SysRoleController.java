package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.common.core.domain.convertor.SysRoleConvertor;
import com.ruoyi.common.core.domain.convertor.SysUserConvertor;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.dto.SysRoleDTO;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    private final ISysRoleService roleService;
    private final TokenService tokenService;
    private final SysPermissionService permissionService;
    private final ISysUserService userService;
    private final ISysDeptService deptService;

    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo<SysRoleDTO> list(SysRoleDTO role) {
        Page<SysRole> paged = roleService.selectRolePaged(role);
        return getDataTable(paged.map(SysRoleConvertor::toDTO));
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRoleDTO role) {
        Page<SysRoleDTO> paged = roleService.selectRolePaged(role)
                .map(SysRoleConvertor::toDTO);
        ExcelUtil<SysRoleDTO> util = new ExcelUtil<>(SysRoleDTO.class);
        util.exportExcel(response, paged.getContent(), "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public ResponseEntity<Void> getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return ResponseEntity.deduce(() -> roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Void> add(@Validated @RequestBody SysRoleDTO role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return ResponseEntity.failed("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return ResponseEntity.failed("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return ResponseEntity.deduce(() -> roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> edit(@Validated @RequestBody SysRoleDTO role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role)) {
            return ResponseEntity.failed("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return ResponseEntity.failed("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        roleService.updateRole(role);
        // 更新缓存用户权限
        LoginUser loginUser = getLoginUser();
        if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
            loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
            SysUser sysUser = userService.selectUserByUserName(loginUser.getUser().getUserName());
            loginUser.setUser(SysUserConvertor.toDTO(sysUser));
            tokenService.setLoginUser(loginUser);
        }
        return ResponseEntity.success();
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public ResponseEntity<Void> dataScope(@RequestBody SysRoleDTO role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return ResponseEntity.deduce(() -> roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public ResponseEntity<Void> changeStatus(@RequestBody SysRoleDTO role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return ResponseEntity.deduce(() -> roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public ResponseEntity<Void> remove(@PathVariable Long[] roleIds) {
        return ResponseEntity.deduce(() -> roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public ResponseEntity<List<SysRoleDTO>> optionselect() {
        List<SysRoleDTO> roleList = roleService.selectRoleAll().stream()
                .map(SysRoleConvertor::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.successful(roleList);
    }

    /**
     * 查询已分配用户角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public ResponseEntity<Void> cancelAuthUser(@RequestBody SysUserRole userRole) {
        return ResponseEntity.deduce(() -> roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public ResponseEntity<Void> cancelAuthUserAll(Long roleId, Long[] userIds) {
        return ResponseEntity.deduce(() -> roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public ResponseEntity<Void> selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return ResponseEntity.deduce(() -> roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 获取对应角色部门树列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/deptTree/{roleId}")
    public AjaxResult deptTree(@PathVariable("roleId") Long roleId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
        return ajax;
    }
}
