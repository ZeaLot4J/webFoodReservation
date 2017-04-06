package com.zl.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;
/**
 * 用于保存每道菜被用户顶过的记录
 * @author ZeaLot
 */
public class UpAndDownUtil {
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
			String key = "up:"+ZLConstants.ALLFOOD+":*:" + df.format(c.getTime());
			nDays[i] = key;
		}
		//2. 求出redis中现存有哪几天的登录数据
		Set<String> set=jedis.keys("up:"+ZLConstants.ALLFOOD+":*:*");
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
	public static boolean upFood(Jedis jedis,long fid, long user_id) {
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return jedis.setbit("up:"+ZLConstants.ALLFOOD+":" +fid+":"+ df.format(d), user_id, true);
	}
	public static boolean downFood(Jedis jedis,long fid, long user_id) {
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return jedis.setbit("down:"+ZLConstants.ALLFOOD+":" +fid+":"+ df.format(d), user_id, true);
	}
	public static boolean isUpFood(Jedis jedis,long fid, long user_id){
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return jedis.getbit("up:"+ZLConstants.ALLFOOD+":" +fid+":"+ df.format(d), user_id);
	}
	public static boolean isDownFood(Jedis jedis,long fid, long user_id){
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return jedis.getbit("down:"+ZLConstants.ALLFOOD+":" +fid+":"+ df.format(d), user_id);
	}
}
