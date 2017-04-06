package com.zl.biz;

import com.zl.bean.Resuser;

public interface IResuserBiz {
	public Resuser loginCheck(Resuser user);

	public boolean isNewUser(Resuser newUser);

	public void addNewUser(Resuser newUser);
}
