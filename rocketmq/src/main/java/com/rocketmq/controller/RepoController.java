package com.rocketmq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rocketmq.producer.OrderTransactionProducer;
import com.rocketmq.producer.RepoTransactionProducer;
import com.rocketmq.service.OrderService;
import com.rocketmq.service.RepoService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RequestMapping("/repo")
@RestController
public class RepoController {

    @Autowired
    RepoService repoService;

    @Autowired
    RepoTransactionProducer repoTransactionProducer;


    @GetMapping("pay")
    public String pay(@RequestParam("orderId") String orderId)
            throws UnsupportedEncodingException, MQClientException, JsonProcessingException {
        repoTransactionProducer.sendRepoSucessEvent(orderId);
        return "success";
    }
}
