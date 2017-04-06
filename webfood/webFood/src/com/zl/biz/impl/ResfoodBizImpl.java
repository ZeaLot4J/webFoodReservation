package com.zl.biz.impl;

import java.util.ArrayList;
import java.util.List;

import com.zl.bean.Resfood;
import com.zl.biz.IResfoodBiz;
import com.zl.dao.DBHelper;
import com.zl.utils.RedisUtil;
import com.zl.utils.ZLConstants;

import redis.clients.jedis.Jedis;

public class ResfoodBizImpl implements IResfoodBiz{
	private DBHelper dao = new DBHelper();
	private Jedis jedis = new Jedis(ZLConstants.REDIS_URL,ZLConstants.REDIS_PORT);
	
	@Override
	public Resfood getResfoodByFid(Integer fid) {
		Resfood rf = null;
		List<Resfood> foodList = null;
		jedis.connect();
		if(jedis.isConnected()==true && jedis.keys(ZLConstants.ALLFOOD+":"+fid).size()>0){
			foodList = RedisUtil.getFromHash(jedis, ZLConstants.ALLFOOD+":"+fid, Resfood.class);
		}else{
			List<Object> params = new ArrayList<Object>();
			params.add(fid);
			foodList = dao.find("select * from resfood where fid=?", Resfood.class, params);
		}
		
		if(foodList != null && foodList.size()>0){
			rf = foodList.get(0);
		}
		
		return rf;
	}
	@Override
	public List<Resfood> findAll(){
		//1.判断redis中是否有数据，如果有，则用redis中的数据
		//2.没有则从mysql里查一次，再放入redis
		List<Resfood> foodList = null;
		jedis.connect();
		if(jedis.isConnected()==true&&jedis.keys(ZLConstants.ALLFOOD+":"+"*").size()>0){
			foodList = RedisUtil.getFromHash(jedis, ZLConstants.ALLFOOD+":"+"*", Resfood.class);
		}else{
			System.out.println("向redis中装填数据");
			foodList = dao.find("select * from resfood", Resfood.class);
			RedisUtil.saveToHash(jedis, ZLConstants.ALLFOOD, "fid", foodList, Resfood.class);
		}
		return foodList;
	}
	@Override//同时修改mysql和redis中的顶数和踩数
	public void upFoodById(Integer fid) {
		//mysql改成功了才去改redis,否则都不改
		if(dao.update("update resfood set up = up + 1 where fid = ?",fid)>=1){
			jedis.hincrBy(ZLConstants.ALLFOOD+":"+fid,"up", 1);
		}
	}
	@Override
	public void downFoodById(Integer fid) {
		if(dao.update("update resfood set down = down + 1 where fid = ?",fid)>=1){
			jedis.hincrBy(ZLConstants.ALLFOOD+":"+fid,"down", 1);
		}
	}
	
}
