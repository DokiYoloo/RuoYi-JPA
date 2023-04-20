package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.convertor.SysUserConvertor;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.dto.SysUserDTO;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUserPost;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.repository.SysPostRepository;
import com.ruoyi.system.repository.SysRoleRepository;
import com.ruoyi.system.repository.SysUserPostRepository;
import com.ruoyi.system.repository.SysUserRepository;
import com.ruoyi.system.repository.SysUserRoleRepository;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserRepository userRepo;
    private final SysRoleRepository roleRepo;
    private final SysPostRepository sysPostRepo;
    private final SysUserRoleRepository userRoleRepo;
    private final SysUserPostRepository userPostRepo;
    private final SysConfigService configService;
    protected final Validator validator;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public Page<SysUser> selectUserPaged(SysUserDTO user) {
        Pageable pageable = user.buildPageable();
        return userRepo.selectUserList(user, pageable);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public Page<SysUser> selectAllocatedPaged(SysUserDTO user) {
        Pageable pageable = user.buildPageable();
        return userRepo.selectAllocatedList(user, pageable);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public Page<SysUser> selectUnallocatedPaged(SysUserDTO user) {
        Pageable pageable = user.buildPageable();
        return userRepo.selectUnallocatedList(user, pageable);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userRepo.findByUserId(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleRepo.findAllByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = sysPostRepo.findByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUserDTO user) {
        long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userRepo.findFirstByUserName(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId() != userId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUserDTO user) {
        long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userRepo.findFirstByPhonenumber(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId() != userId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUserDTO user) {
        long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userRepo.findFirstByEmail(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId() != userId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUserDTO user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUserDTO user = new SysUserDTO();
            user.setUserId(userId);
            Page<SysUser> users = SpringUtils.getAopProxy(this).selectUserPaged(user);
            if (StringUtils.isEmpty(users.getContent())) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void insertUser(SysUserDTO user) {
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        SysUser sysUser = SysUserConvertor.toPO(user);
        // 新增用户信息
        userRepo.save(sysUser);
        SysUserDTO sysUserDTO = SysUserConvertor.toDTO(sysUser);
        // 新增用户岗位关联
        insertUserPost(sysUserDTO);
        // 新增用户与角色管理
        insertUserRole(sysUserDTO);
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public void registerUser(SysUserDTO user) {
        SysUser sysUser = SysUserConvertor.toPO(user);
        userRepo.save(sysUser);
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     */
    @Override
    @Transactional
    public void updateUser(SysUserDTO user) {
        user.setUpdateBy(SecurityUtils.getUsername());
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleRepo.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostRepo.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        SysUser sysUser = SysUserConvertor.toPO(user);
        userRepo.save(sysUser);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleRepo.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     */
    @Override
    public void updateUserStatus(SysUserDTO user) {
        user.setUpdateBy(SecurityUtils.getUsername());
        SysUser sysUser = SysUserConvertor.toPO(user);
        userRepo.save(sysUser);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public void updateUserProfile(SysUserDTO user) {
        SysUser sysUser = SysUserConvertor.toPO(user);
        userRepo.save(sysUser);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     */
    @Override
    public void updateUserAvatar(String userName, String avatar) {
        userRepo.updateUserAvatar(userName, avatar);
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     */
    @Override
    public void resetPwd(SysUserDTO user) {
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        SysUser sysUser = SysUserConvertor.toPO(user);
        userRepo.save(sysUser);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     */
    @Override
    public void resetUserPwd(String userName, String password) {
        userRepo.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUserDTO user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUserDTO user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<>(posts.length);
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostRepo.saveBatch(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleRepo.saveBatch(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     */
    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleRepo.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostRepo.deleteUserPostByUserId(userId);
        userRepo.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     */
    @Override
    @Transactional
    public void deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUserDTO(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleRepo.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostRepo.deleteUserPost(userIds);
        userRepo.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUserDTO> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUserDTO user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userRepo.findByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    SysUser sysUser = SysUserConvertor.toPO(user);
                    userRepo.save(sysUser);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(SysUserConvertor.toDTO(u));
                    checkUserDataScope(u.getUserId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    SysUser sysUser = SysUserConvertor.toPO(user);
                    userRepo.save(sysUser);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(user.getUserName()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
