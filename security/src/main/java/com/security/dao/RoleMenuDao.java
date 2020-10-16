package com.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.entity.RoleMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * <p>
 *  角色权限关系DAO
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
@Component
public interface RoleMenuDao extends BaseMapper<RoleMenuVO> {


}
