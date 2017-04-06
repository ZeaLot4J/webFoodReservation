package com.zl.biz;

import java.util.List;

import com.zl.bean.Resfood;

public interface IResfoodBiz {
	public List<Resfood> findAll();
	
	public Resfood getResfoodByFid(Integer fid);
	
	public void upFoodById(Integer fid);
	public void downFoodById(Integer fid);
}
