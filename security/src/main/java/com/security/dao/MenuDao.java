package com.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.entity.MenuVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  权限dao
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
@Component
public interface MenuDao extends BaseMapper<MenuVO> {

    List<MenuVO> getList(@Param("roleId") Long roleId);
}