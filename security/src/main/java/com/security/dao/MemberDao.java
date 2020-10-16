package com.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.entity.MemberVO;
import com.security.entity.MenuVO;
import com.security.entity.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  系统用户dao
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
@Component
public interface MemberDao extends BaseMapper<MemberVO> {


    List<RoleVO> selectSysRoleByMemberId(Long memberId);

    List<MenuVO> selectSysMenuByMemberId(Long memberId);


    Integer updateMember(Long memberId);
}
