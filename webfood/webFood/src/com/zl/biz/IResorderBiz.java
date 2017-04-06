package com.zl.biz;

import java.util.Map;

import com.zl.bean.Resfood;
import com.zl.bean.Resorder;

public interface IResorderBiz {
	public void submitOrder(Resorder order,Map<Integer,Resfood> cart) throws Exception;
}
