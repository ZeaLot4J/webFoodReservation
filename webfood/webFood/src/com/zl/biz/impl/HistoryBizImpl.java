package com.zl.biz.impl;

import java.io.IOException;
import java.util.Date;

import com.zl.biz.IHistoryBiz;
import com.zl.utils.ZLConstants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class HistoryBizImpl implements IHistoryBiz{
	private Jedis jedis = new Jedis(ZLConstants.REDIS_URL,ZLConstants.REDIS_PORT);
	public static final String REDIS_USER_HISTORY = "uid:";
	@Override
	/**
	 * 在redis中的格式为uid:1    时间戳    菜       时间戳    菜       时间戳    菜
	 */
	public void saveHistory(int uid, String data) throws IOException {
		//求出时间
		Date d = new Date();
		Pipeline pl = jedis.pipelined();
		pl.zadd(REDIS_USER_HISTORY+uid, d.getTime(),data);
		pl.expire(REDIS_USER_HISTORY+uid,24*60*60);
		Response<Long> resp = pl.zcard(REDIS_USER_HISTORY+uid);
		pl.close();
	}

	@Override
	public Object[] getHistory(int uid) {
		return jedis.zrange(REDIS_USER_HISTORY+uid, 0, 9).toArray();
	}
	
}
