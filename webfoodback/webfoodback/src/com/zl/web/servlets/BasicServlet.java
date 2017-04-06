package com.zl.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public abstract class BasicServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected String op;

	protected void outJson(Object obj,HttpServletResponse resp) throws IOException{
		//以JSON格式返回客户端
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);
		System.out.println();
		System.out.println(jsonString);
		System.out.println();
		//以流的方式写出客户端，取流用response.getWriter()
		resp.setContentType("text/json; charset=UTF-8");//设置回传的数据格式
		PrintWriter out = resp.getWriter();
		out.println(jsonString);
		out.close();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		arg0.setCharacterEncoding("utf-8");
		op = arg0.getParameter("op");
		super.service(arg0, arg1);
	}

	protected <T> T parseRequest(HttpServletRequest request,Class<T> c){
		Map<String,String[]> map = request.getParameterMap();
		Set<String> keys = map.keySet();//取出所有的参数名
		Method[] ms=c.getDeclaredMethods(); //取出这个反射实例的所有方法

		T t = null;
		String type=null; //set方法参数类型名
		String[] v = null;//值
		try {
			t = c.newInstance();//实例化c类对象
			for(String k:keys){//username      pwd
				v = map.get(k);//参数名对应的值
				if(v!=null && v.length==1){
					for(Method m:ms){
						if(("set"+k).equalsIgnoreCase(m.getName())){//如果是对应的set方法
							type = m.getParameterTypes()[0].getName();//取得set方法的参数类型
							if("java.lang.Integer".equals(type)){
								m.invoke(t, Integer.parseInt(v[0]));
							}else if("java.lang.Float".equals(type)){
								m.invoke(t, Float.parseFloat(v[0]));
							}else if("java.lang.Double".equals(type)){
								m.invoke(t, Double.parseDouble(v[0]));
							}else if("java.math.BigDecimal".equals(type)){
								m.invoke(t, Double.parseDouble(v[0]));
							}else if("java.lang.Long".equals(type)){
								m.invoke(t, Long.parseLong(v[0]));
							}else{
								m.invoke(t, v[0]);
							}
						}
					}
				}//如果值是一个数组else{另一种情况}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * ����һ��״ֵ̬
	 * @param resp
	 * @param status
	 */
	protected void out(HttpServletResponse resp, String status){
		resp.setContentType("application/json");
		PrintWriter out = null;
		try{
			out = resp.getWriter();
			out.print(status);
			out.flush();
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			if(out!=null){
				out.close();
			}
		}
	}
	/**
	 * ���ص��������
	 * @param resp
	 * @param obj
	 */
	protected void out(HttpServletResponse resp, Object obj){
		resp.setContentType("application/json");
		PrintWriter out = null;
		
		try {
			Gson gson = new Gson();
			out = resp.getWriter();
			out.print(gson.toJson(obj));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				out.close();
			}
		}
	}
	/**
	 * ����һ�����϶����
	 * @param resp
	 * @param objs
	 */
	protected <T> void out(HttpServletResponse resp, List<T> objs){
		resp.setContentType("application/json");
		PrintWriter out = null;
		Gson gson = new Gson();
		try {
			out = resp.getWriter();
			System.out.println(gson.toJson(objs));
			out.print(gson.toJson(objs));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				out.close();
			}
		}
	}
	/**
	 * ���ض�����϶����
	 * @param resp
	 * @param map
	 */
	protected <T> void out(HttpServletResponse resp, Map<String,List<T>> map){
		resp.setContentType("application/json");
		PrintWriter out = null;
		Gson gson = new Gson();
		try {
			out = resp.getWriter();
			out.print(gson.toJson(map));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				out.close();
			}
		}
	}
	/**
	 * ���easyui�еķ�ҳ����
	 * @param resp
	 * @param map
	 */
	protected void outPage(HttpServletResponse resp, Map<String,Object> map){
		resp.setContentType("application/json");
		PrintWriter out = null;
		
		try {
			Gson gson = new Gson();
			out = resp.getWriter();
			out.print(gson.toJson(map));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out!=null){
				out.close();
			}
		}
	}
}
