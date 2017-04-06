package com.zl.web.servlets;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zl.bean.Resfood;
import com.zl.bean.Resuser;
import com.zl.biz.IResfoodBiz;
import com.zl.biz.impl.ResfoodBizImpl;
import com.zl.utils.ZLConstants;
import com.zl.web.model.JsonModel;

public class IndexServlet extends BasicServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Object> result = new ArrayList<Object>();
		//获取所有的菜
		IResfoodBiz rb = new ResfoodBizImpl();
		List<Resfood> foodList = rb.findAll();
		result.add(foodList);//[0]
		//获取购物车
		HttpSession session = req.getSession();
		Map<Integer,Resfood> cart = new HashMap<Integer,Resfood>();
		if(session.getAttribute(ZLConstants.CART_NAME)!=null){
			cart = (Map<Integer, Resfood>) session.getAttribute(ZLConstants.CART_NAME);
			result.add(cart);//[1]
		}else{
			result.add(null);
		}
		//如果登录过，则获取用户信息
		if(session.getAttribute(ZLConstants.LOGIN_USER)!=null){
			Resuser user = (Resuser) session.getAttribute(ZLConstants.LOGIN_USER);
			user.setPwd(null);
			result.add(user);//[2]
		}else{
			result.add(null);
		}
		JsonModel jm = new JsonModel();
		jm.setCode(1);
		jm.setObj(result);
		super.outJson(jm, resp);
	}
}
