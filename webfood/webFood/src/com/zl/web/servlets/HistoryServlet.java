package com.zl.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zl.bean.Resuser;
import com.zl.biz.IHistoryBiz;
import com.zl.biz.impl.HistoryBizImpl;
import com.zl.utils.ZLConstants;
import com.zl.web.model.JsonModel;

public class HistoryServlet extends BasicServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if(op==null||"".equals(op)){
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}else if("saveHistory".equals(op)){
				saveHistory(req,resp);
			}else if("getHistory".equals(op)){
				getHistory(req,resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getHistory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		Resuser user = (Resuser) session.getAttribute(ZLConstants.LOGIN_USER);
		IHistoryBiz ihb = new HistoryBizImpl();		
		Object[] topNHistory = ihb.getHistory(user.getUserid());
		JsonModel jm = new JsonModel();
		jm.setCode(1);
		jm.setObj(topNHistory);
		
		super.outJson(jm, resp);
	}

	private void saveHistory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String historyData = req.getParameter("data");
		HttpSession session = req.getSession();
		Resuser user = (Resuser) session.getAttribute(ZLConstants.LOGIN_USER);
		IHistoryBiz ihb = new HistoryBizImpl();
		//向redis中存入1条浏览记录
		ihb.saveHistory(user.getUserid(), historyData);
		//从redis中取出最后10条浏览记录覆盖到浏览框
		Object[] topNHistory = ihb.getHistory(user.getUserid());
		
		JsonModel jm = new JsonModel();
		jm.setCode(1);
		jm.setObj(topNHistory);
		
		super.outJson(jm, resp);
	}
}
