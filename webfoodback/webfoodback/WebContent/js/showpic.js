function previewMultipleImage(file,id) {
	var MAXWIDTH = 100;
	var MAXHEIGHT = 100;
	var div = document.getElementById(id);
	div.innerHTML="";
	if($('#textfield').val()=="" ||file.files.length==0){
		$("#phototals").html("");
	}
	if (file.files && file.files[0]) {
		if(file.files.length>100){
			alert("上传图片不得多于100张...");
			$("#phototals").html("");
			$('#textfield').val("");
			$("#smallpic").html("");
			$("#upfile").val("");
			return;
		}
		
		for(var i=0;i<file.files.length;i++){
			var reader = new FileReader();
			reader.onload = function(evt) {
				div.innerHTML+='<img  id="img"'+i+' src='+ evt.target.result
				+' style="float:left; margin:10px 0px 0px 10px;"  width="'+MAXWIDTH+'" height="'+MAXHEIGHT+'"  />';	
			};
			reader.readAsDataURL(file.files[i]);
		}
		$("#div_h4").css("display","none");
		$("#phototals").html("共"+file.files.length+"张图片");
		
		$("#smallpic").css("overflow","scroll");
	} else {
		// 运用IE滤镜获取数据;
		var sFilter = 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
		file.select();// /选定程序
		var src = document.selection.createRange().text;
		div.innerHTML = '<img id=imghead>';
		var img = document.getElementById('imghead');
		img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
		var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth,
				img.offsetHeight);
		//状态值
		status = ('rect:' + rect.top + ',' + rect.left + ',' + rect.width + ',' + rect.height);
		div.innerHTML = "<div id=divhead style='width:" + rect.width
				+ "px;height:" + rect.height + "px;margin-top:" + rect.top
				+ "px;margin-left:" + rect.left + "px;" + sFilter + src
				+ "\"'></div>";
	
	}
}