package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.dto.SysUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户表 数据层
 *
 * @author DokiYolo
 */
@Repository
public interface SysUserRepository extends FenixJpaRepository<SysUser, Long> {
    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    Page<SysUser> selectUserList(SysUserDTO sysUser, Pageable pageable);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    Page<SysUser> selectAllocatedList(SysUserDTO user, Pageable pageable);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    Page<SysUser> selectUnallocatedList(SysUserDTO user, Pageable pageable);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Query("from SysUser where userName = ?1 and delFlag = '0'")
    SysUser findByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Query("from SysUser where userId = ?1")
    SysUser findByUserId(Long userId);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update sys_user set avatar = ?2 where user_name = ?1")
    void updateUserAvatar(String userName, String avatar);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update sys_user set password = ?2 where user_name = ?1")
    void resetUserPwd(String userName, String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update sys_user set del_flag = '2' where user_id = ?1")
    void deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update sys_user set del_flag = '2' where user_id in ?1")
    void deleteUserByIds(Long[] userIds);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    SysUser findFirstByUserName(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    SysUser findFirstByPhonenumber(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    SysUser findFirstByEmail(String email);
}
