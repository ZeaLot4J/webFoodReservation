package com.zl.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zl.bean.Resuser;
import com.zl.biz.IResuserBiz;
import com.zl.biz.impl.ResuserBizImpl;

public class ResuserServlet extends BasicServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(op==null || "".equals(op)){
			return;
		}else if("getallusers".equals(op)){
			getAllUsers(req,resp);
		}
	}

	private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) {
		String startNo = req.getParameter("page");
		String rowNum = req.getParameter("rows");
		IResuserBiz irb = new ResuserBizImpl();
		List<Resuser> userList = irb.getAllUsers(Integer.valueOf(startNo),Integer.valueOf(rowNum));
		super.out(resp, userList);
	}
}
