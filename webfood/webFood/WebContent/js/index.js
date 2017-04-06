var haslogined=false;//用来判断用户是否登录
var buyfoodidarr = [];
var allfoodsarr;
var pageNum = 4;//指定页显示的记录数
var currPage;//保存当前页码
var pageCount;//计算出页面数
$(function(){
	$.ajax({
		url: "index.action",
		type:"POST",
		dataType:"JSON",
		success: function(data){
			if(data.code==1){
				allfoodsarr = data.obj[0];
				pageCount = Math.ceil(allfoodsarr.length/pageNum);
				showAll(1);//显示第一页的
				showPageIndex()//显示页码
				showbag(data.obj[1]);
				if(data.obj[2]!=null){
					haslogined = true;
				}else{
					haslogined = false;
				}
				checklogin(data.obj[2]);
				if(haslogined){
					$.ajax({
						url:"history.action",
						type:"POST",
						data:"op=getHistory",
						dataType:"JSON",
						success: function(data){
							if(data.code==1){
								var objs = data.obj;
								refreshlook2(objs);
							}
						}
					});
				}else{	
					refreshlook();
				}
			}
		}
	});
//	$.ajax({ 
//		url: "http://localhost:8080/webFood/resfood.action",
//		data:'op=findAll',
//		type: "POST",
//		dataType: "JSON",
//		success: function(data){
//			if(data.code == 0){
//				alert("服务器错误"+data.msg);
//			}else{
//				allfoodsarr = data.obj;
//				pageCount = Math.ceil(allfoodsarr.length/pageNum);
//				showAll(1);//显示第一页的
//				showPageIndex()//显示页码
//			}
//		}
//	});
	
//	$.ajax({ 
//		url: "resorder.action?op=showorder",
//		type: "GET",
//		dataType: "JSON",
//		success: function(data){
//			if(data.code == 0){
//				alert("服务器错误"+data.msg);
//			}else{
//				showbag(data.obj);
//			}
//		}
//	});
	//保持登录状态
//	$.ajax({
//		url:"resuser.action?op=islogin",
//		type:"GET",
//		dataType:"JSON",
//		success:function(data){
//			if(data.code==1){
//				haslogined = true;
//			}else{
//				haslogined = false;
//			}
//			checklogin(data.obj);
//		}
//	});
	
	$("#unshow").click(function(){
		unshow();
	});
	$("#unshowsignin").click(function(){
		unshowsignin();
	});
	
	$("#unshowinfo").click(function(){
		unshowinfo();
	});
	
	$("#delall").click(function(){
		$.ajax({
			url:"resorder.action?op=delall",
			type:"GET",
			dataType:"JSON",
			success:function(data){
				if(data.code==1){
					showbag(data.obj);
				}else{
					alert("修改失败");
				}
			}
		});
	});
	
	
	$("#btn").click(function(){
		$.ajax({
			url:"resuser.action",
			data:"op="+$('#op').val()+"&username="+$('#username').val()+"&pwd="+$('#pwd').val()+"&valcode="+$('#yzm').val(),
			type:"POST",
			dataType:"JSON",
			success:function(data){
				if(data.code==1){
					alert("登录成功");
					haslogined = true;
					unshow();
					checklogin(data.obj);
					$.ajax({
						url:"history.action",
						type:"POST",
						data:"op=getHistory",
						dataType:"JSON",
						success: function(data){
							if(data.code==1){
								var objs = data.obj;
								refreshlook2(objs);
							}
						}
					});
				}else{
					alert("登录失败."+data.errorMsg);
					haslogined = false;
				}
			}
			
		});
	});
	$("#signinbtn").click(function(){
		var name = $('#signinname').val();
		var pwd = $('#signinpwd').val();
		var confirmPwd = $('#confirmpwd').val();
		var email = $('#signinemail').val();
		var regExp = /^\w+@\w+.com$/;
		if(name==""||pwd==""||confirmPwd==""||email==""){
			alert("请把信息填写完整");
			return;
		}else if(pwd!=confirmPwd){
			alert("两次输入的密码不一致");
			return;
		}else if(!regExp.test(email)){
			alert("邮箱格式错误");
			return;
		}
		
		$.ajax({
			url:"resuser.action",
			data:"op="+$('#opsignin').val()+"&username="+name+"&pwd="+pwd+"&email="+email,
			type:"POST",
			dataType:"JSON",
			success:function(data){
				if(data.code==1){
					alert("注册成功");
					unshowsignin();
				}else{
					alert("注册失败."+data.errorMsg);
				}
			}
		
		});
	});
	
	
	$("#showlogin").click(showLogin);
	
	$("#showsignin").click(showSignin);
	
	$("#submit").click(function(){
		$.ajax({
			url:"resorder.action",
			data:"op=confirmorder"+"&address="+$("#address").val()+"&tel="+$("#tel").val()+"&deliverytime="+$("#deliverytime").val()+"&ps="+$("#ps").val(),
			type:"POST",
			dataType:"JSON",
			success:function(data){
				if(data.code==1){
					alert("购买成功");
					unshowinfo();
				}else{
					alert("购买失败"+data.errorMsg);
				}
			}
		});
	});
	$("#yzm_img").click(function(){
		$(this).attr("src","verifyCode.action?"+Math.random()*100);
	});
	
	$("#showlook").click(showlookdiv);
});


function showAll(pageIndex){//指定第几页
	currPage = pageIndex;
	$("#allfoods").empty();
	for(var i=(pageIndex-1)*pageNum;i<pageIndex*pageNum&&i<allfoodsarr.length;i++){
		var onefood=allfoodsarr[i];
		var li=document.createElement("li");
		//插入菜名
		var title=document.createElement("h3");
		title.innerHTML=onefood.fname;
		title.id="fid"+onefood.fid;
		li.appendChild(title);

		//插入菜单详情的div
		var fooddesc=document.createElement("div");
		yc.addClassName(fooddesc,"fooddesc");
		fooddesc.id="fooddesc"+onefood.fid;
		var foodimg=document.createElement("img");
		foodimg.src=onefood.fphoto;
		yc.addClassName(foodimg,"foodimg");
		fooddesc.appendChild(foodimg);

		(function(index,id){
			yc.addEvent(title,"click",function(){
				showdescs(id);
				var objs = null;
				if(haslogined==false){//如果没登录就存到session中
					if(Cookies.get(allfoodsarr[index].fid)){
						Cookies.del(allfoodsarr[index].fid);
						Cookies.set(allfoodsarr[index].fid,encodeURI(allfoodsarr[index].fname),30*60*24);
					}else{
						Cookies.set(allfoodsarr[index].fid,encodeURI(allfoodsarr[index].fname),30*60*24);
					}
					refreshlook();
				}
				else{//如果登录了就存到redis中
					$.ajax({
						url:"history.action",
						type:"POST",
						data:"op=saveHistory&data="+allfoodsarr[index].fid+"="+encodeURI(allfoodsarr[index].fname),
						dataType:"JSON",
						success: function(data){
							if(data.code==1){
								var objs = data.obj;
								refreshlook2(objs);
							}
						}
					});
				}
			});
		})(i,onefood.fid);
		
		var art=document.createElement("article");
		fooddesc.appendChild(art);
		yc.addClassName(art,"foodprice");

		var detail=document.createElement("p");
		if(onefood.detail){
			detail.innerHTML="菜品描述："+onefood.detail;
		}else{
			detail.innerHTML="菜品描述：无";
		}
		art.appendChild(detail);

		var normprice=document.createElement("p");
		yc.addClassName(normprice,'normprice');
		normprice.innerHTML="原价：￥"+onefood.normprice;
		art.appendChild(normprice);

		var realprice=document.createElement("p");
		yc.addClassName(realprice,'realprice');
		realprice.innerHTML="特价：￥"+onefood.realprice;
		art.appendChild(realprice);

		var buybtn=document.createElement("a");
		buybtn.innerHTML="加入购物车";
		yc.addClassName(buybtn,"buybtn");
		art.appendChild(buybtn);
		
		fooddesc.style.display="none";
		li.appendChild(fooddesc);

		
		document.getElementById("allfoods").appendChild(li);
		$("article").last().append("<input type='button' onclick='up("+onefood.fid+")' style='border-radius:5px;color:blue' id=upbtn"+onefood.fid+" value='好评' />"+"<label id=upnum_"+onefood.fid+">"+onefood.up+"</label>");
		$("article").last().append("<input type='button' onclick='down("+onefood.fid+")' style='border-radius:5px;color:green' id=downbtn"+onefood.fid+" value='差评' />"+"<label id=downnum_"+onefood.fid+">"+onefood.down+"</label>");
		(function(index){
			$(buybtn).click(function(){
				var url="resorder.action?num=1&op=order&fid="+index;
				$.ajax({ 
					url: url,
					type:"GET",
					dataType: "JSON",
					success: function(data){
						if(data.code == 1){
							alert("下订成功");
							showbag(data.obj);
						}else{
							alert("下订失败");
						}
					}
				});
			});
		})(onefood.fid);
		
	}
	//更新上一页，下一页
	$("#prev").attr("href","javascript:showAll("+((currPage-1>0)?(currPage-1):1)+")");
	$("#next").attr("href","javascript:showAll("+((currPage+1<=pageCount)?(currPage+1):pageCount)+")");
}
function up(fid){
	if(haslogined==true){
		$.ajax({
			url:"upanddown.action",
			type:"POST",
			dataType:"JSON",
			data:"op=up&fid="+fid,
			success: function(data){
				if(data.code==1){
					$("#upnum_"+fid).html(parseInt($("#upnum_"+fid).html())+1);
				}
				alert(data.errorMsg);
			}
		});
	}else{
		alert("请先登录");
		showLogin();
	}
}
function down(fid){
	if(haslogined==true){
		$.ajax({
			url:"upanddown.action",
			type:"POST",
			dataType:"JSON",
			data:"op=down&fid="+fid,
			success: function(data){
				if(data.code==1){
					$("#downnum_"+fid).html(parseInt($("#downnum_"+fid).html())+1);
				}
				alert(data.errorMsg);
			}
		});
	}else{
		alert("请先登录");
		showLogin();
	}
}
function showPageIndex(){
	$("#content").append("<a href='javascript:showAll("+((currPage-1>0)?(currPage-1):1)+")' id='prev' class='pageIndex'>上一页</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	for(var i=0; i<pageCount; i++){
		$("#content").append("<a href='javascript:showAll("+(i+1)+")' class='pageIndex'>"+(i+1)+"</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}
	$("#content").append("<a href='javascript:showAll("+((currPage+1<=pageCount)?(currPage+1):pageCount)+")' id='next' class='pageIndex'>下一页</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	$(".pageIndex").css({"font-size":"40px","text-decoration":"none"});
}

function showbag(cart){
	var count = 0;
	for(var key in cart){
		if(cart.hasOwnProperty(key)){
			count++;
		}
	}

	var bag = $("#bagcontainer");

	if(count<=0){
		removebuy();
		calprice(cart);
		bag.html("<p>购物车是空的，赶紧选购吧</p>");
		$(".carbag").css({"bottom":-300-5+"px"});
		bag.css({"height":"260px"});
		return;
	}
	calprice(cart);
	addbuy();
	bag.html("");
	buyfoodidarr = [];
	var theight = 0;
	var str = "";
	for(var key in cart){
		if(cart.hasOwnProperty(key)){
			var buyfood=cart[key];
			buyfoodidarr.push(key);

			str += "<tr>";

			str += "<td width='140px'>"+buyfood.fname+"</td>";
			str += "<td width='130px' class='editfoodnum'>";
			str += "<span>"+buyfood.num+"</span>";
			str += "<b class='subfoodx' onclick='removefood("+key+")'>X</b>"
			str += "<input type='button' value='+' onclick='editfood(1,"+key+")'/>";
			str += "<input type='button' value='-' onclick='editfood(-1,"+key+")'/>";
			str += "</td>";

			str += "<td width='80px' style='color:#69C30'>";
			str += "￥"+(buyfood.num*buyfood.realprice);
			str += "</td>";

			str += "</tr>"
				theight++;
		}
	}
	bag.html(str);
	$(".carbag").css("bottom","50px");
	bag.height(theight*40+"px");
}
//+ -
function editfood(num,id){
	$.ajax({
		url:"resorder.action?op=order&num="+num+"&fid="+id, 
		type:"GET",
		dataType:"JSON",
		success:function(data){
			if(data.code==1){
				showbag(data.obj);
			}else{
				alert("修改失败");
			}
		}
	});
}
//去除一条订单记录
function removefood(id){
	$.ajax({
		url:"resorder.action?op=delorder&fid="+id, 
		type:"GET",
		dataType:"JSON",
		success:function(data){
			if(data.code==1){
				showbag(data.obj);
			}else{
				alert("修改失败");
			}
		}
	});
}


function addbuy(){//结算
	$("#foodcount").html("去结算&gt;").addClass("gotobuy").click(tobuy);
}
function tobuy(){
	if(haslogined){
		showinfo();
	}else{
		showLogin();
	}
}

function showLogin() {
	//后面加个随机数，不然浏览器会用相同的资源
	$("#yzm_img").attr("src","verifyCode.action?"+Math.random()*100);
	$("#login").show();
	$("#mubu").show();
}
function showSignin() {
	$("#signin").show();
	$("#mubu").show();
}

function unshow() {
	$('#login').hide();
	$('#mubu').hide();
}
function unshowsignin() {
	$('#signin').hide();
	$('#mubu').hide();
}
function showinfo() {
	$("#myinfo").show();
	$("#mubu").show();
}
function unshowinfo() {
	$('#myinfo').hide();
	$('#mubu').hide();
}
function calprice(cart){
	var price = 0;
	for(var property in cart){
		if(cart.hasOwnProperty(property)){
			var food = cart[property];
			price += food.realprice * food.num;
		}
	}
}

function removebuy(){
	$("#foodcount").innerHTML = "购物车是空的";
	$("#foodcount").removeClass("gotobuy");
	$("#foodcount").off("click");
}
//购物车
//yc.addEvent("car","click",showcarbag);
//function showcarbag(){
//if(yc.hasClassName(carbag,"showcarbag")){
//yc.removeClassName("carbag","showcarbag");
//}else{
//yc.addClassName("carbag","showcarbag");
//showbag();
//}
//}



function showdescs(index){//显示菜品的详情
	var allfoods=yc.$("allfoods");
	var titles=allfoods.getElementsByTagName("h3");
	var title=yc.$("fid"+index);
	var descs=allfoods.getElementsByTagName("div");
	var desc=yc.$("fooddesc"+index);
	for(var j=0;j<descs.length;j++){
		if(descs[j]==desc) continue;
		descs[j].style.display="none";

		if(index!=allfoodsarr[allfoodsarr.length-1].fid){
			yc.removeClassName("fid"+allfoodsarr[allfoodsarr.length-1].fid,"noradius");
		}
	}
	yc.toggleDisplay("fooddesc"+index,"block");
	if(index==allfoodsarr[allfoodsarr.length-1].fid){
		if(yc.hasClassName(title,"noradius")){
			yc.removeClassName(title,"noradius");
		}else{
			yc.addClassName(title,"noradius");
		}
	}
}
function checklogin(obj){
	if(haslogined==true){
		haslogin(obj);
	}else{
		$("#showlogin").show();
		$("#exitspan").hide()
	}
	
}
function haslogin(obj){
	$("#showlogin").hide();
	$("#showsignin").hide();
	$("#exitspan").show().html("欢迎您"+obj.username+",<a href='javascript:clickexit()'>退出</a>");
}
function clickexit(obj){//用户退出
	$.ajax({
		url:"resuser.action?op=logout",
		type:"GET",
		data:"JSON",
		success:function(data){
			if(data.code==1){
				$("#showlogin").show();
				$("#showsignin").show();
				$("#exitspan").hide();
				haslogined = false;
				refreshlook();
			}
		}
	});
}

function refreshlook2(objs){//显示用户浏览记录
	yc.$("ull").innerHTML="";
	var flag=0;
	for (var i = objs.length-1; i >=0 ; i--) {
		var lii=document.createElement("li");
		yc.$('ull').appendChild(lii);
		var matcharr=objs[i].split("=");
		lii.innerHTML=decodeURI(matcharr[1]);
		flag++;
		(function(index){
			yc.addEvent(lii,"click",function(){
				//TODO:要找到页码
				showdescs(parseInt(index));
			});
		})(matcharr[0]);
		
		if(flag>=9){
			break;
		}
	}
}
function refreshlook(){//显示用户浏览记录
	var cookiearr=Cookies.getall();
	yc.$("ull").innerHTML="";
	var flag=0;
	if(!document.cookie) return ;
	for (var i = cookiearr.length-1; i >=0 ; i--) {
		var lii=document.createElement("li");
		yc.$('ull').appendChild(lii);
		var matcharr=cookiearr[i].split("=");
		lii.innerHTML=decodeURI(matcharr[1]);
		flag++;
		(function(index){
			yc.addEvent(lii,"click",function(){
				//TODO:要找到页码
				showdescs(parseInt(index));
			});
		})(matcharr[0]);
		
		if(flag>=9){
			break;
		}
	}
}

function showlookdiv(){
	if(yc.hasClassName("look","lookblock2")){
		yc.addClassName("look","lookblock3");
		yc.addClassName("showlook","showlookblock3");
		var removestr='yc.removeClassName("look","lookblock1");yc.removeClassName("showlook","showlookblock1");';
		removestr+='yc.removeClassName("look","lookblock2");yc.removeClassName("showlook","showlookblock2");'
		removestr+='yc.removeClassName("look","lookblock3");yc.removeClassName("showlook","showlookblock3");'
		setTimeout(removestr,300);
	}else{
		yc.addClassName("look","lookblock1");
		yc.addClassName("showlook","showlookblock1");
		setTimeout('yc.addClassName("look","lookblock2");yc.addClassName("showlook","showlookblock2");',300);
		// yc.addClassName("look","lookblock2");
		// yc.addClassName("showlook","showlookblock2");
	}
}

//yc.addEvent("delall","click",function(){
//for(var i=0;i<buyfoodidarr.length;i++){
//removefood(buyfoodidarr[i]);
//}
//});
