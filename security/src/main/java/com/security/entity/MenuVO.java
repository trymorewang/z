package com.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  权限实体
 * </p>
 *
 * @Author Zhi.Wang
 * @Date 2020/07/31 14:53
 * @Version 1.0
 */
@Data
@TableName("sys_menu")
public class MenuVO implements Serializable {
	private static final long serialVersionUID = 1L;


	@TableId
	private Long menuId;
	private String name;
	private String url;
}
