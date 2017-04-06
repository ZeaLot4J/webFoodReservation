package com.zl.web.listeners;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zl.bean.Resfood;
import com.zl.biz.IResfoodBiz;
import com.zl.biz.impl.ResfoodBizImpl;

public class InitListener implements ServletContextListener{
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
	/**
	 * 监听application创建
	 */
	@Override
	public void contextInitialized(ServletContextEvent appEvent) {
		System.out.println("系统初始化...");
		IResfoodBiz rb = new ResfoodBizImpl();
		List<Resfood> foodList = rb.findAll();
		//1.利用application存储，缺点：JVM内存有限，无法扩展；当服务器关闭，就没有了
		//ServletContext application = appEvent.getServletContext();
		//application.setAttribute("foodList", foodList);
	}
}
