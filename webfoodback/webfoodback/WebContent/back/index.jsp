<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.zl.web.utils.UserRedis,com.zl.web.utils.ZLConstants,redis.clients.jedis.Jedis,java.util.*,java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>小萌神订餐管理系统后台</title>
	<link rel="stylesheet" type="text/css" href="../css/easyui.css">
	<link rel="stylesheet" type="text/css" href="../css/icon.css">
	<link rel="stylesheet" type="text/css" href="../css/demo.css">
	<script type="text/javascript" src="../js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../js/index.js"></script>
	<script type="text/javascript" src="../js/easyui-lang-zh_CN.js"></script>
	

	<script type="text/javascript" src="../js/showpic.js"></script>
	<script type="text/javascript" src="../js/ajaxfileupload.js"></script>
	
	<script type="text/javascript" charset="utf-8" src="../ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8" src="../ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="../ueditor/lang/zh-cn/zh-cn.js"></script>
</head>
<body class="easyui-layout">
	<%
		Jedis jedis=new Jedis(	ZLConstants.REDIS_URL );
		Date d=new Date();
		DateFormat df=new SimpleDateFormat("yyyyMMdd");
		String action="userPerDay:"+df.format( d );
	%>
	<div data-options="region:'north',border:false" style="height:60px;padding:10px">
	欢迎您,${admin.raname }
	</div>
	<div data-options="region:'west',split:true,title:'管理菜单'" style="width:150px;padding:10px;">
		<ul id="goods_menu" class="easyui-tree">  
		    <li>  
		        <span>信息管理</span>  
		        <ul>  
		            <li>  
		                <span>菜肴信息管理</span>  
		                <ul>  
		                    <li id="manager_goods">上架菜单</li>      
		                </ul>  
		            </li>
		            <li>  
		                <span>用户信息管理</span>  
		                <ul>  
		                    <li id="online_users">用户信息</li>      
		                </ul>  
		            </li>
		            <li>  
		                <span>订单信息管理</span>  
		                <ul>  
		                    <li id="order_record">订单记录</li>      
		                </ul>  
		                <ul>  
		                    <li id="item_record">销售情况</li>      
		                </ul>  
		            </li>   
		            
		        </ul>
		    </li>   
		</ul>  
	</div>
	<div data-options="region:'east',split:true,title:'访问统计'" style="width:100px;padding:10px;">
		
		<div class="easyui-accordion" data-options="multiple:true" style="width:500px;height1:300px;">
			<div title="当天访问人数" data-options="" style="overflow:auto;padding:10px;">
				<span id="number"><%=UserRedis.getOnLineUserCount(jedis, action)  %></span>
			</div>
			<div title="vip用户（连续7天上线）总数" style="padding:10px;">
				<span><%=UserRedis.getSeriesOnLineInNDays(jedis, 7) %></span>
			</div>
			<div title="白金用户(30天内曾经上线) 总数" style="padding:10px;">
				<span><%=UserRedis.getOnLineInNDays(jedis, 30) %></span>
			</div>
		</div>
	</div>
	<div data-options="region:'south',border:false" style="height:50px;padding:10px;text-align:center">小萌神版权所有</div>
	<div data-options="region:'center',title:'操作',tools:[{
		iconCls:'icon-full',
		handler:function(){
			full();
		}
	},{
		iconCls:'icon-unfull',
		handler:function(){
			unFull();
		}
	}]">
		<div id="index_content_info" class="easyui-tabs"  data-options="fit:true">  
		    
		</div>  
	</div>
</body>
</html>