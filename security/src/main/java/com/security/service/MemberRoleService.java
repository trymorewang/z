package com.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.security.entity.MemberRoleVO;

/**
 * <p>
 *  用户与角色业务接口
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
public interface MemberRoleService extends IService<MemberRoleVO> {

    MemberRoleVO getByMemberId(Long memberId);

}