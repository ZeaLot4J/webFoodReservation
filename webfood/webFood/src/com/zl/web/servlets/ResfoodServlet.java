package com.zl.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.zl.bean.Resfood;
import com.zl.biz.IResfoodBiz;
import com.zl.biz.impl.ResfoodBizImpl;
import com.zl.web.model.JsonModel;

public class ResfoodServlet extends BasicServlet{
	private static final long serialVersionUID = 1L;
	private IResfoodBiz rb = new ResfoodBizImpl();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException{
		try {
			if(op==null||"".equals(op)){
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}else if("findAll".equals(op)){
				findAll(req,resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		JsonModel jm = null;
		try {
			List<Resfood> foodList = rb.findAll();
			// code: 1 -> 成功，obj，存数据
			// code: 0 -> 失败，msg，错误信息
			jm = new JsonModel();
			jm.setCode(1);
			jm.setObj(foodList);
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrorMsg(e.getMessage());
		}
		super.outJson(jm, resp);
	}


}
