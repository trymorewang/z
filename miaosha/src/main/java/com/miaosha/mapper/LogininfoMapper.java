package com.miaosha.mapper;

import com.miaosha.common.entity.Logininfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface LogininfoMapper {

	int deleteByPrimaryKey(Long id);

	int insert(Logininfo record);

	Logininfo selectByPrimaryKey(Long id);

	List<Logininfo> selectAll();

	int updateByPrimaryKey(Logininfo record);

	int getCountByNickname(@Param("nickname") String nickname,
                           @Param("userType") int userType);

	Logininfo getLoginInfoByNickname(@Param("nickname") String nickname,
                                     @Param("userType") int userType);

	Logininfo login(@Param("name") String name,
                    @Param("password") String password, @Param("userType") int userType);

	List<Map<String, Object>> autoComplate(@Param("word") String word, @Param("userType") int userType);
}