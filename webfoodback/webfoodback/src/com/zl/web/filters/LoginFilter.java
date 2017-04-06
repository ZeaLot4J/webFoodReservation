package com.zl.web.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zl.web.utils.ZLConstants;

/**
 * 过滤器可以在访问servlet之前对请求进行过滤，访问servlet之后对响应做过滤
 * 
 * @author v5-572G
 *
 */
public class LoginFilter implements Filter{
	private String errorPage = "login.html";
	@Override			//FilterConfig对象获取参数
	public void init(FilterConfig arg0) throws ServletException {
		if(arg0.getInitParameter("errorPage")!=null){
			errorPage = arg0.getInitParameter("errorPage");
		}else if(arg0.getServletContext().getInitParameter("errorPage")!=null){//有没有配置全局的错误页面
			errorPage = arg0.getInitParameter("errorPage");
		}
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain arg2)
			throws IOException, ServletException {
		//判断用户是否已经登录，只需要查看session中的currentUserName属性是否为空
		//因为用户在成功登录后，会将当前登录的用户信息存到session中
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		HttpSession session = request.getSession();
		
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		
		if(session.getAttribute(ZLConstants.LOGIN_ADMIN)==null){//如果用户没有登录
			response.setContentType("text/html;charset=utf-8");
			//将当前请求截断，立即回送一个请求登录的信息给客户端
			PrintWriter out = response.getWriter();
			out.println("<script>location.href='"+basePath+errorPage+"';</script>");
			out.flush();
			out.close();
		}else{
			//如果已经登录，说明已经满足了过滤的要求，则将此请求传递给下一个过滤器
			arg2.doFilter(req, resp);
		}
	}

	@Override
	public void destroy() {
		System.out.println("过滤器已经销毁");
	}


}

