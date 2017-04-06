package com.zl.biz.impl;

import java.util.ArrayList;
import java.util.List;

import com.zl.bean.Resuser;
import com.zl.biz.IResuserBiz;
import com.zl.dao.DBHelper;
import com.zl.utils.Encrypt;

public class ResuserBizImpl implements IResuserBiz{

	@Override
	public Resuser loginCheck(Resuser user) {
		user.setPwd(Encrypt.md5(user.getPwd()));
		DBHelper dao = new DBHelper();
		List<Object> params = new ArrayList<Object>();
		params.add(user.getUsername());
		params.add(user.getPwd());
		String sql = "select * from resuser where username=? and pwd=?";
		List<Resuser> users = dao.find(sql, Resuser.class, params);
		if(users!=null && users.size()>0){
			user = users.get(0);
		}else{
			user = null;
		}
		return user;
	}

	@Override
	public boolean isNewUser(Resuser newUser) {
		DBHelper dao = new DBHelper();
		newUser.setPwd(Encrypt.md5(newUser.getPwd()));
		String sql = "select userid from resuser where username=? or pwd=? or email=?";
		if(dao.findSingle(sql, newUser.getUsername(),newUser.getPwd(),newUser.getEmail()).size()>0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void addNewUser(Resuser newUser) {
		String sql = "insert into resuser(username,pwd,email) values(?,?,?)";
		DBHelper dao = new DBHelper();
		dao.update(sql, newUser.getUsername(),newUser.getPwd(),newUser.getEmail());
	}
}
