package com.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.entity.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  角色DAO
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
@Component
public interface RoleDao extends BaseMapper<RoleVO> {

}