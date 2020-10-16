package com.rocketmq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Wilson
 * @since 2020/2/27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    private Integer id;
    private String name;
}
