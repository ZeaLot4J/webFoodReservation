package com.zl.utils;

import redis.clients.jedis.Jedis;

public class Test {

	public static void main(String[] args) {
		
//		Jedis jedis=new Jedis("192.168.242.128");
//		//用户登录
//		for( int i=1;i<10;i++){
//			UserRedis.activeUsers(jedis, i	);
//		}
//		UserRedis.activeUsers(jedis, 100);
//		UserRedis.activeUsers(jedis, 101);
//		
//		//查看指定用户是否登录
//		System.out.println(  UserRedis.isUserOnline(jedis, "onLineUserPerDay:20161104", 3) );
//		System.out.println(  UserRedis.isUserOnline(jedis, "onLineUserPerDay:20161104", 1000) );
//		
//		//统计某天的在线人数
//		System.out.println(   UserRedis.getOnLineUserCount(jedis, "onLineUserPerDay:20161031") );
//		//统计连续三天上线的人数
//		System.out.println(  UserRedis.getSeriesOnLineInNDays(jedis, 3));
//		//统计三天内上过线的人数
//		System.out.println(  UserRedis.getOnLineInNDays(jedis, 2));
//		
//		//保留最近一天的记录，其它都删除
//		UserRedis.keepNDaysRecord(jedis, 1);
	}

}
