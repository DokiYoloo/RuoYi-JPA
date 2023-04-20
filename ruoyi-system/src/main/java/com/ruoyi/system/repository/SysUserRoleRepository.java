package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.ruoyi.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户与角色关联表 数据层
 *
 * @author DokiYolo
 */
@Repository
public interface SysUserRoleRepository extends FenixJpaRepository<SysUserRole, Long> {
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from sys_user_role where user_id = ?1")
    void deleteUserRoleByUserId(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from sys_user_role where user_id in ?1")
    void deleteUserRole(Long[] ids);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     */
    @Query("select count(ur) from SysUserRole ur where ur.roleId = ?1")
    int countUserRoleByRoleId(Long roleId);

    /**
     * 删除用户和角色关联信息
     *
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from sys_user_role where user_id = ?2 and role_id = ?1")
    void deleteUserRoleInfo(Long roleId, Long userId);

    /**
     * 批量取消授权用户角色
     *  @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from sys_user_role where user_id in ?2 and role_id = ?1")
    void deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
