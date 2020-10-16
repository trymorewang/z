package com.schedule.spring;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *  定时任务实体类
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/08/27 17:16
 * @Version 1.0
 */
@Data
@Builder
public class SysJobVO {

    /**
     * 任务ID
     */
    private Integer jobId;
    /**
     * bean名称
     */
    private String beanName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 方法参数
     */
    private String methodParams;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 状态（1正常 0暂停）
     */
    private Integer jobStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


}
