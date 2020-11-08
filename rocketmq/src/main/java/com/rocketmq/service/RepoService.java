package com.rocketmq.service;

import com.rocketmq.dao.OrderDao;
import com.rocketmq.dao.RepoDao;
import com.rocketmq.entity.WzOrder;
import com.rocketmq.entity.WzRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoService {

    @Autowired
    RepoDao repoDao;

    public final int REPO_DONE = 1;

    /**
     *  检查库存记录是否存在（实际场景需要检查库存是否更新，这里测试省略）
     **/
    public boolean checkRepoSuccess(String orderId){
        List<WzRepo> allOrders = repoDao.findAll();
        return  allOrders.stream()
                .anyMatch(order -> order.getOrderId().equals(orderId) && order.getRepoStatus() == REPO_DONE);
    }

    /**
     *  更新订单是为支付完成
     **/
    public void updateRepoStatusByOrderId(String orderId){
        repoDao.updateRepoStatusByOrderId(orderId, REPO_DONE);
    }
}
