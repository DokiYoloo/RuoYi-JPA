package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.system.domain.convertor.SysPostConvertor;
import com.ruoyi.system.domain.dto.SysPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.ISysPostService;

/**
 * 岗位信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
    private final ISysPostService postService;

    /**
     * 获取岗位列表
     */
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public TableDataInfo<SysPostDTO> list(SysPostDTO post) {
        Page<SysPost> paged = postService.selectPostPaged(post);
        return getDataTable(paged.map(SysPostConvertor::toDTO));
    }

    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPostDTO post) {
        Page<SysPostDTO> paged = postService.selectPostPaged(post)
                .map(SysPostConvertor::toDTO);
        ExcelUtil<SysPostDTO> util = new ExcelUtil<>(SysPostDTO.class);
        util.exportExcel(response, paged.getContent(), "岗位数据");
    }

    /**
     * 根据岗位编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{postId}")
    public ResponseEntity<SysPostDTO> getInfo(@PathVariable Long postId) {
        SysPost sysPost = postService.selectPostById(postId);
        return ResponseEntity.successful(SysPostConvertor.toDTO(sysPost));
    }

    /**
     * 新增岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Void> add(@Validated @RequestBody SysPostDTO post) {
        if (!postService.checkPostNameUnique(post)) {
            return ResponseEntity.failed("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            return ResponseEntity.failed("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return ResponseEntity.deduce(() -> postService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Void> edit(@Validated @RequestBody SysPostDTO post) {
        if (!postService.checkPostNameUnique(post)) {
            return ResponseEntity.failed("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            return ResponseEntity.failed("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return ResponseEntity.deduce(() -> postService.updatePost(post));
    }

    /**
     * 删除岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{postIds}")
    public ResponseEntity<Void> remove(@PathVariable Long[] postIds) {
        return ResponseEntity.deduce(() -> postService.deletePostByIds(postIds));
    }

    /**
     * 获取岗位选择框列表
     */
    @GetMapping("/optionselect")
    public ResponseEntity<List<SysPostDTO>> optionselect() {
        List<SysPostDTO> posts = postService.selectPostAll()
                .stream().map(SysPostConvertor::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.successful(posts);
    }
}
