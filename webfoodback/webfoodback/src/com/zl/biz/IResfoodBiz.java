package com.zl.biz;

import java.util.List; 
import java.util.Map;

import com.zl.bean.Resfood;

public interface IResfoodBiz {
	
	public List<Resfood> findAll()  throws Exception;
	
	public Resfood getResfoodByFid( Integer fid) throws Exception;
	
	public List<Resfood> findByPage(int start ,int pageSize)  throws Exception;
	
	public int totalCount();
	
	public int addgoods(String fname,Double normprice,Double realprice,String detail,String fphoto) throws Exception;
	
	public int delgoods(String fid);
	
	public int updategoods(String fname,Double normprice,Double realprice,String detail,String fphoto,String fid);

	public List<Resfood> findGoods(Map<String, String> params, Integer pageFirst, Integer rows) throws Exception;
	
}
