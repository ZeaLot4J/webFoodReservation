package com.zl.biz.impl;

import java.util.List;

import com.zl.bean.Resuser;
import com.zl.biz.IResuserBiz;
import com.zl.dao.DBHelper;

public class ResuserBizImpl implements IResuserBiz{
	
	@Override
	public List<Resuser> getAllUsers(Integer pageNo, Integer rowNum) {
		DBHelper dao = new DBHelper();
		String sql = "select * from resuser limit "+(pageNo-1)*rowNum+","+rowNum;
		return dao.find(sql, Resuser.class);
	}

}
