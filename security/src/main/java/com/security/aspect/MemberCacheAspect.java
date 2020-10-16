package com.security.aspect;


import com.security.constant.PlatformConstants;
import com.security.dao.MemberDao;
import com.security.entity.MemberVO;
import com.security.entity.MenuVO;
import com.security.entity.RoleVO;
import com.security.service.MemberService;
import com.security.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  用户基础信息或者权限信息更新时进行缓存刷新切面类
 *
 *  还有一个思路是用观察者模式，通过ApplicationEvent 类和 ApplicationListener 接口来处理事件
 *  onApplicationEvent 接收到事件执行刷新逻辑，不过需要在每次修改的地方都调用发布事件，也许没有直接执行删除或者修改缓存来的更直接
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/07/31 11:35
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class MemberCacheAspect {

    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberService memberService;

    @Pointcut("execution(public * com.security.dao.MemberDao.updateMember(..))")
    public void flush() {
    }

    @Before("flush()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        log.info("开始执行缓存刷新。。。。");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String id = request.getHeader(PlatformConstants.TOKEN_ID_HEARD);
        MemberVO memberVO = memberService.getById(id);
        List<RoleVO> roleVOS = memberDao.selectSysRoleByMemberId(memberVO.getMemberId());
        roleVOS.forEach(e -> {
            if ("ADMIN".equals(e.getRoleName())) {
                memberVO.setIsAdmin(true);
            }
            memberVO.setIsAdmin(false);
        });
        if (!StringUtils.isEmpty(memberVO)) {

            List<MenuVO> menuVOList = memberDao.selectSysMenuByMemberId(memberVO.getMemberId());
            memberVO.setMenus(menuVOList);
            //保存用户token对应用户信息
            redisService.set(String.valueOf(id), memberVO);
            log.info(memberVO.getUsername() + " - 缓存更新完成。。。");
        }
    }

    @AfterReturning(returning = "ret", pointcut = "flush()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        //logger.info("RESPONSE : " + ret);

    }

}
