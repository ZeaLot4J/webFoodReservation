package com.zl.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;
public class TagUtil {
	public final static String ITEM = "item:";
	public final static String TAG = "tag:";
	public final static int EXPIRETIME = 365 * 24 * 60;
	/**
	 * 存储条目:一个name可能会属于哪些tags
	 * @param jedis
	 * @param id：某物的id
	 * @param name：某物的名称
	 * @param tags：tags存放某物所属的标签，一个或多个，比如，java编程属于java标签和编程标签
	 */
	public static void saveItem(Jedis jedis,String id, String name,List<String> tags){
		jedis.set(ITEM+id, name);
		jedis.expire(ITEM+id, EXPIRETIME);
		for(String tag:tags){
			jedis.sadd(TAG+tag, id);
		}
	}
	/**
	 * 取得某些标签下的所有条目(交集、并集、差集)
	 * @param jedis
	 * @param tags
	 * @return
	 */
	public static List<Map<String,String>> getMultiAnd(Jedis jedis,List<String> tags){
		String[] keys = getKeysFromSets(tags);
		Set<String> ids = jedis.sinter(keys);//得到id的交集
		return getItemByIdS(jedis,ids);
	}
	public static List<Map<String,String>> getMultiOr(Jedis jedis,List<String> tags){
		String[] keys = getKeysFromSets(tags);
		Set<String> ids = jedis.sunion(keys);//得到id的并集
		return getItemByIdS(jedis,ids);
	}
	public static List<Map<String,String>> getMultiDiff(Jedis jedis,List<String> tags){
		String[] keys = getKeysFromSets(tags);
		Set<String> ids = jedis.sdiff(keys);//得到id的并集
		return getItemByIdS(jedis,ids);
	}
	/**
	 * 根据所有的id取得对应的所有条目item
	 * @param jedis
	 * @param ids
	 * @return
	 */
	public static List<Map<String,String>> getItemByIdS(Jedis jedis,Set<String> ids){
		List<Map<String,String>> items = new ArrayList<Map<String,String>>();
		Iterator<String> iter = ids.iterator();
		String id = null;
		Map<String,String> map = null;
		while(iter.hasNext()){
			id = iter.next();
			map = new HashMap<String,String>(); 
			map.put(id, jedis.get(ITEM+id));
			items.add(map);
		}
		return items;
	}
	/**
	 * 取得多个标签（集合）的名字
	 * @param tags
	 * @return
	 */
	public static String[] getKeysFromSets(List<String> tags){
		int len = tags.size();
		String[] keys = new String[len]; 
		for(int i=0; i<len; i++){
			keys[i] = TAG+tags.get(i);
		}
		return keys;
	}
}
