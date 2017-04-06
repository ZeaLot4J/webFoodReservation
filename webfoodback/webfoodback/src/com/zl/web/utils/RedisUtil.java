package com.zl.web.utils;

import java.lang.reflect.Field; 
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zl.bean.Resfood;

import redis.clients.jedis.Jedis;

public class RedisUtil {
	/**
	 * 一个allfood:#为一道菜 
	 * allfood:编号  名字 值 原价 值 现价 值 信息 值 图片 值
	 * @param jedis
	 * @param hashNamePrefix
	 * @param id
	 * @param list
	 * @param c
	 * hset hashName 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	//saveToHash(jedis,ALLFOOD:xxx,1,rf,Resfood.class);
	public static <T> void saveToHash(Jedis jedis,String hashNamePrefix,String id,List<T> list,Class<T> c){
		jedis.connect();
		if(jedis==null || jedis.isConnected()==false){
			return;
		}
		String idMethod = "get" + id.substring(0, 1).toUpperCase() + id.substring(1);
		String hashName = "";//hash表名
		Field[] fs = c.getDeclaredFields();
		Method m = null;
		String field = "";
		String method = "";
		for(T t:list){
			try {
				hashName = hashNamePrefix + ":" + c.getDeclaredMethod(idMethod).invoke(t).toString();
				for(Field f:fs){
					field = f.getName();
					//如果属性是序列号则跳过
					if("serialVersionUID".equals(field)) continue;
					method = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
					m = c.getDeclaredMethod(method);
					Object obj = m.invoke(t);
					//System.out.println(obj);
					if(obj!=null){//因为Resfood多个了num，数据库里没有，防止空指针，要排除
						jedis.hset(hashName, field, obj.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 从redis中取出，放入javaBean
	 * @param jedis
	 * @param hashNamePrefix
	 * @param c
	 * @return
	 */
	public static <T> List<T> getFromHash(Jedis jedis,String hashName,Class<T> c) {
		List<T> list = new ArrayList<T>();
		T t = null;
		Set<String> hashNames = jedis.keys(hashName);
		String field = "";//属性
		String value = "";//值
		String method = "";//方法
		Class<?> type = null;//参数类型
		String beanType = null;//javaBean中的属性的类型
		for(String s:hashNames){
			try {
				t = c.newInstance();
				Map<String,String> map = jedis.hgetAll(s);
				for(Map.Entry<String, String> entry:map.entrySet()){
					field = entry.getKey();
					value = entry.getValue();
					type = c.getDeclaredField(field).getType();
					beanType = type.getName();
					method = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
					if("java.lang.Integer".equals(beanType)){
						c.getDeclaredMethod(method, type).invoke(t, Integer.valueOf(value));
					}else if("java.lang.Float".equals(beanType)){
						c.getDeclaredMethod(method, type).invoke(t, Float.valueOf(value));
					}else if("java.lang.Double".equals(beanType)){
						c.getDeclaredMethod(method, type).invoke(t, Double.valueOf(value));
					}else if("java.math.BigDecimal".equals(beanType)){
						c.getDeclaredMethod(method, type).invoke(t, Double.valueOf(value));
					}else if("java.lang.Long".equals(beanType)){
						c.getDeclaredMethod(method, type).invoke(t, Long.valueOf(value));
					}else{
						c.getDeclaredMethod(method, type).invoke(t, value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
			list.add(t);
		}
		return list;
	}
}