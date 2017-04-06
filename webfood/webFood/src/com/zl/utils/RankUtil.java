package com.zl.utils;
import java.util.Iterator;
import java.util.Set;
import redis.clients.jedis.Jedis;
/**
 *	1.存储到redis数据库中
 *	2.求top n
 *	3.求某个用户的排名
 */
public class RankUtil {
	public static final String RANKBOARD = "rankboard";//集合名
	public static void main(String[] args) {
		Jedis jedis = new Jedis();
		updateScore(jedis, 3253, "a");
		updateScore(jedis, 4315, "b");
		updateScore(jedis, 4657, "c");
		updateScore(jedis, 7896, "d");
		updateScore(jedis, 4678, "e");
		updateScore(jedis, 2367, "f");
		updateScore(jedis, 88, "g");
		updateScore(jedis, 324, "h");
		updateScore(jedis, 5657, "i");
		updateScore(jedis, 8654, "j");
		Set<String> res = getTopN(jedis,10);
		Iterator<String> iter = res.iterator();
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
		System.out.println(getRankByUser(jedis, "g"));
	}
	/**
	 * 设置积分
	 * @param jedis：redis的连接
	 * @param key：要存储的集合名，如果key为空，则默认用上面的RANKBOARD
	 * @param score：排名的分数，分数的算法由调用者决定
	 * @param user：用户ID或用户名
	 * @return
	 */
	public static Double updateScore(Jedis jedis, String key, double score, String user){
		if(jedis==null){
			throw new RuntimeException("jedis connection cannot be null");
		}
		if(key==null){
			key = RANKBOARD;
		}
		return jedis.zincrby(key, score,user);
		/*long id = jedis.zadd(key, score,user);
		//只保留前面的一些数据
		if(jedis.zcard(key)>2){
			jedis.zremrangeByRank(key, 0L, 2L);
		}
		return id;*/
	}//重载一次
	public static Double updateScore(Jedis jedis, double score, String user){
		return updateScore(jedis,null,score,user);
	}
	/**
	 * 求TOP N个数据
	 * @param jedis：
	 * @param key：集合名
	 * @param topN：提取的前几个数据
	 * @return
	 */
	public static Set<String> getTopN(Jedis jedis,String key,int topN){
		if(jedis==null){
			throw new RuntimeException("jedis connection cannot be null");
		}
		if(key==null){
			key = RANKBOARD;
		}
		return jedis.zrevrange(key, 0, topN-1);
	}
	public static Set<String> getTopN(Jedis jedis,int topN){
		return getTopN(jedis,null,topN);
	}
	/**
	 * 取得用户的排名
	 * @param jedis
	 * @param key：集合名
	 * @param user：用户名
	 * @return 返回的结果按照习惯从1开始算
	 */
	public static long getRankByUser(Jedis jedis,String key,String user){
		if(jedis==null){
			throw new RuntimeException("jedis connection cannot be null");
		}
		if(key==null){
			key = RANKBOARD;
		}
		return jedis.zrevrank(key, user)+1;
	}
	public static long getRankByUser(Jedis jedis,String user){
		return getRankByUser(jedis,null,user);
	}
}
