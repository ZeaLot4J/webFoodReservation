package com.zl.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zl.dao.DBHelper;

public class Test1 {
	public static void main(String[] args) {
		DBHelper dao = new DBHelper();
		List<Object> params = new ArrayList<Object>();
		List<String> res = dao.find("select * from resfood", params);
		
		for(String s:res){
			System.out.println(s);
		}
	}
}
