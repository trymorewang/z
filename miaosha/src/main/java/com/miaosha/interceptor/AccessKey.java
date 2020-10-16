package com.miaosha.interceptor;

import com.miaosha.redis.BasePrefix;

public class AccessKey extends BasePrefix {

	private AccessKey( int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static AccessKey withExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "interceptor");
	}
	
}
