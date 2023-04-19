package com.ruoyi.system.repository;

import com.blinkfox.fenix.jpa.FenixJpaRepository;
import com.blinkfox.fenix.jpa.QueryFenix;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.dto.SysMenuDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author DokiYolo
 */
@Repository
public interface SysMenuRepository extends FenixJpaRepository<SysMenu, Long> {
    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @QueryFenix
    List<SysMenu> findMenuList(SysMenuDTO menu);

    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    @Query("select distinct m.perms from SysMenu m")
    List<String> findMenuPerms();

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @QueryFenix
    List<SysMenu> findAllMenuByUserId(SysMenuDTO menu);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Query("select distinct m.perms from SysMenu m left join SysRoleMenu rm on m.menuId = rm.menuId where m.status = '0' and rm.roleId = ?1")
    List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Query("select distinct m.perms from SysMenu m left join SysRoleMenu rm on m.menuId = rm.menuId " +
            "left join SysUserRole ur on ur.roleId = rm.roleId left join SysRole r on r.roleId = ur.roleId " +
            "where m.status = '0' and r.status = '0' and ur.userId = ?1")
    List<String> findMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    @Query("select distinct m from SysMenu m where m.menuType in ('M', 'C') and m.status = '0' order by m.parentId, m.orderNum")
    List<SysMenu> findMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Query("select distinct m from SysMenu m left join SysRoleMenu rm on m.menuId = rm.menuId " +
            "left join SysUserRole ur on ur.roleId = rm.roleId left join SysRole r on r.roleId = ur.roleId " +
            "where m.status = '0' and r.status = '0' and m.menuType in ('M', 'C') and ur.userId = ?1 order by m.parentId, m.orderNum")
    List<SysMenu> findMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    @QueryFenix
    List<Long> findAllMenuByRoleId(Long roleId, boolean menuCheckStrictly);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Query("from SysMenu where menuId = ?1")
    SysMenu findMenuById(Long menuId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Query("select count(m) from SysMenu m where m.parentId = ?1")
    int hasChildByMenuId(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    SysMenu findFirstByMenuNameAndParentId(String menuName, Long parentId);
}
