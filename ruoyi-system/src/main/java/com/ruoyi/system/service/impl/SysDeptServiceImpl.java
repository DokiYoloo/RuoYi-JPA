package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.dto.SysDeptDTO;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.core.domain.convertor.SysDeptConvertor;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.repository.SysRoleRepository;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
    @Autowired
    private SysDeptRepository deptRepo;

    @Autowired
    private SysRoleRepository roleRepo;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public Page<SysDept> selectDeptPaged(SysDeptDTO dept) {
        Pageable pageable = dept.buildPageable();
        return deptRepo.selectDeptList(dept, pageable);
    }

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDeptDTO dept) {
        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptPaged(dept).getContent();
        return buildDeptTreeSelect(depts.stream().map(SysDeptConvertor::toDTO).collect(Collectors.toList()));
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDeptDTO> buildDeptTree(List<SysDeptDTO> depts) {
        List<SysDeptDTO> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(SysDeptDTO::getDeptId).collect(Collectors.toList());
        for (SysDeptDTO dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDeptDTO> depts) {
        List<SysDeptDTO> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleRepo.findById(roleId).orElseThrow(() -> new ServiceException("角色不存在"));
        return deptRepo.findDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId) {
        return deptRepo.findDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptRepo.findNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptRepo.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptRepo.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDeptDTO dept) {
        long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptRepo.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId() != deptId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysDeptDTO dept = new SysDeptDTO();
            dept.setDeptId(deptId);
            Page<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptPaged(dept);
            if (depts.getContent().isEmpty()) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     */
    @Override
    public void insertDept(SysDeptDTO dept) {
        SysDept info = deptRepo.findDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        SysDept sysDept = SysDeptConvertor.toPO(dept);
        sysDept.setCreateBy(SecurityUtils.getUsername());
        sysDept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        deptRepo.save(sysDept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     */
    @Override
    public void updateDept(SysDeptDTO dept) {
        SysDept newParentDept = deptRepo.findDeptById(dept.getParentId());
        SysDept oldDept = deptRepo.findDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        SysDept sysDept = SysDeptConvertor.toPO(dept);
        sysDept.setUpdateBy(SecurityUtils.getUsername());
        dept = SysDeptConvertor.toDTO(deptRepo.save(sysDept));
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDeptDTO dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptRepo.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptRepo.findChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (!children.isEmpty()) {
            deptRepo.saveAll(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     */
    @Override
    public void deleteDeptById(Long deptId) {
        deptRepo.deleteById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDeptDTO> list, SysDeptDTO t) {
        // 得到子节点列表
        List<SysDeptDTO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDeptDTO tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDeptDTO> getChildList(List<SysDeptDTO> list, SysDeptDTO t) {
        List<SysDeptDTO> tlist = new ArrayList<>();
        for (SysDeptDTO n : list) {
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDeptDTO> list, SysDeptDTO t) {
        return getChildList(list, t).size() > 0;
    }
}
