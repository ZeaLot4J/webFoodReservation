var haslogined=false;//用来判断用户是否登录
yc.addLoadEvent(checklogin);//检测用户登录
yc.addLoadEvent(showbag);//刷新购物车
yc.addLoadEvent(refreshlook);//刷新浏览记录

var options={
	"completeListener":function(){
		allfoodsarr=this.responseJSON.obj;
		var allfoods=yc.$("allfoods");
		for(var i=0;i<allfoodsarr.length;i++){
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
			foodimg.src="http://218.196.14.220:8080/res/images/"+onefood.fphoto;
			yc.addClassName(foodimg,"foodimg");
			fooddesc.appendChild(foodimg);

			(function(index,id){
				yc.addEvent(title,"click",function(){
					showdescs(id);
					if(Cookies.get(allfoodsarr[index].fname)){
						Cookies.del(allfoodsarr[index].fname);
						Cookies.set(allfoodsarr[index].fname,allfoodsarr[index].fid,60*24);
					}else{
						Cookies.set(allfoodsarr[index].fname,allfoodsarr[index].fid,60*24);
					}
					// var url="http://218.196.14.220:8080/res/resfood.action?op=findFood&fid="+id;
					// var sendfooddesc={
					// 	'completeListener':function(){
							
					// 	}
					// };
					// yc.xssRequest(url,sendfooddesc); 
					
					refreshlook();
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
			(function(index){
				yc.addEvent(buybtn,"click",function(){
					var url="http://218.196.14.220:8080/res/resorder.action?num=1&op=order&fid="+index;
					var options={
						'completeListener':function(){
							if(this.responseJSON.code==0){
								alert(this.responseJSON.msg);
							}
						}
					};
					yc.xssRequest(url,options); 
					showbag();
				});
			})(onefood.fid);

			fooddesc.style.display="none";
			li.appendChild(fooddesc);


			allfoods.appendChild(li);
		}
	}
};
//初始化页面，把所有的菜品插入到界面并绑定事件
yc.xssRequest("http://218.196.14.220:8080/res/resfood.action?op=findAllFoods",options);

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

function checklogin(){//检测用户登录
	var options={
		'completeListener':function(){
			//console.log(this.responseJSON);
			if(this.responseJSON.code==1){
				haslogined=true;
				haslogin(this.responseJSON.obj);
			}
		}
	};
	yc.xssRequest("http://218.196.14.220:8080/res/resuser.action?op=checkLogin",options);
}


yc.addEvent('yzm_img','click',function(){//获取随机验证码
	yc.$('yzm_img').src="http://218.196.14.220:8080/res/verifyCodeServlet?valcode="+Math.random();
});

yc.addEvent('username','blur',function () {//对用户输入的用户名验证
	if(!(/^[0-9a-z_\u4E00-\u9FA5]+/i).test(yc.$('username').value)){
		yc.$('pp').style.borderBottomColor='red';
	}else{
		yc.$('pp').style.borderBottomColor='green';
	}
});

yc.addEvent('pwd','blur',function () {//对用户输入的密码验证
	if(!(/^[0-9a-z_]+/i).test(yc.$('pwd').value)){
		yc.$('pp2').style.borderBottomColor='red';
	}else{
		yc.$('pp2').style.borderBottomColor='green';
	}
});

//弹出登录框
yc.addEvent("showlogin","click",show);
yc.addEvent("unshow","click",unshow)
yc.addEvent("unshowinfo","click",unshowinfo)

function show() {
	yc.$('login').style.display="block";
	yc.$('mubu').style.display="block";
}
function unshow() {
	yc.$('login').style.display="none";
	yc.$('mubu').style.display="none";
}

function showinfo() {
	yc.$('myinfo').style.display="block";
	yc.$('mubu').style.display="block";
}
function unshowinfo() {
	yc.$('myinfo').style.display="none";
	yc.$('mubu').style.display="none";
}

function login(){
	var uri=yc.serialize(document.myform);
	var url="http://218.196.14.220:8080/res/resuser.action?"+uri;
	var options={
		'completeListener':function(){
			var json=this.responseJSON;
			if (json.code==0) {
				alert(json.msg);
				haslogined=false;
			}else if(json.code==1) {
				unshow();
				checklogin();
				haslogined=true;
			}
		}
	};
	yc.xssRequest(url,options);
}
yc.addEvent("btn","click",login);

function haslogin(obj){
	yc.$("showlogin").style.display="none";
	yc.$("exitspan").style.display="block";
	yc.$('exitspan').innerHTML="欢迎您"+obj.username+",";
	var exitbtn=document.createElement("a");
	exitbtn.innerHTML="退出";
	yc.addEvent(exitbtn,"click",clickexit);
	yc.$('exitspan').appendChild(exitbtn);
}

function clickexit(obj){//用户退出
	var options={
		'completeListener':function(){
			if(this.responseJSON.code==1){
				yc.$("showlogin").style.display="block";
				yc.$("exitspan").style.display="none";
				haslogined=false;
			}
		}
	};
	yc.xssRequest("http://218.196.14.220:8080/res/resuser.action?op=logout",options);
}

//购物车
yc.addEvent("car","click",showcarbag);
function showcarbag(){
	if(yc.hasClassName(carbag,"showcarbag")){
		yc.removeClassName("carbag","showcarbag");
	}else{
		yc.addClassName("carbag","showcarbag");
		showbag();
	}
}

var buyfoodidarr=[];
function showbag(){
	var cart={
		"completeListener":function(){
			var obj=this.responseJSON.obj;
			console.log(this.responseJSON);
			var count=0;
			for(var a in obj){
				if(obj.hasOwnProperty(a)){
					count++;
				}
			}
			var bag=yc.$("bagcontainer");
			if(count>0){
				calprice(obj);
				addbuy();
				bag.innerHTML="";
				yc.removeChildren(bag);
				var theight=0;
				buyfoodidarr=[];
				for(var property in obj){
					if(obj.hasOwnProperty(property)){
						var buyfood=obj[property];
						buyfoodidarr.push(buyfood.food.fid);
						var tr=document.createElement("tr");
						bag.appendChild(tr);
						var td1=document.createElement("td");
						var td2=document.createElement("td");
						var td3=document.createElement("td");
						tr.appendChild(td1);
						tr.appendChild(td2);
						tr.appendChild(td3);
						td1.width="140px";
						td1.innerHTML=buyfood.food.fname;

						var x=document.createElement("b");
						x.innerHTML="X";
						yc.addClassName(x,"subfoodx");
						yc.prependChild(tr,x);

						td2.width="130px";
						yc.addClassName(td2,"editfoodnum");
						var numspan=document.createElement("span");
						numspan.innerHTML=buyfood.num;
						td2.appendChild(numspan);
						var addfoodbtn=document.createElement("button");
						addfoodbtn.innerHTML="+";
						td2.appendChild(addfoodbtn);
						var subfoodbtn=document.createElement("button");
						subfoodbtn.innerHTML="-";
						yc.prependChild(td2,subfoodbtn);

						(function(id){
							yc.addEvent(addfoodbtn,"click",function(){editfood(1,id)});
							yc.addEvent(subfoodbtn,"click",function(){editfood(-1,id)});
							yc.addEvent(x,"click",function(){removefood(id)});
						})(buyfood.food.fid);

						td3.width="80px";
						td3.style.color="#F69C30";
						td3.innerHTML="￥"+buyfood.smallCount;
						theight++;
					}
				}
				var newbottom={"bottom":-(theight*40+40+5)+"px"};
				yc.editCSSRule(".carbag",newbottom,"css/index.css");
				bag.style.height=theight*40+"px";
			}else{
				removebuy();
				calprice(obj);
				var empcar=document.createElement("p");
				bag.innerHTML="";
				empcar.innerHTML="购物车是空的，赶紧选购吧";
				bag.appendChild(empcar);
				var newbottom={"bottom":-300-5+"px"};
				yc.editCSSRule(".carbag",newbottom,"css/index.css");
				bag.style.height=260+"px";
			}
		}
	}
	yc.xssRequest("http://218.196.14.220:8080/res/resorder.action?op=getCartInfo",cart);
}
yc.addEvent("delall","click",function(){
	for(var i=0;i<buyfoodidarr.length;i++){
		removefood(buyfoodidarr[i]);
	}
});
function editfood(num,id){
	var options={
		'completeListener':function(){
			//var json=this.responseJSON;
			showbag();
		}
	};
	yc.xssRequest("http://218.196.14.220:8080/res/resorder.action?op=orderJson&num="+num+"&fid="+id,options);
}

function removefood(id){
	var options={
		'completeListener':function(){
			var json=this.responseJSON;
			showbag();
		}
	};
	yc.xssRequest("http://218.196.14.220:8080/res/resorder.action?op=delorder&fid="+id,options);
}


function calprice(obj){//计算价格
	var price=0;
	for(var property in obj){
		if(obj.hasOwnProperty(property)){
			var hasfood=obj[property];
			price+=hasfood.smallCount;
		}
	}
	if(yc.$("pricetext")){
		yc.$("pricetext").innerHTML="￥"+price;
	}else{
		var pricetext=document.createElement("p");
		pricetext.id="pricetext";
		pricetext.innerHTML="￥"+price;
		yc.prependChild("car",pricetext);
	}
	
}

function addbuy(){
	yc.$("foodcount").innerHTML="去结算&gt;";
	yc.addClassName("foodcount","gotobuy");
	yc.addEvent("foodcount","click",tobuy);
}
function removebuy(){
	yc.$("foodcount").innerHTML="购物车是空的";
	yc.removeClassName("foodcount","gotobuy");
	yc.removeEvent("foodcount","click",tobuy);
}

function tobuy(){
	if(haslogined){
		showinfo();
	}else{
		show();
	}
}


function submit(){

	var uri=yc.serialize(forminfo);
	var options={
		"completeListener":function(){
			if(this.responseJSON.code==1){
				unshowinfo();
				alert("购买成功");
			}else{
				alert("购买失败");
			}
		}
	}
	yc.xssRequest("http://218.196.14.220:8080/res/cust/custOp.action?"+uri,options);
}
yc.addEvent("submit","click",submit);

function refreshlook(){//显示用户浏览记录
	// var options={
	// 	'completeListener':function(){
	// 		var json=this.responseJSON;
	// 		var flag=0;
	// 		if (json.code==1) {
	// 			yc.$("ull").innerHTML="";
	// 			for (var i = 0; i < json.obj.length; i++) {
	// 				var lii=document.createElement("li");
	// 				yc.$('ull').appendChild(lii);
	// 				lii.innerHTML=json.obj[i].fname;
	// 				flag++;
	// 				(function(id){
	// 					yc.addEvent(lii,"click",function(){
	// 						showdescs(id);
	// 						window.scrollTo(0,50*id);
	// 					});
	// 				})(json.obj[i].fid);
					
	// 				if(flag>=9){
	// 					break;
	// 				}
	// 			}
	// 		}
	// 	}
	// };
	//yc.xssRequest("http://218.196.14.220:8080/res/resfood.action?op=findAllSelectedFoods",options);
	
	var cookiearr=Cookies.getall();
	yc.$("ull").innerHTML="";
	var flag=0;
	if(!document.cookie) return ;
	for (var i = cookiearr.length-1; i >=0 ; i--) {
		var lii=document.createElement("li");
		yc.$('ull').appendChild(lii);
		var matcharr=/([\u0391-\uFFE5]+)=(\d*)/.exec(cookiearr[i]);
		lii.innerHTML=matcharr[1];
		flag++;
		
		(function(index){
			yc.addEvent(lii,"click",function(){
				showdescs(index);
				window.scrollTo(0,50*index);
			});
		})(matcharr[2]);
		
		if(flag>=9){
			break;
		}
	}
}

yc.addEvent("showlook","click",showlookdiv);
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
