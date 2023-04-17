package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.blinkfox.fenix.jpa.QueryFenix;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.dto.SysDeptDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author ruoyi
 */
public interface SysDeptRepository extends FenixJpaRepository<SysDept, Long> {
    /**
     * 查询部门管理数据
     * TODO
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    Page<SysDept> selectDeptList(SysDeptDTO dept, Pageable pageable);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    @QueryFenix
    List<Long> findDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Query("from SysDept WHERE deptId = ?1")
    SysDept findDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    @Query("from SysDept d where find_in_set(:deptId, d.ancestors) > 0")
    List<SysDept> findChildrenDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Query("select count(d) from SysDept d where d.status = 0 and d.delFlag = '0' and find_in_set(:deptId, d.ancestors) > 0")
    int findNormalChildrenDeptById(Long deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Query("select 1 from SysDept where parentId = ?1 and delFlag = '0'")
    int hasChildByDeptId(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Query("select 1 from SysDept where deptId = ?1 and delFlag = '0'")
    int checkDeptExistUser(Long deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    @Query(nativeQuery = true, value = "select * from SysDept where dept_name = ?1 AND parent_id = ?2 AND delFlag = '0' limit 1")
    SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * 修改所在部门正常状态
     *
     * @param deptIds 部门ID组
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update sys_dept set status = '0' where dept_id in ?1")
    void updateDeptStatusNormal(Long[] deptIds);
}
