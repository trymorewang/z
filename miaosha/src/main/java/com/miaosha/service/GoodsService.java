package com.miaosha.service;

import com.miaosha.api.entity.GoodsVoOrder;
import com.miaosha.common.entity.MiaoshaGoods;
import com.miaosha.common.vo.GoodsVo;
import com.miaosha.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
	
	@Autowired
	GoodsMapper goodsMapper;


	public List<GoodsVoOrder> listGoodsVo(){
		return goodsMapper.listGoodsVo();
	}

	public GoodsVoOrder getGoodsVoByGoodsId(long goodsId) {
		return goodsMapper.getGoodsVoByGoodsId(goodsId);
	}

	public boolean reduceStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		int ret = goodsMapper.reduceStock(g);
		return ret > 0;
	}
	
	
	
}
