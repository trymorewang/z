package com.security.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 *  角色实体
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/07/31 14:53
 * @Version 1.0
 */
@Data
@TableName("sys_role")
public class RoleVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long roleId;
	private String roleName;
}
