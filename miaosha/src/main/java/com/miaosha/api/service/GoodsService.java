package com.miaosha.api.service;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.api.utils.ResultGeekQOrder;

import java.util.List;

/**
 * @author 邱润泽
 */
public interface GoodsService {

    /**
     * 查询获取全部信息
     * @return
     */
    public ResultGeekQOrder<List<GoodsVoOrder>> listGoodsVo();

    /**
     * 根据商品id查询货物信息
     * @param goodsId
     * @return
     */
    public ResultGeekQOrder<GoodsVoOrder> getGoodsVoByGoodsId(long goodsId);

    /**
     * 减库存
     * @return
     */
    public  boolean reduceStock(GoodsVoOrder goods);
}
