package com.zl.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zl.bean.Resadmin;
import com.zl.bean.Resfood;

public class Test2 {
	public static void main(String[] args) {
//		DBHelper dao = new DBHelper();
//		String sql = "select * from resfood where fname = ?";
//		List<Object> params = new ArrayList<Object>();
//		params.add("手撕前女友");
//		List<Resfood> foodList = dao.find(sql, Resfood.class,params);
//		
//		for(Resfood rf:foodList){
//			System.out.println(rf.toString());
//		}
		Class<?> c = null;
		String className = "com.zl.dao.Person";
		String methodName = "";
		try {
			c  = Class.forName(className);
			methodName = "setName";
			Person p = (Person) c.newInstance();
			Field fs = c.getDeclaredField("name");
			//Field类的getType()方法，可以生成set方法的类型
			//实现 通过属性名拼接来查找相应的set get方法，再调用之
			Class paramType = fs.getType();
			c.getMethod(methodName, paramType).invoke(p, "zl");
			System.out.println(p.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
class Person{
	private String name;
	private int age;
	public Person() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
}