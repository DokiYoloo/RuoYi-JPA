package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.dto.SysRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统角色Repository
 *
 * @author DokiYolo
 * @since 2023-04-14
 */
@Repository
public interface SysRoleRepository extends FenixJpaRepository<SysRole, Long> {
    /**
     * TODO fenix xml
     */
    Page<SysRole> findPaged(SysRoleDTO role, Pageable pageable);

    @Query("from SysRole where roleName = ?1")
    SysRole findByRoleName(String roleName);

    @Query("from SysRole where roleKey = ?1")
    SysRole findByRoleKey(String roleKey);

    @Query("from SysRole r left join SysUserRole ur on r.roleId = ur.roleId left join SysUser u on u.userId = ur.userId left join SysDept d on u.deptId = d.deptId where r.delFlag = '0' and ur.userId = ?1")
    List<SysRole> findAllByUserId(Long userId);

    @Query("select r.roleId from SysRole r left join SysUserRole ur on r.roleId = ur.roleId left join SysUser u on u.userId = ur.userId where u.userId = ?1")
    List<Long> findIdsByUserId(Long userId);

    @Query("from SysRole r left join SysUserRole ur on r.roleId = ur.roleId left join SysUser u on u.userId = ur.userId where r.delFlag = '0' and u.userName = ?1")
    List<SysRole> findAllByUserName(String userName);
}
