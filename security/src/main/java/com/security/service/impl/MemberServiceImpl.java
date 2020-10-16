package com.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.constant.PlatformConstants;
import com.security.dao.MemberDao;
import com.security.dao.MemberRoleDao;
import com.security.dao.RoleDao;
import com.security.entity.MemberVO;
import com.security.entity.MenuVO;
import com.security.entity.RoleVO;
import com.security.service.MemberService;
import com.security.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * <p>
 *  系统用户业务实现
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberVO> implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private RedisService redisService;

    /**
     * 根据用户名查询实体
     *
     * @Author Sans
     * @CreateTime 2019/9/14 16:30
     * @Param Membername 用户名
     * @Return SysMemberEntity 用户实体
     */


    /**
     * 通过用户ID查询角色集合
     *
     * @Author Sans
     * @CreateTime 2019/9/18 18:01
     * @Param MemberId 用户ID
     * @Return List<SysRoleEntity> 角色名集合
     */
    @Override
    public List<RoleVO> selectSysRoleByMemberId(Long MemberId) {
        return this.baseMapper.selectSysRoleByMemberId(MemberId);
    }

    /**
     * 根据用户ID查询权限集合
     *
     * @Author Sans
     * @CreateTime 2019/9/18 18:01
     * @Param MemberId 用户ID
     * @Return List<SysMenuEntity> 角色名集合
     */
    @Override
    public List<MenuVO> selectSysMenuByMemberId(Long MemberId) {
        return this.baseMapper.selectSysMenuByMemberId(MemberId);
    }

    @Override
    public String login(Long id) {
        //判断是否已经登录
        Object cacheMember = redisService.get(String.valueOf(id));
        if (!ObjectUtils.isEmpty(cacheMember)) {
            return "已经登录";
        }

        MemberVO memberVO = memberDao.selectById(id);

        List<RoleVO> roleVOS = memberDao.selectSysRoleByMemberId(memberVO.getMemberId());
        roleVOS.forEach( e -> {
            if ("ADMIN".equals(e.getRoleName())) {
                memberVO.setIsAdmin(true);
            }
            memberVO.setIsAdmin(false);
        });
        if (!StringUtils.isEmpty(memberVO)) {

            List<MenuVO> menuVOList = memberDao.selectSysMenuByMemberId(memberVO.getMemberId());
            memberVO.setMenus(menuVOList);
            //保存用户token对应用户信息
            redisService.set(String.valueOf(id), memberVO);
            return "success";
        }
        return "用户不存在";
    }
}