package com.security.constant;

/**
 * <p>
 *     常量类
 * </p>
 *
 * @author Zhi.Wang
 * @version 1.0
 * @date 2020-09-16 18:14
 */
public interface PlatformConstants {


    /**##################AES加密常量##################*/


    /**##############名片服务常量##############*/
    String MALL_KEY = "MALL_";
    String MALL_TOKEN_KEY = "MALL_TOKEN_";
    String MALL_MEMBER_LOGIN_KEY = "MALL_MEMBER_LOGIN_KEY_";
    /**图片上传路径*/
    String ZHI_PATH = "card/";
    /**图片最大值*/
    Integer IMG_MAX_SIZE = 1 * 1024 * 1024;


    /**################token信息传递heard################*/
    String TOKEN_ID_HEARD = "Authorization";

    /**################requestId传递heard################*/


    /**
     * 分页数
     */
    int PAGESIZE = 10;

    /**
     * 登录计算时间
     */
    int LOGIN_COMPUTE_TIME = 60;



}
