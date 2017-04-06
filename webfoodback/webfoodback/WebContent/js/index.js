var haslogined = false;


function submitForm(){
	$.ajax({
		url:'resadmin.action',
		type:"POST",
		data:"op=login&"+$("#ff").serialize(),
		dataType:"JSON",
		success: function(data){
			if(data.code==1){
				$.messager.alert('提示','登录成功');
				haslogined = true;
				location.href = "back/index.jsp"
			}else{
				$.messager.alert('提示',data.errorMsg,'warning');
			}
		}
	});
}
function clearForm(){
	$('#ff').form('clear');
}


$(function(){
	$('#index_content_info').tabs('add',{   
	    title:'小萌神信息管理后台',  
	    href:'yc.html', //注意 ：  不是相对于当前的js而是相对于页面  
	    fit:true
	});
	$('#goods_menu').tree({
		onClick: function(node){
			//alert(node.text);  
			var id=node.id; //获取点击节点的id
			var title="";
			var href="";
			var tabObj=$('#index_content_info');
			
			if(id=="manager_goods"){
				if(tabObj.tabs("exists","管理商品")){
					//选中
					tabObj.tabs("select","管理商品");
					return;
				}else{
					title="管理商品";
					href="showgoods.html";
				}
			}else if(id=="online_users"){
				if(tabObj.tabs("exists","用户信息")){
					tabObj.tabs("select","用户信息");
					return;
				}else{
					title = "用户信息";
					href="userInfo.html";
				}
			}else if(id=="order_record"){
				if(tabObj.tabs("exists","订单记录")){
					tabObj.tabs("select","订单记录");
					return;
				}else{
					title="订单记录";
					href="showorder.html";
				}
			}else if(id=="item_record"){
				if(tabObj.tabs("exists","销售情况")){
					tabObj.tabs("select","销售情况");
					return;
				}else{
					title="销售情况";
					href="showitem.html";
				}
			}
			if(href==""){
				return;
			}
			tabObj.tabs('add',{   
			    title:title,  
			    href:href,
			    fit:true,
			    closable:true  
			});  
		}
	});
})

