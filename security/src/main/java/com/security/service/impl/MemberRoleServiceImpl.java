package com.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.dao.MemberRoleDao;
import com.security.entity.MemberRoleVO;
import com.security.entity.MemberVO;
import com.security.service.MemberRoleService;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  用户与角色业务实现
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
@Service
public class MemberRoleServiceImpl extends ServiceImpl<MemberRoleDao, MemberRoleVO> implements MemberRoleService {

    @Override
    public MemberRoleVO getByMemberId(Long memberId) {
        QueryWrapper<MemberRoleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberRoleVO::getMemberId, memberId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}