package com.miaosha.controller;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.common.entity.MiaoshaOrder;
import com.miaosha.common.entity.MiaoshaUser;
import com.miaosha.common.enums.resultbean.ResultGeekQ;
import com.miaosha.interceptor.RequireLogin;
import com.miaosha.redis.GoodsKey;
import com.miaosha.redis.RedisService;
import com.miaosha.redis.redismanager.RedisLimitRateWithLUA;
import com.miaosha.service.GoodsService;
import com.miaosha.service.MiaoShaUserService;
import com.miaosha.service.MiaoshaService;
import com.miaosha.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.miaosha.common.enums.enums.ResultStatus.*;

@Slf4j
@RestController
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {


    @Autowired
    MiaoShaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;


    private HashMap<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * QPS:1306
     * 5000 * 10
     * get　post get 幂等　从服务端获取数据　不会产生影响　　post 对服务端产生变化
     */
    @RequireLogin(seconds = 5, maxCount = 5, needLogin = true)
    @GetMapping("/{path}/do_miaosha")
    public ResultGeekQ<Integer> miaosha(Model model, MiaoshaUser user, @PathVariable("path") String path,
                                        @RequestParam("goodsId") long goodsId) {
        ResultGeekQ<Integer> result = ResultGeekQ.build();

        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
            return result;
        }
		//使用RateLimiter 限流
		/*RateLimiter rateLimiter = RateLimiter.create(10);
		//判断能否在1秒内得到令牌，如果不能则立即返回false，不会阻塞程序
		if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
			System.out.println("短期无法获取令牌，真不幸，排队也瞎排");
			return ResultGeekQ.error(CodeMsg.MIAOSHA_FAIL);

		}*/

        //分布式限流
        RedisLimitRateWithLUA.accquire();

        //是否已经秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(user.getNickname()), goodsId);
        if (order != null) {
            result.withError(EXCEPTION.getCode(), REPEATE_MIAOSHA.getMessage());
            return result;
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            result.withError(EXCEPTION.getCode(), MIAO_SHA_OVER.getMessage());
            return result;
        }
        //预见库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            result.withError(EXCEPTION.getCode(), MIAO_SHA_OVER.getMessage());
            return result;
        }

        //发送mq
        /*MiaoshaMessage mm = new MiaoshaMessage();
        mm.setGoodsId(goodsId);
        mm.setUser(user);
        mqSender.sendMiaoshaMessage(mm);*/
        return result;
    }


    /**
     * <P>
     *     获取秒杀结果 orderId：成功 -1：秒杀失败 0： 排队中
     * </P>
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequireLogin(seconds = 5, maxCount = 5, needLogin = true)
    @GetMapping("/result")
    public ResultGeekQ<Long> miaoshaResult(Model model, MiaoshaUser user,
                                           @RequestParam("goodsId") long goodsId) {
        ResultGeekQ<Long> result = ResultGeekQ.build();
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        model.addAttribute("user", user);
        Long miaoshaResult = miaoshaService.getMiaoshaResult(Long.valueOf(user.getNickname()), goodsId);
        result.setData(miaoshaResult);
        return result;
    }

    /**
     * <P>
     *     获取秒杀地址
     * </P>
     *
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    @RequireLogin(seconds = 5, maxCount = 5, needLogin = true)
    @GetMapping("/path")
    public ResultGeekQ<String> getMiaoshaPath(MiaoshaUser user,
                                              @RequestParam("goodsId") long goodsId,
                                              @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode
    ) {
        ResultGeekQ<String> result = ResultGeekQ.build();
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        /*boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
            return result;
        }*/
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        result.setData(path);
        return result;
    }

    @GetMapping("/verifyCodeRegister")
    public ResultGeekQ<String> getMiaoshaVerifyCod(HttpServletResponse response
    ) {
        ResultGeekQ<String> result = ResultGeekQ.build();
        try {
            BufferedImage image = miaoshaService.createVerifyCodeRegister();
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return result;
        } catch (Exception e) {
            log.error("生成验证码错误-----注册:{}", e);
            result.withError(MIAOSHA_FAIL.getCode(), MIAOSHA_FAIL.getMessage());
            return result;
        }
    }


    @GetMapping("/verifyCode")
    public ResultGeekQ<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoshaUser user,
                                                   @RequestParam("goodsId") long goodsId) {
        ResultGeekQ<String> result = ResultGeekQ.build();
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return result;
        } catch (Exception e) {
            log.error("生成验证码错误-----goodsId:{}", goodsId, e);
            result.withError(MIAOSHA_FAIL.getCode(), MIAOSHA_FAIL.getMessage());
            return result;
        }
    }

    /**
     * 系统初始化
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVoOrder> goodsList = goodsService.listGoodsVo();

        Optional<List<GoodsVoOrder>> optionalGoodsVos = Optional.ofNullable(goodsList);

        optionalGoodsVos.orElseThrow(() -> new RuntimeException("系统初始化秒杀数据失败"));

        optionalGoodsVos.ifPresent(goodsVos -> {
            goodsVos.forEach(goods -> {
                redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
                localOverMap.put(goods.getId(), false);
            });
        });


        /*if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }*/
        log.info("系统初始化秒杀商品缓存成功。。。。");
    }
}
