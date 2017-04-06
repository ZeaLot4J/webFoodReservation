//每个域下最多存储20个cookie，如果更多早期的会丢失
// 每个cookie最多4kb数据
// 存储时都要进行编码
//cookie只能保存文本数据，不能存二进制数据
Cookies={
	set:function(key,value,minsToExpire){
		var expires="";
		if(minsToExpire){
			var date=new Date();//获取客户端的时间
			date.setTime(date.getTime()+(minsToExpire*60*1000));
			expires=";expires="+date.toGMTString();//转换为GMT的时间格式
		}
		//所有存入cookie的数据都要进行encodeURIComponent编码
		document.cookie=key+"="+value+expires+";path=/";//  /表示当前网站的根目录
		return value;
	},
	getall:function(){
		return document.cookie.split(";");
	},
	get:function(key){
		var nameCompare=key+"=";
		var cookieArr=document.cookie.split(";");
		for(var i=0;i<cookieArr.length;i++){
			var a=cookieArr[i].split("=");
			var currentKey=decodeURIComponent(a[0]);
			if(key==currentKey || " "+key==currentKey){
				return decodeURIComponent(a[1]);
			}
		}
		return null;
	},
	del:function(key){
		this.set(key,"",-1);
	}
};