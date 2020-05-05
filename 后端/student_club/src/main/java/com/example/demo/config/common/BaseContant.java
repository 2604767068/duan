package com.example.demo.config.common;

import java.math.BigDecimal;

/***
 * 基础常量类
 * @author Administrator
 *
 */
public class BaseContant {

	/***
	 * 配置表最大个人拥有社团Key
	 */
	public static final String MAX_CLUB_NUM="club_num";
	/***
	 * 头部参数，社团ID
	 */
	public static final String HEAD_PARAMS_CLUB_ID="clubId";
	/***
	 * 每次错误增加一次
	 */
	public static final BigDecimal UPDATE_ADD_NUM=new BigDecimal(1);
	/**
	 * 验证码过期时间(错误到达五次后的重试时间),十分钟
	 * */
	public static  long CODE_EXIST_TIME=600;
	
	/**
	 * 登录令牌过期时间 10小时
	 * */
	public static  long LOGIN_TOKEN_EXPRISE_TIME=36000000;
	/**
	 * 社团状态正常
	 */
	public static final String CLUB_STATUS_NORMAL="0";
	/**
	 * 社团状态申请中
	 */
	public static final String CLUB_STATUS_APPLYING="1";
	/**
	 * 社团状态被拒绝
	 */
	public static final String CLUB_STATUS_REFUSE="2";
	/**
	 * 社团成员状态正常
	 */
	public static final String CLUB_USER_STATUS_NORMAL="0";
	/**
	 * 社团成员状态申请中
	 */
	public static final String CLUB_USER_STATUS_APPLYING="1";
	/**
	 * 社团成员状态被拒绝
	 */
	public static final String CLUB_USER_STATUS_REFUSE="2";
	/**
	 * 是社团管理员
	 */
	public static final String CLUB_MANAGE="1";
	/**
	 * 普通社团成员
	 */
	public static final String CLUB_USER="0";
	/***
	 * 初始数量
	 */
	public static final String INIT_NUM="0";
	/***
	 * 状态停用
	 * 	 */
	public static final String STATUS_STOP="1";
	/***
	 * 状态启用
	 */
	public static final String STATUS_START="0";
	
}
