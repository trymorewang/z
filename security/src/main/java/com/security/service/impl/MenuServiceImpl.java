package com.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.dao.MenuDao;
import com.security.entity.MenuVO;
import com.security.service.MenuService;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  权限业务实现
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:13
 * @Version 1.0
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuVO> implements MenuService {

}