package com.zl.biz;

import java.util.List;
import java.util.Map;

import com.zl.bean.Resorder;
import com.zl.bean.Resorderitem;

public interface IResorderBiz {

	public List<Map<String, Object>> getAllOrders(Integer pageNo, Integer rowNum);
	
	public List<Map<String, Object>> getAllItems();

	public List<String> getTotal();
}
