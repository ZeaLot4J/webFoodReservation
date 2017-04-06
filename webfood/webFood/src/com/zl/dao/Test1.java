package com.zl.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;


public class Test1 {
	//此处的<T>不是返回值，表示的是这是个泛型函数
		public static <T> List<T> searchDept(String sql,Class<T> c){//定义一个泛型函数，
			List<T> res = new ArrayList<T>();
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306:webfood","root","a");
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				Method[] methods= c.getMethods();//取得c的所有方法
				
				//取得所有的列名，放入数组中
				int colCount = rsmd.getColumnCount();
				String []colNames = new String[colCount];
				for(int i=0;i<colCount;i++){
					colNames[i] = rsmd.getColumnName(i+1);
				}
				
				T t = null;
				Object obj = null;
				while(rs.next()){//一条记录一条记录地迭代
					t = c.newInstance();//实例化c类对象
					for(String cn:colNames){
						obj = rs.getObject(cn);//取得每条记录中每一列的值
						if(obj!=null){//保证值不为空
							String colType = obj.getClass().getName();//取得类型名
							//System.out.print(colType+" ");
							for(Method m:methods){
								if(("set"+cn).equalsIgnoreCase(m.getName())){//如果是对应的set方法
									if("java.math.BigDecimal".equals(colType)){
										m.invoke(t, rs.getInt(cn));
									}else if("java.lang.String".equals(colType)){
										m.invoke(t, rs.getString(cn));
									}else if("java.sql.Date".equals(colType)){
										m.invoke(t, rs.getDate(cn));
									}else if("image".equals(colType)){
										m.invoke(t, rs.getBlob(cn));
									}else{
										m.invoke(t, rs.getString(cn));
									}
								}
							}
						}
					}
					res.add(t);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return res;
		}
}
