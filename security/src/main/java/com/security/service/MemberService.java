package com.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.security.entity.MemberVO;
import com.security.entity.MenuVO;
import com.security.entity.RoleVO;

import java.util.List;

/**
 * <p>
 *  系统用户业务接口
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
public interface MemberService extends IService<MemberVO>  {

    /**
     * 根据用户名查询实体
     * @Author Sans
     * @CreateTime 2019/9/14 16:30
     * @Param  Membername 用户名
     * @Return SysMemberEntity 用户实体
     */

    /**
     * 根据用户ID查询角色集合
     * @Author Sans
     * @CreateTime 2019/9/18 18:01
     * @Param  MemberId 用户ID
     * @Return List<SysRoleEntity> 角色名集合
     */
    List<RoleVO> selectSysRoleByMemberId(Long MemberId);
    /**
     * 根据用户ID查询权限集合
     * @Author Sans
     * @CreateTime 2019/9/18 18:01
     * @Param  MemberId 用户ID
     * @Return List<SysMenuEntity> 角色名集合
     */
    List<MenuVO> selectSysMenuByMemberId(Long MemberId);

    String login(Long id);

}