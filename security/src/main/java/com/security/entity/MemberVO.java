package com.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  系统用户实体
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/07/31 14:53
 * @Version 1.0
 */
@Data
@TableName("sys_member")
public class MemberVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long memberId;
	private String username;
	private String password;
	private String status;

	/**
	 * 资源（exlst=false意思是非数据库字段）
	 */
	@TableField(exist = false)
	private List<MenuVO> menus;

	/**
	 * 是否是管理员
	 */
	@TableField(exist = false)
	private Boolean isAdmin;
}