package com.rocketmq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rocketmq.producer.OrderTransactionProducer;
import com.rocketmq.service.OrderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTransactionProducer orderTransactionProducer;

    /**
     * 生成订单接口
     *
     * @param num
     * @param goodId
     * @param userId
     * @return
     */
    @GetMapping("save")
    public String makeOrder(
            @RequestParam("num") int num,
            @RequestParam("goodId") int goodId,
            @RequestParam("userId") int userId) {

        orderService.save(UUID.randomUUID().toString(), num, goodId, userId);
        return "success";
    }

    @GetMapping("pay")
    public String pay(@RequestParam("orderId") String orderId)
            throws UnsupportedEncodingException, MQClientException, JsonProcessingException {
        orderTransactionProducer.sendOrderPaySucessEvent(orderId);
        return "success";
    }
}
