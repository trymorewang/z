package com.miaosha.common.entity;

import lombok.*;

import java.util.Date;

@Data
public class MiaoshaUser {

	private Long id;
	private String nickname;
	private String password;
	private String salt;
	private String head;
	private Date registerDate;
	private Date lastLoginDate;
	private Integer loginCount;

}
