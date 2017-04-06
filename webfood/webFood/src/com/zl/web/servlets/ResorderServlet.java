package com.zl.web.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.internal.compiler.ast.SynchronizedStatement;

import com.zl.bean.Resfood;
import com.zl.bean.Resorder;
import com.zl.bean.Resuser;
import com.zl.biz.IResfoodBiz;
import com.zl.biz.IResorderBiz;
import com.zl.biz.IUserRankBiz;
import com.zl.biz.impl.ResfoodBizImpl;
import com.zl.biz.impl.ResorderBizImpl;
import com.zl.biz.impl.UserRankBizImpl;
import com.zl.utils.ZLConstants;
import com.zl.web.model.JsonModel;

public class ResorderServlet extends BasicServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if("order".equals(op)){
			orderFood(req,resp);
		}else if("delorder".equals(op)){
			delOrder(req,resp);
		}else if("delall".equals(op)){
			delAll(req,resp);
		}else if("showorder".equals(op)){
			showOrder(req,resp);
		}else if("confirmorder".equals(op)){
			confirmorder(req,resp);
		}
	}
	
	private void confirmorder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		JsonModel jm = new JsonModel();
		//如果发现还没登录
		if(session.getAttribute(ZLConstants.LOGIN_USER)==null){//最后一道防线
			jm.setCode(0);
			jm.setErrorMsg("您还未登录");
			super.outJson(jm, resp);
			return;
		}
		//如果购物车里没有东西
		if(session.getAttribute(ZLConstants.CART_NAME)==null){
			jm.setCode(0);
			jm.setErrorMsg("您还未选择任何一道菜");
			super.outJson(jm, resp);
			return;
		}
		Map<Integer,Resfood> cart = (Map<Integer, Resfood>) session.getAttribute(ZLConstants.CART_NAME);
		Resuser user = (Resuser) session.getAttribute(ZLConstants.LOGIN_USER);
		Resorder order = super.parseRequest(req, Resorder.class);
		order.setUserid(user.getUserid());//把用户的ID填入订单中
		order.setStatus(0);//0代表...
		//提交订单
		IResorderBiz irb = new ResorderBizImpl();
		try {
			irb.submitOrder(order, cart);
			//计算总价
			double total = 0;
			for(Map.Entry<Integer, Resfood> entry:cart.entrySet()){
				total += entry.getValue().getRealprice()*entry.getValue().getNum();
			}
			IUserRankBiz iurb = new UserRankBizImpl();
			iurb.updateScore(total, user.getUserid());
			//删除购物车
			session.removeAttribute(ZLConstants.CART_NAME);
			jm.setCode(1);
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrorMsg(e.getMessage());
		}
		super.outJson(jm, resp);
	}

	private void delAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		session.removeAttribute(ZLConstants.CART_NAME);
		
		JsonModel jm = new JsonModel();
			jm = new JsonModel();
			jm.setCode(1);
		super.outJson(jm, resp);
	}

	private void showOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		Map<Integer,Resfood> cart = new HashMap<Integer,Resfood>();
		//判断session中map是否已经存在,如果已有，则cart为session中的cart；如果没有，则用新建的cart
		if(session.getAttribute(ZLConstants.CART_NAME)!=null){
			cart = (Map<Integer, Resfood>) session.getAttribute(ZLConstants.CART_NAME);
		}
		
		JsonModel jm = new JsonModel();
		if(cart==null){
			jm.setCode(0);
		}else{
			jm.setObj(cart);
			jm.setCode(1);
		}
		super.outJson(jm, resp);
	}

	//删除一条订单记录
	private void delOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Resfood rf = super.parseRequest(req, Resfood.class);
		HttpSession session = req.getSession();
		Map<Integer,Resfood> cart = new HashMap<Integer,Resfood>();
		//判断session中map是否已经存在,如果已有，则cart为session中的cart；如果没有，则用新建的cart
		if(session.getAttribute(ZLConstants.CART_NAME)!=null){
			cart = (Map<Integer, Resfood>) session.getAttribute(ZLConstants.CART_NAME);
		}
		if(cart.containsKey(rf.getFid())){
			cart.remove(rf.getFid());
		}
		
		JsonModel jm = null;
		try {
			// code: 1 -> 成功，obj，存数据
			// code: 0 -> 失败，msg，错误信息
			jm = new JsonModel();
			jm.setCode(1);
			jm.setObj(cart);
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrorMsg(e.getMessage());
		}
		super.outJson(jm, resp);
	}

	private void orderFood(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//此时rf里只有fid和num有值
		Resfood rf = super.parseRequest(req, Resfood.class);
		HttpSession session = req.getSession();
		Map<Integer,Resfood> cart = new HashMap<Integer,Resfood>();
		//判断session中map是否已经存在,如果已有，则cart为session中的cart；如果没有，则用新建的cart
		if(session.getAttribute(ZLConstants.CART_NAME)!=null){
			cart = (Map<Integer, Resfood>) session.getAttribute(ZLConstants.CART_NAME);
		}
		
		JsonModel jm = null;
		try {
			//如果购物车cart中有这道菜，则取出来数量加(或减)起来
			if(cart.containsKey(rf.getFid())){
				Resfood resfood = cart.get(rf.getFid());
				int num = resfood.getNum()+rf.getNum();
				if(num<=0){
					cart.remove(rf.getFid());
				}else{
					resfood.setNum(num);
					cart.put(resfood.getFid(), resfood);//更新
				}
			}else{//如果没买这道菜，则数量为1
				IResfoodBiz rbi = new ResfoodBizImpl();
				Resfood resfood = rbi.getResfoodByFid(rf.getFid());
				resfood.setNum(rf.getNum());
				cart.put(rf.getFid(), resfood);
			}
			session.setAttribute(ZLConstants.CART_NAME, cart);
			// code: 1 -> 成功，obj，存数据
			// code: 0 -> 失败，msg，错误信息
			jm = new JsonModel();
			jm.setCode(1);
			jm.setObj(cart);
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrorMsg(e.getMessage());
		}
		super.outJson(jm, resp);
	}
}
