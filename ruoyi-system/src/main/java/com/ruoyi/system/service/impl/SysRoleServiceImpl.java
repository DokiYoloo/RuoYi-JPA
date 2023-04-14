package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.convertor.SysRoleConvertor;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.dto.SysRoleDTO;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.repository.SysRoleDeptRepository;
import com.ruoyi.system.repository.SysRoleMenuRepository;
import com.ruoyi.system.repository.SysRoleRepository;
import com.ruoyi.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements ISysRoleService {
    private final SysRoleRepository sysRoleRepo;
    private final SysRoleMenuRepository roleMenuRepo;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleDeptRepository roleDeptRepo;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public Page<SysRole> selectRolePaged(SysRoleDTO role) {
        Pageable pageable = role.buildPageable();
        return sysRoleRepo.findPaged(role, pageable);
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = sysRoleRepo.findAllByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = sysRoleRepo.findAllByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        SysRoleDTO sysRoleDTO = new SysRoleDTO();
        sysRoleDTO.setPageNum(1);
        sysRoleDTO.setPageSize(99999);
        return SpringUtils.getAopProxy(this).selectRolePaged(sysRoleDTO).getContent();
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return sysRoleRepo.findIdsByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return sysRoleRepo.findById(roleId).orElse(null);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRoleDTO role) {
        long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = sysRoleRepo.findByRoleName(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId() != roleId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRoleDTO role) {
        long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = sysRoleRepo.findByRoleKey(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId() != roleId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRoleDTO role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    @Override
    public void checkRoleDataScope(Long roleId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysRoleDTO role = new SysRoleDTO();
            role.setRoleId(roleId);
            role.setPageNum(1);
            role.setPageSize(99999);
            Page<SysRole> roles = SpringUtils.getAopProxy(this).selectRolePaged(role);
            if (StringUtils.isEmpty(roles.getContent())) {
                throw new ServiceException("没有权限访问角色数据！");
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public void insertRole(SysRoleDTO role) {
        SysRole sysRole = SysRoleConvertor.toPO(role);
        sysRole.setCreateBy(getUsername());
        sysRole = sysRoleRepo.save(sysRole);
        role.setRoleId(sysRole.getRoleId());
        insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateRole(SysRoleDTO role) {
        SysRole sysRole = SysRoleConvertor.toPO(role);
        sysRole.setUpdateBy(getUsername());
        // 修改角色信息
        sysRoleRepo.save(sysRole);
        // 删除角色与菜单关联
        roleMenuRepo.deleteRoleMenuByRoleId(role.getRoleId());
        insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public void updateRoleStatus(SysRoleDTO role) {
        SysRole sysRole = SysRoleConvertor.toPO(role);
        sysRole.setUpdateBy(getUsername());
        sysRoleRepo.save(sysRole);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     */
    @Override
    @Transactional
    public void authDataScope(SysRoleDTO role) {
        // 修改角色信息
        SysRole sysRole = SysRoleConvertor.toPO(role);
        sysRoleRepo.save(sysRole);
        // 删除角色与部门关联
        roleDeptRepo.deleteRoleDeptByRoleId(role.getRoleId());
        // 新增角色和部门信息（数据权限）
        insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public void insertRoleMenu(SysRoleDTO role) {
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        roleMenuRepo.saveBatch(list);
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public void insertRoleDept(SysRoleDTO role) {
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        roleDeptRepo.saveBatch(list);
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     */
    @Override
    @Transactional
    public void deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        roleMenuRepo.deleteRoleMenuByRoleId(roleId);
        // 删除角色与部门关联
        roleDeptRepo.deleteRoleDeptByRoleId(roleId);
        sysRoleRepo.deleteById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     */
    @Override
    @Transactional
    public void deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRoleDTO(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuRepo.deleteRoleMenu(roleIds);
        // 删除角色与部门关联
        roleDeptRepo.deleteRoleDept(roleIds);
        sysRoleRepo.deleteAllById(Arrays.asList(roleIds));
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }
}
