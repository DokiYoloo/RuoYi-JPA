package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.ruoyi.system.domain.SysRoleDept;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * 系统角色部门Repository
 *
 * @author DokiYolo
 */
@Repository
public interface SysRoleDeptRepository extends FenixJpaRepository<SysRoleDept, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM sys_role_dept WHERE role_id in ?1")
    void deleteRoleDept(Long[] roleIds);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM sys_role_dept WHERE role_id = ?1")
    void deleteRoleDeptByRoleId(Long roleId);
}
