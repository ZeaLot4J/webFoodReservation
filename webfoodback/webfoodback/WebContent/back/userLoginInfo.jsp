<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" 
    import="com.zl.web.utils.UserRedis,com.zl.web.utils.ZLConstants,redis.clients.jedis.Jedis,java.util.*,java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网站登录用户数据统计</title>
</head>
<body>
	
	<%
		Jedis jedis=new Jedis(	ZLConstants.REDIS_URL );
		Date d=new Date();
		DateFormat df=new SimpleDateFormat("yyyyMMdd");
		String action="onLineUserPerDay:"+df.format( d );
	%>
	<div class="easyui-accordion" data-options="multiple:true" style="width:500px;height1:300px;">
		<div title="当天在线人数" data-options="" style="overflow:auto;padding:10px;">
			<span id="number"><%=UserRedis.getOnLineUserCount(jedis, action)  %></span>
		</div>
		<div title="vip用户（连续7天上线）总数" style="padding:10px;">
			<span><%=UserRedis.getSeriesOnLineInNDays(jedis, 7) %></span>
		</div>
		<div title="白金用户(7天内曾经上线) 总数" style="padding:10px;">
			<span><%=UserRedis.getOnLineInNDays(jedis, 30) %></span>
		</div>
	</div>
	
</body>
</html>