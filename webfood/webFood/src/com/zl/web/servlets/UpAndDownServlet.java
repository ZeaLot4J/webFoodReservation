package com.zl.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

import com.zl.bean.Resuser;
import com.zl.biz.IResfoodBiz;
import com.zl.biz.impl.ResfoodBizImpl;
import com.zl.utils.UpAndDownUtil;
import com.zl.utils.ZLConstants;
import com.zl.web.model.JsonModel;

import redis.clients.jedis.Jedis;

public class UpAndDownServlet extends BasicServlet{
	private Jedis jedis = new Jedis(ZLConstants.REDIS_URL,ZLConstants.REDIS_PORT);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if(op==null||"".equals(op)){
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}else if("up".equals(op)){
				up(req,resp);
			}else if("down".equals(op)){
				down(req,resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void up(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String fid = req.getParameter("fid");
		Resuser user = (Resuser) req.getSession().getAttribute(ZLConstants.LOGIN_USER);
		JsonModel jm = new JsonModel();
		
		if(UpAndDownUtil.isUpFood(jedis, Integer.parseInt(fid), user.getUserid())==true ||
		   UpAndDownUtil.isDownFood(jedis, Integer.parseInt(fid), user.getUserid())==true){
			jm.setCode(0);
			jm.setErrorMsg("您今天已经评价过");
			super.outJson(jm, resp);
			return;
		}
		
		IResfoodBiz irb = new ResfoodBizImpl();
		irb.upFoodById(Integer.parseInt(fid));
		UpAndDownUtil.upFood(jedis, Integer.parseInt(fid), user.getUserid());
		
		jm.setCode(1);
		jm.setErrorMsg("您的支持是我们最大的动力!");
		super.outJson(jm, resp);
	}

	private void down(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String fid = req.getParameter("fid");
		Resuser user = (Resuser) req.getSession().getAttribute(ZLConstants.LOGIN_USER);
		JsonModel jm = new JsonModel();
		if(UpAndDownUtil.isUpFood(jedis, Integer.parseInt(fid), user.getUserid())==true ||
				   UpAndDownUtil.isDownFood(jedis, Integer.parseInt(fid), user.getUserid())==true){
					jm.setCode(0);
					jm.setErrorMsg("您今天已经评价过");
					super.outJson(jm, resp);
					return;
				}
		IResfoodBiz irb = new ResfoodBizImpl();
		irb.downFoodById(Integer.parseInt(fid));
		UpAndDownUtil.downFood(jedis, Integer.parseInt(fid), user.getUserid());
		
		jm.setCode(1);
		jm.setErrorMsg("对此我们深表歉意...");
		super.outJson(jm, resp);
	}
}
