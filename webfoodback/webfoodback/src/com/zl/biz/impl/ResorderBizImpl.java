package com.zl.biz.impl;

import java.util.List;
import java.util.Map;

import com.zl.bean.Resorder;
import com.zl.bean.Resorderitem;
import com.zl.biz.IResorderBiz;
import com.zl.dao.DBHelper;

public class ResorderBizImpl implements IResorderBiz{

	@Override
	public List<Map<String, Object>> getAllOrders(Integer pageNo, Integer rowNum) {
		DBHelper dao = new DBHelper();
		String sql = "select roid,username,address,tel,ordertime,deliverytime,ps from resorder o, resuser u where o.userid=u.userid limit "+(pageNo-1)*rowNum+","+rowNum;
		return dao.findSingle(sql);
	}

	@Override
	public List<Map<String, Object>> getAllItems() {
		DBHelper dao = new DBHelper();
		String sql = " select i.roiid,f.fname,i.dealprice,i.num from resorderitem i, resfood f where i.fid = f.fid";
		return dao.findSingle(sql);
	}

	@Override
	public List<String> getTotal() {
		String sql = "select sum(dealprice*num) from resorderitem";
		DBHelper dao = new DBHelper();
		return dao.find(sql);
	}

}
