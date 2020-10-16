package com.miaosha.mapper;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.common.entity.MiaoshaGoods;
import com.miaosha.common.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 邱润泽
 */
@Component
public interface GoodsMapper {

    public List<GoodsVoOrder> listGoodsVo();

    public GoodsVoOrder getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    public int reduceStock(MiaoshaGoods g);

}
