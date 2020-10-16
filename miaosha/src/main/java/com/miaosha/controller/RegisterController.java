package com.miaosha.controller;

import com.miaosha.common.enums.resultbean.ResultGeekQ;
import com.miaosha.service.MiaoShaUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.miaosha.common.enums.Constanst.COUNTLOGIN;
import static com.miaosha.common.enums.enums.ResultStatus.RESIGETER_FAIL;


@Slf4j
@Controller
@RequestMapping("/")
public class RegisterController {


    @Autowired
    private MiaoShaUserService miaoShaUserService;


    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("/do_login")
    public String loginIndex(Model model) {
        model.addAttribute("count", 1000);
        return "login";
    }

    /**
     * 注册页面
     *
     * @return
     */
    @RequestMapping("/regist")
    public String register() {
        return "register2";
    }


    /**
     * 校验程序
     *
     * @return
     */
    @RequestMapping("/checkUsername")
    @ResponseBody
    public ResultGeekQ<Boolean> checkUsername(String username) {

        ResultGeekQ<Boolean> result = ResultGeekQ.build();
        boolean nickNameCount = miaoShaUserService.getNickNameCount(username);
        result.setData(nickNameCount);
        return result;
    }

    @RequestMapping("/register")
    @ResponseBody
    public ResultGeekQ<Boolean> register(@RequestParam("username") String userName,
                                         @RequestParam("password") String passWord,
                                         HttpServletResponse response,
                                         HttpServletRequest request) {

        ResultGeekQ<Boolean> result = ResultGeekQ.build();
        boolean registerInfo = miaoShaUserService.register(userName, passWord, response, request);
        if (!registerInfo) {
            result.withError(RESIGETER_FAIL.getCode(), RESIGETER_FAIL.getMessage());
            result.setData(false);
        }
        result.setData(true);
        return result;
    }
}
