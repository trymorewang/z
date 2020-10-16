package com.security.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  用户与角色关系实体
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/07/31 14:53
 * @Version 1.0
 */
@Data
@TableName("sys_member_role")
public class MemberRoleVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	@TableId
	private Long id;
	private Long memberId;
	private Long roleId;
}
