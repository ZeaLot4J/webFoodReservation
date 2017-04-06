package com.zl.biz.impl;

import java.lang.reflect.InvocationTargetException; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zl.bean.Resfood;
import com.zl.dao.DBHelper;
import com.zl.web.utils.RedisUtil;
import com.zl.web.utils.ZLConstants;
import com.zl.biz.IResfoodBiz;

import redis.clients.jedis.Jedis;

public class ResfoodBizImpl implements IResfoodBiz{

	private DBHelper db=new DBHelper();
	private Jedis jedis=new Jedis(ZLConstants.REDIS_URL, ZLConstants.REDIS_PORT);
	
	RedisUtil ru=new RedisUtil();
	@Override
	public List<Resfood> findGoods(Map<String, String> params, Integer pageFirst, Integer rows) throws Exception{
		List<Resfood> list=null;
		String sql="select * from resfood where fname like "+"'"+params.get("fnamelike")+"'"+" limit "+pageFirst+","+rows;
		
		list=db.find(sql, Resfood.class);
		return list;
	}

	//带条件的获取总记录数
//	public int findGoodsCounts(Map<String, String> params) throws Exception{
//		List<Resfood> list=null;
//		String sql="select count(*) from resfood where fname like "+"'"+params.get("fnamelike")+"'";
//		
//		list=db.find(sql, params);
//		int count=list.size();
//		System.out.println(list);
//		System.out.println(count);
//		return count;
//	}
	
	
	@Override
	public int updategoods(String fname, Double normprice, Double realprice, String detail, String fphoto, String fid) {
		String sql="";
		int result=0;
		if( fphoto==null || fphoto.equals("")){
			sql="update resfood set fname=?,normprice=?,realprice=?,detail=? where fid=?";
			result=db.update(sql, fname,normprice,realprice,detail,fid);
			if(result==1){
				updateRedis();
			}
		}else{
			sql="update resfood set fname=?,normprice=?,realprice=?,detail=?,fphoto=? where fid=?";
			result=db.update(sql, fname,normprice,realprice,detail,fphoto,fid);
			if(result==1){
				updateRedis();
			}
		}
		return result;
	}
	
	//删除商品
	@Override
	public int delgoods(String fid) {
		String sql=null;
		int result=0;
		if(fid.contains(",") && !fid.contains(" or ")){ //删除多个
			sql="delete from resfood where fid in("+fid+")";
			result=db.update(sql);
			System.out.println(fid);
			if(result>0){
				String a[]=fid.split(",");
				for(int i=0;i<a.length;i++){
					System.out.println(a[i]);
					jedis.del("allfood:"+a[i]);
				}
			}
		}else{
			sql="delete from resfood where fid=?";
			result=db.update(sql, fid);
			if(result==1){
				jedis.del("allfood:"+fid);
			}
		}
		return result;
	}
	
	//添加商品
	public int addgoods(String fname, Double normprice, Double realprice, String detail, String fphoto) throws Exception{
		if(fphoto == ""){
			fphoto = "../../pics/zanwu.jpg";
		}
		String	sql="insert into resfood(fname,normprice,realprice,detail,fphoto,up,down) values(?,?,?,?,?,0,0)";
		
		int result=db.update(sql, fname,normprice,realprice,detail,fphoto);
		if(result==1){
			updateRedis();
		}
		return result;
	}
	
	
	//oracle分页查询sql语句在mysql有问题 
	//TODO:
	public List<Resfood> findByPage(int start ,int pageSize) throws Exception {
		List<Resfood> list=null;
		String sql="select * from resfood limit "+start+","+pageSize;
		list=db.find(sql, Resfood.class);
		return list;
	}
	
	//获取总数
	public int totalCount() {
		Connection con=db.getConn(); //获取连接
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select count(*) as total from resfood";
		int count=0;
		try{
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()){
				count=rs.getInt("total");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.closeAll(con, pstmt, rs);;
		}
		return count;
	}
	
	@Override
	public Resfood getResfoodByFid(Integer fid) throws Exception {
		Resfood resfood=null;
		List<Resfood> list=null;
		try {
			jedis.connect();
			if(	jedis.isConnected()==true && jedis.keys( ZLConstants.ALLFOOD+":"+fid).size()>0){
				list=ru.getFromHash(jedis,ZLConstants.ALLFOOD+":"+fid, Resfood.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( list==null){
			List<Object> params=new ArrayList<Object>();
			params.add(fid);
			list=db.find("select * from resfood where fid=?", Resfood.class,params);
		}
		
		if(list!=null && list.size()>0){
			resfood=list.get(0);
		}
		return resfood;
	}
	
	public List<Resfood> findAll() throws Exception {
		//1.判断jedis中是否有数据 如果有，则用redis中的数据
		//2.  没用则从数据查一次
		List<Resfood> list=null;
		try {
			jedis.connect();
			if(	jedis.isConnected()==true && jedis.keys( ZLConstants.ALLFOOD+"*").size()>0){
				list=ru.getFromHash(jedis, ZLConstants.ALLFOOD+":*", Resfood.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( list==null){
			list=db.find("select * from resfood", Resfood.class);
			ru.saveToHash(jedis, ZLConstants.ALLFOOD, "fid", list, Resfood.class);
		}
		return list;
	}
	
	
	public void updateRedis(){
		try {
			List<Resfood> list=null;
			String sqlall="select * from resfood";
			list=db.find(sqlall, Resfood.class);
			System.out.println(list);
			ru.saveToHash(jedis, ZLConstants.ALLFOOD, "fid", list, Resfood.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) throws Exception {
		ResfoodBizImpl rbi=new ResfoodBizImpl();
//		List<Resfood> list=rbi.findAll();
//		
//		for( Resfood f:list){
//			System.out.println( f);
//		}
		
//		int count=rbi.totalCount();
//		System.out.println(count);
		
		List<Resfood> list=rbi.findByPage(0, 4);
		System.out.println(list);
//		Resfood rf=rbi.getResfoodByFid(2);
//		System.out.println(rf);
		
	//	Jedis jedis1=new Jedis();
	//	System.out.println(jedis1.isConnected());
	}












	
}
