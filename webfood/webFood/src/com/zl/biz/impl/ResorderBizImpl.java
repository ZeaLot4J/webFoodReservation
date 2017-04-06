package com.zl.biz.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.zl.bean.Resfood;
import com.zl.bean.Resorder;
import com.zl.biz.IResorderBiz;
import com.zl.dao.DBHelper;

public class ResorderBizImpl implements IResorderBiz{
	private DBHelper dao = new DBHelper();
	@Override
	public void submitOrder(Resorder order, Map<Integer, Resfood> cart) throws Exception {
		
		Connection con = dao.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			con.setAutoCommit(false);//关闭隐式自动提交，为了整块手动提交，利于出错回滚
		//1.插入resorder表
			sql = "insert into resorder(userid,address,tel,ordertime,deliverytime,ps,status) values(?,?,?,now(),?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, order.getUserid()+"");
			pstmt.setString(2, order.getAddress()+"");
			pstmt.setString(3, order.getTel()+"");
			pstmt.setString(4, order.getFormatDeliverytime()+"");
			pstmt.setString(5, order.getPs()+"");
			pstmt.setString(6, order.getStatus()+"");
			pstmt.executeUpdate();
		//2.查出新插入的订单记录的roid
			sql = "select max(roid) from resorder";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			String roid = null;
			if(rs.next()){
				roid = rs.getString(1);
			}else{
				throw new Exception("database error");//抛到下面去捕获，然后回滚
			}
		//3.把购物车里的东西加入resorderitem
			sql = "insert into resorderitem(roid,fid,dealprice,num) value(?,?,?,?)";
			for(Map.Entry<Integer, Resfood> entry:cart.entrySet()){//从购物车中一个一个地取出
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, roid);
				pstmt.setString(2, String.valueOf(entry.getKey()));
				pstmt.setString(3, String.valueOf(entry.getValue().getRealprice()));
				pstmt.setString(4, String.valueOf(entry.getValue().getNum()));
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}//失败则回滚
			throw e;
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null){
				try {
					con.setAutoCommit(true);//还原
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
