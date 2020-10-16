package com.rocketmq.entity;

import com.rocketmq.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 点赞记录表
 * </p>
 *
 * @author 
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@ApiModel("点赞记录表")
public class PraiseRecord extends BaseVO {
    private Long id;
    private Long uid;
    private Long liveId;
    private LocalDateTime createTime;
}
