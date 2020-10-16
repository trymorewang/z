package com.miaosha.mapper;

import com.miaosha.common.entity.MiaoshaUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author 邱润泽
 */
@Component
public interface MiaoShaUserMapper {

    public MiaoshaUser getByNickname(@Param("nickname") String nickname) ;

    public MiaoshaUser getById(@Param("id") long id) ;

    public void update(MiaoshaUser toBeUpdate);

    public void insertMiaoShaUser(MiaoshaUser miaoshaUser);

    public int getCountByUserName(@Param("userName") String userName, @Param("userType") int userType);

}
