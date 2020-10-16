package com.security.controller;

import com.security.annotation.MallPermission;
import com.security.dao.MemberDao;
import com.security.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/07/31 11:35
 * @Version 1.0
 */
@RestController
public class TestController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDao memberDao;


    @GetMapping("role/user")
    public String test1() {
        return "有权限访问";
    }

    @GetMapping("role/admin")
    public String test2() {
        return "有权限访问";
    }

    @GetMapping("login")
    public String login(Long id) {
        return memberService.login(id);
    }

    @GetMapping("member/update")
    public void test3(Long memberId) {
        memberDao.updateMember(memberId);
    }

}
