package com.miaosha.controller;

import com.miaosha.common.enums.resultbean.ResultGeekQ;
import com.miaosha.common.vo.LoginVo;
import com.miaosha.service.MiaoShaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoShaUserService userService;


    /*@GetMapping("/to_login")
    public String tologin(LoginVo loginVo, Model model) {
        logger.info(loginVo.toString());

        String count = RedisLua.getVistorCount(COUNTLOGIN).toString();
        logger.info("访问网站的次数为:{}", count);
        model.addAttribute("count", count);
        return "login";
    }*/

    @PostMapping("/loginin")
    public ResultGeekQ<String> dologin(HttpServletResponse response, @Valid LoginVo loginVo) {
        ResultGeekQ<String> result = ResultGeekQ.build();
        logger.info("登录用户为：{}", loginVo.toString());
        userService.login(response, loginVo);
        return result;
    }


    @PostMapping("/create_token")
    public String createToken(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.toString());
        String token = userService.createToken(response, loginVo);
        return token;
    }
}
