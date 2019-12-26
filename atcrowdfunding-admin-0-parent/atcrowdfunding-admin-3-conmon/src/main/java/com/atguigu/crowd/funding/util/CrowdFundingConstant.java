package com.atguigu.crowd.funding.util;

import java.util.HashMap;
import java.util.Map;


public class CrowdFundingConstant {
	//本类作用:把页面所有消息字段,定义成常量,方便管理
	public static final String ATTR_NAME_MESSAGE = "MESSAGE";
	public static final String ATTR_NAME_LOGIN_ADMIN = "LOGIN-ADMIN";
	public static final String MESSAGE_LOGIN_FAILED = "登陆用户或密码不正确,请核对后再操作";
	public static final String MESSAGE_CODE_INVALID = "明文不是有效字符串,请核对后再操作";
	public static final String ATTR_NAME_PAGE_INFO = "PAGE-INFO";
	public static final String MESSAGE_ACCESS_DENIED = "请登录后再操作！";
	public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "登录账号被占用，请重新设定！";

public static final Map<String, String> EXCEPTION_MESSAGE_MAP = new HashMap<>();
	
	static {
		EXCEPTION_MESSAGE_MAP.put("java.lang.ArithmeticException", "系统在进行数学运算时发生错误");
		EXCEPTION_MESSAGE_MAP.put("java.lang.RuntimeException", "系统在运行时发生错误");
		EXCEPTION_MESSAGE_MAP.put("com.atguigu.crowd.funding.exception.LoginException", "登录过程中运行错误");
	}
}
