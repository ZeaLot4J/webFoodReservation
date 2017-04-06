package com.zl.biz;

import java.util.List;

import com.zl.bean.Resuser;

public interface IResuserBiz {

	List<Resuser> getAllUsers(Integer pageNo, Integer rowNum);
	
}
