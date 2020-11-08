package com.rocketmq.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketmq.entity.WzRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RepoDao extends BaseMapper<WzRepo> {
    List<WzRepo> findAll();

    int updateRepoStatusByOrderId(String orderId, int repo_done);
}
