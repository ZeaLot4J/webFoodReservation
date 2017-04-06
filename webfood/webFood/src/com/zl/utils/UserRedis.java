package com.zl.utils;

import java.text.DateFormat; 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

/**
 * 统计用户登录的操作封装
 * @author ZeaLot
 *
 */
public class UserRedis {

	/** 每天登录过的用户 -> key格式 -> onLineUserPerDay:yyyyMMdd */
	public final static String USERPERDAY = "userPerDay:";
	/** 连续N天都登录的用户 */
	public final static String NDAYSBOTHONLINEUSERS = "n_days_both_online_users";
	/** N天中曾经登录过的用户 */
	public final static String NDAYSONLINEUSERS = "n_days_online_users";
	


	/**
	 * 保留最近n天的登录记录，其它的从redis中删除.
	 */
	public static void keepNDaysRecord(Jedis jedis, int n) {
		//1. 求出要保留哪几天的
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String[] nDays = new String[n];
		for (int i = 0; i < n; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, -i);
			String key = USERPERDAY + df.format(c.getTime());
			nDays[i] = key;
		}
		//2. 求出redis中现存有哪几天的登录数据
		Set<String> set=jedis.keys("userPerDay:*");
		for( String d: nDays){
			if( set.contains(d)){
				set.remove(d);
			}
		}
		//3. 现在set中剩下的就是要删除的了
		if(  set.size()>0){
			String[] todel = new String[set.size()];
			int i = 0;
			Iterator<String> ite = set.iterator();
			while (ite.hasNext()) {
				String s = ite.next();
				todel[i] = s;
				i++;
			}

			jedis.del(todel);
		}
	}

	/**
	 * 记录每天的登录用户, 生成的redis键为 onLineUserPerDay:yyyyMMdd
	 * user_id和位对应,比如user_id为1的用户上线就表示为010000000000000000...
	 * 又有用户上线的时候,比如user_id为2的用户上线就变成为011000000000000000...
	 * @param action
	 * @param user_id
	 * @return
	 */
	public static boolean activeUsers(Jedis jedis, long user_id) {
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return jedis.setbit(USERPERDAY + df.format(d), user_id, true);
	}

	/**
	 * 查看指定用户是否在线;
	 * 
	 * @param: key的格式
	 *             onLineUserPerDay:yyyyMMdd
	 */
	public static boolean isUserOnline(Jedis jedis, String key, long user_id) {
		return jedis.getbit(key, user_id);
	}

	/**
	 * 统计某天在线的人数 <br />
	 * 
	 * @param key的格式
	 *            onLineUserPerDay:yyyyMMdd
	 * bitcount 求位为1的个数
	 */
	public static long getOnLineUserCount(Jedis jedis, String key) {
		return jedis.bitcount(key);
	}

	/**
	 * 统计出n天连续上线的用户总数, 计算完后在redis中存好 键为 NDAYSBOTHONLINEUSERS
	 * @param n
	 *            几天
	 */
	public static Long getSeriesOnLineInNDays(Jedis jedis, int n) {
		// 循环求出七天所对应的键名: onLineUserPerDay:yyyyMMdd
		String[] sevenDays = new String[n];
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < n; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, -i);
			String key = USERPERDAY + df.format(c.getTime());
			sevenDays[i] = key;
		}
		jedis.bitop(BitOP.AND, NDAYSBOTHONLINEUSERS, sevenDays);
		return jedis.bitcount(NDAYSBOTHONLINEUSERS);
	}

	/**
	 * 统计出n天中曾经上过线的用户总数, 计算完后在redis中存好 键为 n_days_online_users
	 * @param: n
	 *             几天
	 */
	public static Long getOnLineInNDays(Jedis jedis, int n) {
		// 循环求出七天所对应的键名: onLineUserPerDay:yyyyMMdd
		String[] sevenDays = new String[n];
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < n; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -i);
			String key = USERPERDAY + df.format(c.getTime());
			sevenDays[i] = key;
		}
		jedis.bitop(BitOP.OR, NDAYSONLINEUSERS, sevenDays);
		return jedis.bitcount(NDAYSONLINEUSERS);
	}

}
