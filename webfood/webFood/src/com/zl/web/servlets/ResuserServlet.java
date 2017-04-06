package com.zl.web.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zl.bean.Resuser;
import com.zl.biz.IResuserBiz;
import com.zl.biz.impl.ResuserBizImpl;
import com.zl.utils.UserRedis;
import com.zl.utils.ZLConstants;
import com.zl.web.model.JsonModel;

import redis.clients.jedis.Jedis;

public class ResuserServlet extends BasicServlet{
	private IResuserBiz irb = new ResuserBizImpl(); 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if("".equals(op)){
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		}
		if("login".equals(op)){
			login(req,resp);
		}else if("logout".equals(op)){
			logOut(req,resp);
		}else if("islogin".equals(op)){
			isLogin(req,resp);
		}else if("signin".equals(op)){
			signIn(req,resp);
		}
	}
	
	private void signIn(HttpServletRequest req, HttpServletResponse resp) {
		Resuser newUser = super.parseRequest(req, Resuser.class);
		IResuserBiz irb = new ResuserBizImpl();
		JsonModel jm = new JsonModel();
		try {
			if(!irb.isNewUser(newUser)){//如果用户的相关信息已经重复
				jm.setCode(0);
				jm.setErrorMsg("用户信息已存在");
			}else{//如果用户是新的
				irb.addNewUser(newUser);
				jm.setCode(1);
				jm.setObj(null);
			}
			super.outJson(jm, resp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void isLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		JsonModel jm = new JsonModel();
		if(session.getAttribute(ZLConstants.LOGIN_USER)!=null){
			Resuser user = (Resuser) session.getAttribute(ZLConstants.LOGIN_USER);
			jm.setCode(1);
			user.setPwd(null);
			jm.setObj(user);
		}else{
			jm.setCode(0);
		}
		super.outJson(jm, resp);
	}

	private void logOut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		session.removeAttribute(ZLConstants.LOGIN_USER);
		//退出也不要删掉购物车 
		JsonModel jm = new JsonModel();
		jm.setCode(1);
		super.outJson(jm, resp);
	}

	private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		Resuser user = super.parseRequest(req, Resuser.class);
		JsonModel jm = new JsonModel();
		HttpSession session = req.getSession();
		String verifyCode =  session.getAttribute(ZLConstants.VERIFYCODE).toString();
		if(user.getUsername()==null||"".equals(user.getUsername())
		 ||user.getPwd()==null||"".equals(user.getPwd())||verifyCode==null||"".equals(verifyCode)){
			jm.setCode(0);
			jm.setErrorMsg("请填写用户名、密码和验证码!");
			super.outJson(jm, resp);
			return;
		}
		if(!verifyCode.equalsIgnoreCase(user.getValcode())){
			jm.setCode(0);
			jm.setErrorMsg("验证码输入错误!");
			super.outJson(jm, resp);
			return;
		}
		
		user = irb.loginCheck(user);//先执行这一行，user才有id
		session.setAttribute(ZLConstants.LOGIN_USER, user);
		
		if(user!=null){
			UserRedis.activeUsers(new Jedis(ZLConstants.REDIS_URL,ZLConstants.REDIS_PORT), user.getUserid());
			jm.setCode(1);
			user.setPwd(null);
			jm.setObj(user);
			super.outJson(jm, resp);
			return;
		}
		jm.setCode(0);
		jm.setErrorMsg("用户名或密码有误");
		super.outJson(jm, resp);
	}
}
