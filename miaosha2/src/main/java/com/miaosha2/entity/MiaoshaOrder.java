package com.miaosha2.entity;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiaoshaOrder {
    private Long id;
    private Long userId;
    private String orderId;
    private Long goodsId;
    private Integer status;
    private Date createTime;
}
