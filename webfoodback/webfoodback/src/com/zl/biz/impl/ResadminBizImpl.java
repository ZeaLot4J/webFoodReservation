package com.zl.biz.impl;

import java.util.ArrayList;
import java.util.List;
import com.zl.bean.Resadmin;
import com.zl.biz.IResadminBiz;
import com.zl.dao.DBHelper;
import com.zl.web.utils.Encrypt;

public class ResadminBizImpl implements IResadminBiz{

	@Override
	public Resadmin checkAdmin(Resadmin admin) {
		admin.setRapwd(Encrypt.md5(admin.getRapwd()));
		DBHelper dao = new DBHelper();
		List<Object> params = new ArrayList<Object>();
		params.add(admin.getRaname());
		params.add(admin.getRapwd()); 
		String sql = "select * from resadmin where raname=? and rapwd=?";
		List<Resadmin> admins = dao.find(sql, Resadmin.class, params);
		if(admins!=null && admins.size()>0){
			admin = admins.get(0);
		}else{
			admin = null;
		}
		return admin;
	}
}
