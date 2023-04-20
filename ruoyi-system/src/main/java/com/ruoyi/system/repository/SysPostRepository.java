package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.dto.SysPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 岗位Repository
 *
 * @author DokiYolo
 */
@Repository
public interface SysPostRepository extends FenixJpaRepository<SysPost, Long> {
    Page<SysPost> findPaged(SysPostDTO post, Pageable pageable);

    @Query("from SysPost p left join SysUserPost up on up.postId = p.postId left join SysUser u on u.userId = up.userId where u.userId = ?1")
    List<Long> findByUserId(Long userId);

    SysPost findByPostCode(String postCode);

    SysPost findByPostName(String postName);

    @Query("from SysPost p left join SysUserPost up on up.postId = p.postId left join SysUser u on u.userId = up.userId where u.userName = ?1")
    List<SysPost> findByUserName(String userName);
}
