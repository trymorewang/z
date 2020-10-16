package com.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.entity.MemberRoleVO;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  用户与角色关系DAO
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
@Component
public interface MemberRoleDao extends BaseMapper<MemberRoleVO> {

}
