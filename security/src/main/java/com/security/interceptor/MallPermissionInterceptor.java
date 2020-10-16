package com.security.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.annotation.MallPermission;
import com.security.config.IgnoreUrlsConfig;
import com.security.constant.PlatformConstants;
import com.security.dao.*;
import com.security.entity.*;
import com.security.service.MemberRoleService;
import com.security.service.RedisService;
import com.security.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 权限拦截器
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/07/31 14:53
 * @Version 1.0
 */
@Slf4j
public class MallPermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberRoleService memberRoleService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //不需要保护的资源路径允许访问
        for (String url : ignoreUrlsConfig.getUrls()) {
            if (url.indexOf(request.getRequestURI()) > -1) {
                return true;
            }
        }

        // 验证权限
        if (this.hasPermission(request, response, handler)) {
            return true;
        }
        // 如果没有权限 则抛403异常
        ResultUtil.responseJson(response, ResultUtil.resultCode(403, "无权限"));
        return false;
    }

    /**
     * 是否有权限
     *
     * @param handler
     * @return
     */
    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String id = request.getHeader(PlatformConstants.TOKEN_ID_HEARD);
        if (StringUtils.isEmpty(id)) {
            ResultUtil.responseJson(response, ResultUtil.resultCode(200, "未登录"));
            return false;
        }

        Object cacheMember = redisService.get(id);
        if (StringUtils.isEmpty(cacheMember)) {
            ResultUtil.responseJson(response, ResultUtil.resultCode(200, "token已过期或无效"));
            return false;
        }

        MemberVO memberVO = (MemberVO) cacheMember;

        //管理员直接放行
        if (memberVO.getIsAdmin()) {
            return true;
        }

        //List<RoleVO> roleVOS = getRoleList(memberVO.getMemberId());
        List<MenuVO> menuVOS = memberVO.getMenus();

        //无资源配置
        if (isListNull(menuVOS)) {
            return false;
        }

        if(hasAuth(menuVOS, request.getRequestURI())) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * 检查list是否为空
     * </p>
     *
     * @param list
     * @return
     */
    private boolean isListNull(List list) {
        return CollectionUtils.isEmpty(list);
    }


    /**
     * <p>
     * 判断是否有权限
     * </p>
     *
     * @param menuVOS
     * @param uri
     * @return
     */
    private boolean hasAuth(List<MenuVO> menuVOS, String uri) {

        for (MenuVO menuVO : menuVOS) {
            if (uri.equals(menuVO.getUrl())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO
    }
}
