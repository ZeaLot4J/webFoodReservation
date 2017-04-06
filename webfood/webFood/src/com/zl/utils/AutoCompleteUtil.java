package com.zl.utils;
import java.io.IOException; 
import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
/**
 *	搜索中的自动提示功能：
 *	1.记录用户的输入
 *		根据用户输入的前缀字符来查找redis中对应的用户的集合
 *
 * 	分析：键的设计：
 * 		recentInfo:1    中国  中国人民大学 美国 美国华人 (这次用List,速度快,
 * 										         用sortedset：存入一个时间，按时间排序，认为用户最近的搜索优先级高)
 * 											用set不重复
 * 		recentInfo:2    中国人民大学 美国 美国华人
 * 		recentInfo:2    美国 美国华人
 *
 */
public class AutoCompleteUtil {
	public static void main(String[] args) throws IOException {
		Jedis jedis = new Jedis(ZLConstants.REDIS_URL,ZLConstants.REDIS_PORT);
		AutoCompleteUtil.addRecentInfo(jedis, "1", "a",5);
		AutoCompleteUtil.addRecentInfo(jedis, "1", "b",5);
		AutoCompleteUtil.addRecentInfo(jedis, "1", "c",5);
		AutoCompleteUtil.addRecentInfo(jedis, "1", "d",5);
		AutoCompleteUtil.addRecentInfo(jedis, "1", "e",5);
		AutoCompleteUtil.addRecentInfo(jedis, "1", "f",5);
		AutoCompleteUtil.addRecentInfo(jedis, "1", "g",5);
		AutoCompleteUtil.addRecentInfo(jedis, "1", "h",5);
		List<String> res = getRecentInfo(jedis,"1");
		for(String s:res){
			System.out.println(s);
		}
	}
	public static final String RECENTINFO = "recentInfo:";
	public static void addRecentInfo(Jedis jedis,String id,String info,int n) throws IOException{
		String key = RECENTINFO + id;
		//redis的事务:
		//启动:multi() 提交：exec() 回滚：discard() 关闭：close() 
		//使用管道
		Pipeline pl = jedis.pipelined();
		pl.multi();
		pl.lrem(key, 1, info);//去除相同的
		pl.lpush(key, info);
		pl.ltrim(key, 0, n-1);//只保留n条
		pl.exec();
		pl.close();
	}
	public static List<String> getRecentInfo(Jedis jedis,String id){
		return jedis.lrange(RECENTINFO+id, 0, -1);
	}
}
