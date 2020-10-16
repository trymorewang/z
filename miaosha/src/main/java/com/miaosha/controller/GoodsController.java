package com.miaosha.controller;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.common.entity.MiaoshaUser;
import com.miaosha.common.enums.resultbean.ResultGeekQ;
import com.miaosha.common.vo.GoodsDetailVo;
import com.miaosha.interceptor.RequireLogin;
import com.miaosha.redis.GoodsKey;
import com.miaosha.redis.RedisService;
import com.miaosha.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {


    @Autowired
    private GoodsService goodsService;


    /**
     * QPS:1267 load:15 mysql
     * 5000 * 10
     * QPS:2884, load:5
     */
    @RequireLogin(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        model.addAttribute("user", user);

        List<GoodsVoOrder> goodsList = goodsService.listGoodsVo();

        model.addAttribute("goodsList", goodsList);
        return render(request, response, model, "goods_list", GoodsKey.getGoodsList, "");
    }


    /**
     * 数据库很少使用long的，id 正常使一般使用　snowflake 分布式自增id
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public ResultGeekQ<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                             @PathVariable("goodsId") long goodsId) {
        ResultGeekQ<GoodsDetailVo> result = ResultGeekQ.build();

        GoodsVoOrder goodsVoOrder = goodsService.getGoodsVoByGoodsId(goodsId);

        long startAt = goodsVoOrder.getStartDate().getTime();
        long endAt = goodsVoOrder.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goodsVoOrder);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        result.setData(vo);
        return result;
    }
}
