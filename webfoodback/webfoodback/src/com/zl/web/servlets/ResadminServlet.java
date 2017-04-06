package com.zl.web.servlets;

import java.io.IOException; 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.zl.bean.Resadmin;
import com.zl.biz.IResadminBiz;
import com.zl.biz.impl.ResadminBizImpl;
import com.zl.web.model.JsonModel;
import com.zl.web.utils.ZLConstants;


public class ResadminServlet extends BasicServlet{
	private IResadminBiz irb = new ResadminBizImpl(); 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if(op==null||"".equals(op)){
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}else if("login".equals(op)){
				login(req,resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Resadmin admin = super.parseRequest(req, Resadmin.class);
		JsonModel jm = new JsonModel();
		HttpSession session = req.getSession();
		if(admin.getRaname()==null||"".equals(admin.getRaname())
		 ||admin.getRapwd()==null||"".equals(admin.getRapwd())){
			jm.setCode(0);
			jm.setErrorMsg("请填写用户名、密码!");
			super.outJson(jm, resp);
			return;
		}
		admin = irb.checkAdmin(admin);
		session.setAttribute(ZLConstants.LOGIN_ADMIN, admin);
		
		if(admin!=null){
			jm.setCode(1);
			admin.setRapwd(null);
			jm.setObj(admin);
			super.outJson(jm, resp);
			return;
		}
		jm.setCode(0);
		jm.setErrorMsg("用户名或密码有误");
		super.outJson(jm, resp);
	}
}
