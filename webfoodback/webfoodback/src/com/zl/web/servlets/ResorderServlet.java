package com.zl.web.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zl.bean.Resorder;
import com.zl.bean.Resorderitem;
import com.zl.biz.IResorderBiz;
import com.zl.biz.impl.ResorderBizImpl;
import com.zl.web.model.JsonModel;

public class ResorderServlet extends BasicServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(op==null || "".equals(op)){
			return;
		}else if("getallorders".equals(op)){
			getAllOrders(req,resp);
		}else if("getallitems".equals(op)){
			getOrderItemsById(req,resp);
		}else if("gettotal".equals(op)){
			getTotal(req,resp);
		}
	}


	private void getTotal(HttpServletRequest req, HttpServletResponse resp) {
		IResorderBiz irb = new ResorderBizImpl();
		String result = irb.getTotal().get(0);
		JsonModel jm = new JsonModel();
		try {
			jm.setCode(1);
			jm.setObj(result);
			super.outJson(jm, resp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void getOrderItemsById(HttpServletRequest req, HttpServletResponse resp) {
		IResorderBiz irb = new ResorderBizImpl();
		List<Map<String,Object>> itemList = irb.getAllItems();
		super.out(resp, itemList);
	}

	private void getAllOrders(HttpServletRequest req, HttpServletResponse resp) {
		String startNo = req.getParameter("page");
		String rowNum = req.getParameter("rows");
		IResorderBiz irb = new ResorderBizImpl();
		List<Map<String,Object>> orderList = irb.getAllOrders(Integer.valueOf(startNo),Integer.valueOf(rowNum));
		super.out(resp, orderList);
	}
}
