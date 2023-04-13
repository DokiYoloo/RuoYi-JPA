package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.ruoyi.system.domain.SysRoleMenu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * 角色菜单Repository
 *
 * @author DokiYolo
 */
@Repository
public interface SysRoleMenuRepository extends FenixJpaRepository<SysRoleMenu, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM sys_role_menu WHERE role_id in ?1")
    void deleteRoleMenu(Long[] roleIds);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM sys_role_menu WHERE role_id = ?1")
    void deleteRoleMenuByRoleId(Long roleId);
}
