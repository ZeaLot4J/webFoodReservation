package com.zl.utils;
/**
 * 此类专门用于定义系统中所使用的字符串常量
 * @author ZeaLot
 *
 */
public class ZLConstants {
	//redis中存放Resfood的hash的名字前缀
	public final static String ALLFOOD = "allfood";
	
	public final static String REDIS_URL = "192.168.242.128"; 
	public final static int REDIS_PORT = 6379; 
	public final static String VERIFYCODE = "verifyCode"; 
	
	public final static String CART_NAME = "cart";
	public final static String LOGIN_USER = "user";
	
	public final static int KEEP_NDAYS_FOR_ONLINE_USER = 1;//删除1天前的数据
}
