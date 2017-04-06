package com.zl.dao;

import java.io.BufferedInputStream;  
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zl.utils.LogUtil;
/**
 * @ClassName:     DBHelper.java
 * @Description:   
 * @author          POM
 * @version         V1.0  
 * @Date            
 */
public class DBHelper {
	private Connection con=null;
	private PreparedStatement pstmt=null;
	private ResultSet rs=null;

	static{
		try {
			Class.forName(MyProperties.getInstance().getProperty("driverClassName"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DBHelper dao = new DBHelper();
		String sql = "select t_id from tag where t_name = '历史'";
		List<Map<String,Object>> res = dao.findSingle(sql);
		for(Map<String,Object> m:res){
			System.out.println(m);
		}
	}
	public  Connection getConn(){
		Connection con = null;
		try {
			con = DriverManager.getConnection(MyProperties.getInstance().getProperty("url"),MyProperties.getInstance().getProperty("username"),MyProperties.getInstance().getProperty("password"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
//class反射
	/**
	 * 关闭的方法
	 */
	public void closeAll(Connection con,PreparedStatement pstmt,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				LogUtil.log.error(e.toString());
			}
		}
		if(pstmt!=null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				LogUtil.log.error(e.toString());
			}
		}
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				LogUtil.log.error(e.toString());
			}
		}
	}

	/**
	 * 设置PreparedStatement对象的sql语句中的参数？
	 */
	public void setValues(PreparedStatement pstmt,Object ... params){
		if(pstmt!=null&&params!=null&&params.length>0){
			String type=null;
			for(int i=0,len=params.length;i<len;i++){
				Object o=params[i];
				try {
					type=o.getClass().getName();
					if("javax.sql.rowset.serial.SerialBlob".equals(type)){
						pstmt.setBlob(i+1, (Blob)o);
					}else if("java.lang.Integer".equals(type)){
						pstmt.setInt(i+1,Integer.parseInt(String.valueOf(o)));
					}else if("java.lang.Double".equals(type)){
						pstmt.setDouble(i+1, Double.parseDouble(String.valueOf(o)));
					}else{
						pstmt.setString(i+1,String.valueOf(o));
					}
				}catch (SQLException e) {
					e.printStackTrace();
					LogUtil.log.error(e.toString());
				}
			}
		}
	}
	public void setValues(PreparedStatement pstmt,List<Object> params){
		if(pstmt!=null&&params!=null&&params.size()>0){
			Object[] objs = params.toArray();
			setValues(pstmt,objs);
		}
	}
	/**
	 * 增删改
	 * @param sql：sql语句集合，里面可以加？
	 * @param params：表示?对应的参数值的集合
	 * @return int:返回的值。成功>0，失败<=0
	 */
	public int update(List<String> sql,List<List<Object>> params){
		int result=0;
		con=getConn();	
		try {
			con.setAutoCommit(false);  //事务处理
			for(int i=0;i<sql.size();i++){
				List<Object> param=params.get(i);
				pstmt=con.prepareStatement(sql.get(i));  //预编译对象
				setValues(pstmt,param);    //设置参数
				result=pstmt.executeUpdate();
			}
			con.commit(); //没有错处执行
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.log.error(e.toString());
			try {
				con.rollback();  //出错回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeAll(con,pstmt,null);
		}
		return result;
	}
	/**
	 * 单表增删改
	 * @param sql：sql语句集合，里面可以加？
	 * @param params：表示?对应的参数值的集合
	 * @return int:返回的值。成功>0，失败<=0
	 */
	public int update(String sql,Object ... params){
		int result=0;
		con=getConn();
		try {
			pstmt=con.prepareStatement(sql);  //预编译对象
			setValues(pstmt,params);    //设置参数
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			LogUtil.log.error(e.toString());
			try {
				con.rollback();  //出错回滚
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			closeAll(con,pstmt,null);
		}
		return result;
	}
	public int update(String sql,List<Object> params){
		Object[] objs = params.toArray();
		return update(sql,objs);
	}
	/**
	 * 聚合查询
	 * @param sql：聚合查询语句
	 * @param params：参数列表，用来替换sql中的?（占位符）
	 * @return list:结果集
	 */
	public List<String> find(String sql,Object ... params){
		List<String> list=new ArrayList<String>();
		con=getConn();
		try {
			pstmt=con.prepareStatement(sql);  //预编译对象
			setValues(pstmt,params);   //设置参数
			rs=pstmt.executeQuery();  //执行查询

			ResultSetMetaData md=rs.getMetaData();  //结果集的元数据，它反映了结果集的信息
			int count=md.getColumnCount();    //取出结果集中列的数量

			if(rs.next()){
				for(int i=1;i<=count;i++){
					list.add(rs.getString(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.log.error(e.toString());
		}finally{
			closeAll(con,pstmt,rs);
		}
		return list;
	}
	public List<String> find(String sql,List<Object> params){
		Object[] objs = params.toArray();
		return find(sql,objs);
	}
	/**
	 * 查询单个表
	 * @param <T> 泛型：即你要得到的集合中存的对象的类型
	 * @param sql: 查询语句，可以含有?
	 * @param params: ?所对应的参数值的集合
	 * @param c： 泛型类型所对应的反射对象
	 * @return ：存储了对象的集合
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <T> List<T> find(String sql,Class<T> c,Object ... params){
		List<T> list=new ArrayList<T>(); //要返回的结果的集合
		con=getConn();  //获取连接
		try {
			pstmt=con.prepareStatement(sql); //预编译对象
			setValues(pstmt,params); //设置占位符
			rs=pstmt.executeQuery();  //执行查询语句，得到结果集

			Method[] ms=c.getMethods(); //取出这个反射实例的所有方法
			ResultSetMetaData md=rs.getMetaData(); //获取结果集的元数据，它反映了结果集的信息
			String[] colNames=new String[md.getColumnCount()];//创建一个数据colNames，用来存放列的名字
			for(int i=0;i<colNames.length;i++){  //将列名保存到colName数组中
				colNames[i]=md.getColumnName(i+1);
			}
			T t;
			String colType=null; //类型名
			Object obj=null;
				while(rs.next()){//一条记录一条记录地迭代
					t = c.newInstance();//实例化c类对象
					for(String cn:colNames){
						obj = rs.getObject(cn);//取得每条记录中每一列的值
						if(obj!=null){//保证值不为空
							colType = obj.getClass().getName();//取得类型名
							for(Method m:ms){
								if(("set"+cn).equalsIgnoreCase(m.getName())){//如果是对应的set方法
									if("java.lang.Integer".equals(colType)){
										m.invoke(t, rs.getInt(cn));
									}else if("java.math.BigInteger".equals(colType)){
										m.invoke(t, rs.getInt(cn));
									}else if("java.lang.Double".equals(colType)){
										m.invoke(t, rs.getDouble((cn)));
									}else if("java.math.BigDecimal".equals(colType)){
										m.invoke(t, Double.parseDouble(   rs.getBigDecimal(cn).toString()) );
									}else if("image".equals(colType)){
										m.invoke(t, rs.getBlob(cn));
									}else{
										m.invoke(t, rs.getString(cn));
									}
								}
							}
						}
					}
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.log.equals(e.toString());
		}finally{
			closeAll(con, pstmt, rs);
		}
		return list;
	}
	public <T> List<T> find(String sql,Class<T> c,List<Object> params){
		Object[] objs = params.toArray();
		return find(sql,c,objs);
	}
	/**
	 * 查询操作
	 * @param sql：要执行的查询语句
	 * @param params：查询语句中，对用占位符?的值
	 * @return：返回满足条件的所有数据
	 */
	public List<Map<String,Object>> findSingle(String sql,Object ... objs){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try {
			con=this.getConn();
			pstmt=con.prepareStatement(sql);
			this.setValues(pstmt,objs);//给占位符赋值
			rs=pstmt.executeQuery();//获取结果集
			//获取返回结果中的列的列名
			ResultSetMetaData rsmd=rs.getMetaData();
			int colLen=rsmd.getColumnCount(); //获取结果集中列的数量
			String[] colNames=new String[colLen];
			for(int i=0;i<colLen;i++){//取出每个列的列名存放到数组中
				colNames[i]=rsmd.getColumnName(i+1);
			}

			Map<String,Object> map=null;
			String typeName;
			Object obj;
			while(rs.next()){//循环取值，每循环一次就是一条记录，存放到一个map中
				map=new HashMap<String,Object>();
				for(int i=0;i<colLen;i++){ //循环取出每个列的值
					obj=rs.getObject(colNames[i]);
					if(obj!=null){//如果分量不为空
						typeName=obj.getClass().getSimpleName();
						if("Integer".equals(typeName)){
							map.put(colNames[i],rs.getInt(colNames[i]));
						}else if("Double".equals(typeName)){
							map.put(colNames[i], rs.getDouble(colNames[i]));
						}else if("BLOB".equals(typeName)){
							Blob blob=rs.getBlob(colNames[i]);
							byte[] bt=null;
							BufferedInputStream bis=null;
							try {
								bis=new BufferedInputStream( blob.getBinaryStream());
								bt=new byte[(int) blob.length()];
								bis.read(bt);
								map.put(colNames[i],bt);
							} catch (IOException e) {
								e.printStackTrace();
							}  finally{
								if(bis!=null){
									try {
										bis.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}else{
							map.put(colNames[i],rs.getString(colNames[i])); 
						}
					}else{
						map.put(colNames[i],"");
					}
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.log.error(e.toString());
		} finally{
			this.closeAll(con, pstmt, rs);
		}
		return list;
	}
	public List<Map<String,Object>> findSingle(String sql,List<Object> params){
		Object[] objs = params.toArray();
		return findSingle(sql,objs);
	}
}
