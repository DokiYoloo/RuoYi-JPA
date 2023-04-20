package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.convertor.SysPostConvertor;
import com.ruoyi.system.domain.dto.SysPostDTO;
import com.ruoyi.system.repository.SysPostRepository;
import com.ruoyi.system.repository.SysUserPostRepository;
import com.ruoyi.system.service.SysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysPostServiceImpl implements SysPostService {
    private final SysUserPostRepository userPostRepo;
    private final SysPostRepository sysPostRepo;

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public Page<SysPost> selectPostPaged(SysPostDTO post) {
        Pageable pageable = post.buildPageable();
        return sysPostRepo.findPaged(post, pageable);
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostAll() {
        return sysPostRepo.findAll();
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(Long postId) {
        return sysPostRepo.findById(postId).orElse(null);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return sysPostRepo.findByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostNameUnique(SysPostDTO post) {
        long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = sysPostRepo.findByPostName(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId() != postId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostCodeUnique(SysPostDTO post) {
        long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = sysPostRepo.findByPostCode(post.getPostCode());
        if (StringUtils.isNotNull(info) && info.getPostId() != postId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId) {
        return userPostRepo.countUserPostById(postId);
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     */
    @Override
    public void deletePostById(Long postId) {
        sysPostRepo.deleteById(postId);
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     */
    @Override
    public void deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        sysPostRepo.deleteAllById(Arrays.asList(postIds));
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     */
    @Override
    public void insertPost(SysPostDTO post) {
        SysPost sysPost = SysPostConvertor.toPO(post);
        post.setCreateBy(SecurityUtils.getUsername());
        sysPostRepo.save(sysPost);
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     */
    @Override
    public void updatePost(SysPostDTO post) {
        SysPost sysPost = SysPostConvertor.toPO(post);
        post.setUpdateBy(SecurityUtils.getUsername());
        sysPostRepo.save(sysPost);
    }
}
