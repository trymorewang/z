package com.miaosha2.dao;

import com.miaosha2.entity.MiaoshaGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface GoodsMapper {

    List<MiaoshaGoods> listGoodsVo();

    MiaoshaGoods getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    int reduceStock(@Param("goodsId") long goodsId, @Param("version") int version);

}
