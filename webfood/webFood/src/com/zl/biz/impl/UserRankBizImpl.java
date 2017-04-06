package com.zl.biz.impl;

import com.zl.biz.IUserRankBiz;
import com.zl.utils.RankUtil;
import com.zl.utils.ZLConstants;

import redis.clients.jedis.Jedis;

public class UserRankBizImpl implements IUserRankBiz{
	private Jedis jedis = new Jedis(ZLConstants.REDIS_URL,ZLConstants.REDIS_PORT);
	/**
	 * 按金额排名
	 */
	@Override
	public void updateScore(double total, int userid) {
		double score = total/100;//暂时用总金额/100来排名
		RankUtil.updateScore(jedis, score, String.valueOf(userid));
	}

}
