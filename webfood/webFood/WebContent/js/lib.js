//库用来存放一些内置函数的扩展(String,Array,Object)
//放一些自定义的函数，这些函数为了不与别的库冲突，定义到一个命名空间（对象）中。
(function(){
	//给window顶层对象添加一个属性，相当于一个命名空间
	if(  !window.yc  ){
		window.yc={};
	}
///////////////////////////////////////////////////////////////////////////////////////////
//=======================================浏览器的检测======================================
///////////////////////////////////////////////////////////////////////////////////////////
	//判断当前浏览器是否兼容这个库：浏览器能力检测
	function isCompatible(other){
		if(other===false||!Array.prototype.push||!Object.hasOwnProperty||!document.createElement||!document.getElementsByTagName){
			return false;
		}
		return true;
	}
	window["yc"]["isCompatible"]=isCompatible;

///////////////////////////////////////////////////////////////////////////////////////////
//========================================节点的获取======================================
///////////////////////////////////////////////////////////////////////////////////////////
	//通过id名获取节点,传入多个id名时返回一个数组
	function $(){
		var elements=[];
		for(var i=0;i<arguments.length;i++){
			var element=arguments[i];
			if((typeof element)=="string"){
				element=document.getElementById(element);
			}
			if(arguments.length==1){
				return element;
			}
			elements.push(element);
		}
		return elements;
	}
	window["yc"]["$"]=$;

	//通过类名查找节点,返回一个数组   classname 类名；tag 标签名
	function getElementsByClassName(className,tag,parent){
		parent=parent||document;
		if(!( parent=$(parent) )){return false;}
		var allTags=(tag=="*" && parent.all)?parent.all:parent.getElementsByTagName(tag);
		var matchingElements=[];
		var reg=new RegExp("(^|\\s)"+className+"(\\s|$)");//表示classname是开头或者空格开头，或者是结尾或者空格结尾
		var element;
		for(var i=0;i<allTags.length;i++){
			element=allTags[i];
			if(reg.test(element.className)){
				matchingElements.push(element);//将所有匹配到的节点放入数组
			}
		}
		return matchingElements;
	}
	window["yc"]["getElementsByClassName"]=getElementsByClassName;

//////////////////////////////////////////////////////////////////////////////////////////////
//========================================DOM节点操作的补充======================================
///////////////////////////////////////////////////////////////////////////////////////////

	//插入一个节点到指定节点之后
	function insertAfter(node,referenceNode){
		if( !(node=$(node)) ){return false;}
		if( !(referenceNode=$(referenceNode)) ){return false;}
		var parent=referenceNode.parentNode;
		if(parent.lastChild==referenceNode){
			parent.appendChild(node);
		}else{
			parent.insertBefore(node,referenceNode.nextSibling);
		}
		return node;
	}
	window["yc"]["insertAfter"]=insertAfter;

	//在一个父节点的第一个子节点前面添加一个新的节点
	function prependChild(parent,newChild){
		if( !(parent=$(parent)) ){return false;}
		if( !(newChild=$(newChild)) ){return false;}
		if(parent.firstChild){
			parent.insertBefore(newChild,parent.firstChild);
		}else{
			parent.appendChild(newChild);
		}
		return parent;
	}
	window["yc"]["prependChild"]=prependChild;

	//删除节点下的所有子节点
	function removeChildren(parent){
		if( !(parent=$(parent)) ){return false;}
		while( parent.firstChild ){
			parent.removeChild( parent.firstChild );
		}
		return parent;
	}
	window["yc"]["removeChildren"]=removeChildren;

///////////////////////////////////////////////////////////////////////////////////////////
//======================================事件的操作=======================================
///////////////////////////////////////////////////////////////////////////////////////////
	//事件添加  node 节点；type 事件类型；listener 监听器函数
	function addEvent(node,type,listener){ 
		if(!isCompatible()){return false;}
		if( !( node=$(node) )){return false;}
		if(node.addEventListener){//ff浏览器
			node.addEventListener(type,listener,false);
			return true;
		}else if(node.attachEvent){//兼容低版本ie浏览器
			node['e'+type+listener]=listener;
			node[type+listener]=function(){
				node['e'+type+listener](window.event);
			};
			node.attachEvent('on'+type,node[type+listener]);
			return true;
		}
		return false;
	}
	window["yc"]["addEvent"]=addEvent;

	//事件移除  node 节点；type 事件类型；listener 监听器函数
	function removeEvent(node,type,listener){
		if(  !( node=$(node) ) ){return false;}
		if(node.removeEventListener){//ff chorme
			node.removeEventListener(type,listener,false);
			return true;
		}else if( node.detachEvent ){//兼容低版本ie浏览器
			node.detachEvent( "on"+type,node[type+listener] );
			node[type+listener]=null;
			return true;
		}
		return false;
	}
	window["yc"]["removeEvent"]=removeEvent;

	function addLoadEvent(func){
		var oldOnLoad=window.onload;
		if(typeof window.onload!='function'){
			window.onload=func;
		}else{
			window.onload=function(){
				oldOnLoad();
				func();
			}
		}
	}
	window["yc"]["addLoadEvent"]=addLoadEvent;

///////////////////////////////////////////////////////////////////////////////////////////
//========================================界面的操作======================================
///////////////////////////////////////////////////////////////////////////////////////////

	//添加一个显示与隐藏的开关
	function toggleDisplay(node,value){
		if( !(node=$(node)) ){return false;}
		if(node.style.display!='none'){
			node.style.display='none';
		}else{
			node.style.display=value||'';
		}
		return true;
	}
	window['yc']['toggleDisplay']=toggleDisplay;
	
	//替换模版
	function supplant(str,date){
		// for(var index in date){
		// 	template=template.replace("{"+index+"}",date[index]);
		// }
		// return template;
		return str.replace(/{([a-z]*)}/g,function(a,b){return date[b]})
	}
	window["yc"]["supplant"]=supplant;

	function moveElement(element,final_x,final_y,interval){
		if(!isCompatible()) return false;
		if(!$(element)) return false;
		var ele=$(element);
		if(ele.movement){
			clearTimeout(ele.movement);
		}
		var x=ele.offsetLeft;
		var y=ele.offsetTop;
		if(x==final_x && y==final_y) return true;
		var dist=0;
		if(x<final_x){
			dist=(final_x-x)/10;
			x+=dist;
		}
		if(x>final_x){
			dist=(x-final_x)/10;
			x-=dist;
		}
		if(y<final_y){
			dist=(final_y-y)/10;
			y+=dist;
		}
		if(y>final_y){
			dist=(y-final_y)/10;
			y-=dist;
		}
		ele.style.left=x+"px";
		ele.style.top=y+"px";
		var repeat="yc.moveElement('"+element+"',"+final_x+","+final_y+","+interval+")";

		ele.movement=setTimeout(repeat,interval);
	}
	window["yc"]["moveElement"]=moveElement;

///////////////////////////////////////////////////////////////////////////////////////////
//========================================样式表的操作======================================
///////////////////////////////////////////////////////////////////////////////////////////

	function camelize(s){//将word-word 转换为 wordWord(驼峰命名法)
		return s.replace(/-(\w)/g,function(strMatch,p1){return p1.toUpperCase();})
	}
	window["yc"]["camelize"]=camelize;

	function uncamelize(s,sep){//将wordWord 转换为 word-word
		sep=sep||'-';
		return s.replace(/([a-z])([A-Z])/g,function(math,p1,p2){return p1+sep+p2.toLowerCase();});
	}
	window["yc"]["uncamelize"]=uncamelize;

	function setStyleById(element,styles){//element:节点对象     styles：json格式的样式表
		if(!(element=$(element))){return false;}
		for(var property in styles){
			if(!styles.hasOwnProperty(property)) continue;	//排除不属于这个json对象本身的属性（从原型链继承的）
			if(element.style.setProperty){//w3c
				element.style.setProperty(uncamelize(property,'-'),styles[property],null);
			}else{//ie
				element.style[camelize(property)]=styles[property];
			}
		}
		return true;
	}

	window["yc"]["setStyle"]=setStyleById;
	window["yc"]["setStyleById"]=setStyleById;

	function setStyleByTagName(tag,styles,parent){
		parent=parent||document;
		if(!(parent=$(parent))){return false;}
		var elements=parent.getElementsByTagName(tag);
		for(var e=0;e<elements.length;e++){
			setStyleById(elements[e],styles);
		}
	}
	window["yc"]["setStyleByTagName"]=setStyleByTagName;

	function setStyleByClassName(styles,className,tag,parent){
		var elements=getElementsByClassName(className,tag,parent);
		for(var i=0;i<elements.length;i++){
			setStyleById(elements[i],styles);
		}
		return true;
	}
	window["yc"]["setStyleByClassName"]=setStyleByClassName;

	function getClassNames(element){//element  要获得类名的元素
		if(!(element=$(element))){return false;}
		return element.className.replace(/\s+/,' ').split(' ');
	}
	window["yc"]["getClassNames"]=getClassNames;

	function hasClassName(element,className){  //element 要查找的元素    className 要查找的类名
		if(!(element=$(element))){return false;}
		var classes=getClassNames(element);
		for(var i=0;i<classes.length;i++){
			if(classes[i]===className){
				return true;
			}
		}
		return false;
	}
	window["yc"]["hasClassName"]=hasClassName;

	function addClassName(element,className){
		if(!(element=$(element))){return false;}
		if(!hasClassName(element,className)){
			element.className+=(element.className?' ':'') + className;
			return true;
		}
	}
	window["yc"]["addClassName"]=addClassName;

	function removeClassName(element,className){
		if(!(element=$(element))){return false;}
		var classes=getClassNames(element);
		var length=classes.length;
		for(var i=length-1;i>=0;i--){
			if(classes[i]===className){
				delete(classes[i]);
				classes.splice(i,1);
			}
		}
		element.className=classes.join(' ');
		return (length===classes.length?false:true);
	}
	window["yc"]["removeClassName"]=removeClassName;
	
///////////////////////////////////////////////////////////////////////////////////////////
//==================================更大范围的操作样式表==================================
///////////////////////////////////////////////////////////////////////////////////////////
	function getStyleSheets(url,media){//"screen,print" 

		var sheets=[];
		for(var i=0;i<document.styleSheets.length;i++){
			var href=document.styleSheets[i].href;
			if( !href || (url && href.indexOf(url)==-1)){
				continue;
			}
			if(media){
				var media=media.replace(/\s*,\s*/,',');
				var sheetMedia;
				if(document.styleSheets[i].media.mediaText){//w3c
					sheetMedia=document.styleSheets[i].media.mediaText.replace(/,\s*/,',');
					sheetMedia=sheetMedia.replace(/,\s*$/,'');//apple  safari
				}else{//ie
					sheetMedia=document.styleSheets[i].media.replace(/,\s*/,',');
				}
				if(media!=sheetMedia){
					continue;
				}
			}
			sheets.push(document.styleSheets[i]);
		}
		return sheets;
	}
	window["yc"]["getStyleSheets"]=getStyleSheets;

	function addStyleSheet(url,media){//添加样式表
		media=media||'screen';
		var link=document.createElement("link");
		link.setAttribute("href",url);
		link.setAttribute("media",media);
		link.setAttribute("rel","stylesheet");
		link.setAttribute("type","text/css");
		document.getElementsByTagName("head")[0].appendChild(link);
	}
	window["yc"]["addStyleSheet"]=addStyleSheet;

	function removeStyleSheet(url,media){//移除样式表
		var styles=getStyleSheets(url,media);
		for(var i=0;i<styles.length;i++){
			var node=styles[i].ownerNode||styles[i].owningElement;
			styles[i].disabled=true;//禁用样式表
			node.parentNode.removeChild(node);//移除节点
		}
	}
	window["yc"]["removeStyleSheet"]=removeStyleSheet;

	function addCSSRule(selector,styles,index,url,media){
		var declaration='';
		for(var property in styles){
			if(!styles.hasOwnProperty(property)){
				continue;
			}
			declaration+=property+":"+styles[property]+";";
		}
		var styleSheets=getStyleSheets(url,media);
		var newindex;
		for(var i=0;i<styleSheets.length;i++){
			if(styleSheets[i].insertRule){
				newindex=(index>=0?index:styleSheets[i].cssRules.length);
				styleSheets[i].insertRule(selector+"{"+declaration+"}",newindex);
			}else if(styleSheets[i].addRule){
				newindex=(index>=0?index:-1);
				styleSheets[i].addRule(selector,declaration,newindex);
			}
		}
	}
	window["yc"]["addCSSRule"]=addCSSRule;

	function editCSSRule(selector,styles,url,media){
		var styleSheets=getStyleSheets(url,media);
		for(var i=0;i<styleSheets.length;i++){
			var rules=styleSheets[i].rules||styleSheets[i].cssRules;//取出样式表中的规则表
			if(rules){
				selector=selector.toUpperCase();
				for(var j=0;j<rules.length;j++){
					if(rules[j].selectorText.toUpperCase()==selector){
						for(var property in styles){
							if(styles.hasOwnProperty(property)){
								rules[j].style[camelize(property)]=styles[property];
							}
						}
					}
				}
			}
		}
	}
	window["yc"]["editCSSRule"]=editCSSRule;

	function getStyle(element,property){
		if(!(element=$(element))) return false;
		var value=element.style[camelize(property)];
		if(!value){
			if(document.defaultView&&document.defaultView.getComputedStyle){
				//w3c
				var css=document.defaultView.getComputedStyle(element,null);
				value=css?css.getPropertyValue(property):null;
			}else if(element.currentStyle){
				//ie
				value=element.currentStyle[camelize(property)];
			}
		}
		return (value=="auto")?"":value;
	}
	window["yc"]["getStyle"]=getStyle;
	window["yc"]["getStyleById"]=getStyle;

///////////////////////////////////////////////////////////////////////////////////////////
//==========================================xml操作========================================
///////////////////////////////////////////////////////////////////////////////////////////

	function selectXMLNodes(xmlDoc,xpath){
		if("ActiveXObject" in window){   //ie
			xmlDoc.setProperty("SelectionLanguage","XPath");
			return xmlDoc.documentElement.selectNodes(xpath);
		}else{  //ff
			var evaluator=new XPathEvaluator();
			var resultSet=evaluator.evaluate(xpath,xmlDoc,null,XPathResult.ORDERED_NODE_ITERATOR_TYPE,null);
			var finalArray=[];
			if(resultSet){
				var el=resultSet.iterateNext();
				while(el){
					finalArray.push(el);
					el=resultSet.iterateNext();
				}
				return finalArray;
			}
		}
	}
	window["yc"]["selectXMLNodes"]=selectXMLNodes;

	function getElementByIdXML(rootnode,id){
		var nodeTags=rootnode.getElementsByTagName("*");
		for(var i=0;i<nodeTags.length;i++){
			if(   nodeTags[i].hasAttribute("id")  ){
				if(nodeTags[i].getAttribute("id")==id){
					return nodeTags[i];
				}
			}
		}
	}
	window["yc"]["getElementByIdXML"]=getElementByIdXML;

	function parseTextToXmlDomObjet(str){
		var xmlDoc=null;
		if("ActiveXObject" in window){
			var xmlNames=["Msxml2.DOMDocument.6.0","Msxml2.DOMDocument.4.0","Msxml2.DOMDocument.3.0","Msxml2.DOMDocument","Microsoft.XMLDOM","Microsoft.XMLDom"];
			for(var i=0;i<xmlNames.length;i++){
				try{
					var xmlDoc=new ActiveXObject(xmlNames[i]);
					break;
				}catch(e){

				}
			}
			xmlDoc.async=false;
			xmlDoc.loadXML(str);
		}else{
			try{
				var parser=new DOMParser();
				xmlDoc=parser.parseFromString(str,"text/xml");
			}catch(x){
				alert(x.message);
				return ;
			}
		}
		return xmlDoc;
	}
	window["yc"]["parseTextToXmlDomObjet"]=parseTextToXmlDomObjet;

	function parseXmlDomObjectToText(xmlDOM){
		if(xmlDOM.xml){
			return xmlDOM.xml;
		}else{
			var serializer=new XMLSerializer();
			return serializer.serializeToString(xmlDOM,"text/xml");
		}
	}
	window["yc"]["parseXmlDomObjectToText"]=parseXmlDomObjectToText;


///////////////////////////////////////////////////////////////////////////////////////////
//========================================ajax封装======================================
///////////////////////////////////////////////////////////////////////////////////////////
	function addUrlParam(url,name,value){
		url+=(url.indexOf("?")==-1? "?" : "&");
		url+=encodeURIComponent(name)+"="+encodeURIComponent(value);
		return url;
	}
	window["yc"]["addUrlParam"]=addUrlParam;


	function serialize(form){
		var parts=new Array();
		var field=null;
		for(var i=0;i<form.elements.length;i++){
			field=form.elements[i];
			switch(field.type){
				case "select-one":
				case "select-multiple":
					for(var j=0;j<field.options.length;j++){
						var option=field.options[j];
						if(option.selected){
							var optValue="";
							if(option.hasAttribute){
								optValue=(option.hasAttribute("value") ? option.value : option.text);
							}else {
								optValue=(option.attribute["value"].sepcified ? option.value : option.text );
							}
							parts.push(encodeURIComponent(field.name) + "=" + encodeURIComponent(optValue));
						}
					}
					break;
				case undefined:
				case "file":
				case "submit":
				case "reset":
				case "button":
					break;
				case "radio":
				case "checkbox":
					if(!field.checked){
						break;
					}
				default:
					parts.push(encodeURIComponent(field.name) + "=" + encodeURIComponent(field.value));
			}
		}
		return parts.join("&");
	}
	window["yc"]["serialize"]=serialize;


	function getRequestobject(url,options){
		var req=false;
		if(window.XMLHttpRequest){
			var req=new window.XMLHttpRequest();
		}else if(window.ActiveXObject){
			var req=new window.ActiveXObject('Microsoft.XMLHTTP');
		}
		if(!req) return false;
		options=options || {};
		options.method=options.method||"POST";
		options.send=options.send||null;
		req.onreadystatechange=function(){
			switch(req.readyState){
				case 1://请求初始化时
					if(options.loadListener){
						options.loadListener.apply(req,arguments);
					}
					break;
				case 2://加载完成
					if(options.loadedListener){
						options.loadedListener.apply(req,arguments);
					}
					break;
				case 3://交互
					if(options.ineractiveListener){
						options.ineractiveListener.apply(req,arguments);
					}
					break;
				case 4://完成交互时的回调操作
					try{
						if(req.status&&req.status==200){
							var contentType=req.getResponseHeader("content-Type");
							var mineType=contentType.match(/\s*([^;]+)\s*(;|$)/i)[1];
							switch(mineType){
								case "text/javascript":
								case "application/javascript":
									if(options.jsResponseListener){
										options.jsResponseListener.call(req,req.responseText);
									}
									break;
								case "text/plain":
								case "application/json":
									if(options.jsonResponseListener){
										try{
											var json = parseJSON(req.responseText);
										}catch(e){
											var json=false;
										}
										options.jsonResponseListener.call(req,json);
									}
									break;
								case "text/xml":
								case "application/xml":
								case "application/xhtml+xml":
									if(options.xmlResponseListener){
										options.xmlResponseListener.call(req,req.responseXML);
									}
									break;
								case "text/html":
									if(options.htmlResponseListener){
										options.htmlResponseListener.call(req,req.responseText);
									}
									break;
							}
							if(options.completeListener){
								options.completeListener.call(req,req.responseText);
							}
						}else{//响应码不为200
							if(options.errorListener){
								options.errorListener.apply(req,arguments);
							}
						}
					}catch(e){
						alert(e);
					}
					break;

			}
		}
		//打开请求
		req.open(options.method,url,true);
		//在此处可以自己添加请求头：
		//e.g.   req.setRequestHeader("X-yc-Ajax-Request","AjaxRequest");
		return req;
	}
	window["yc"]["getRequestobject"]=getRequestobject;

	//调用该方法时，需要传入一个options的json对象
	/*
	options对象结构：{
		'method':'GET' or 'POST'   不写默认为POST
		'send':要发生的参数
		'loadListener':readyState=1时的回调函数
		'loadedLIstener':readyState=2时的回调函数
		'ineractiveListener':readyState=3时的回调函数

		以下是readyState=4 时的处理回调函数
		'jsResponseListener':当返回结果是js代码时
		'jsonResponseListener':当返回结果是json对象时
		'xmlResponseListener':当返回结果是xml时
		'htmlResponseListener':当返回结果是html时

		'completeListener':处理完成后的回调

		'errorListener':响应码不为200时的回调函数
	}
	*/
	function ajaxRequest(url,options){
		var req=getRequestobject(url,options);
		req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		return req.send(options.send);
	}
	window["yc"]["ajaxRequest"]=ajaxRequest;
	
	
	
	
	
/**
 * 跨站对象计数器
 */
var XssHttpRequestCount=0;

/**
 *request对象的一个跨站点<script>标签生成器
 */
var XssHttpRequest = function(){
    this.requestID = 'XSS_HTTP_REQUEST_' + (++XssHttpRequestCount);   //请求的编号，保证唯一. 
}
//扩展   httpRequest对象。添加了一些属性
XssHttpRequest.prototype = {
    url:null,
    scriptObject:null,
    responseJSON:null,    //  包含响应的结果，这个结果已经是json对象，所以不要 eval了. 
    status:0,        //1表示成功，   2表示错误
    readyState:0,      
    timeout:30000,
    onreadystatechange:function() { },
    
    setReadyState: function(newReadyState) {
        // 如果比当前状态更新，，则更新就绪状态
        if(this.readyState < newReadyState || newReadyState==0) {
            this.readyState = newReadyState;
            this.onreadystatechange();
        }
    },
    
    open: function(url,timeout){
        this.timeout = timeout || 30000;
        // 将一个名字为  XSS_HTTP_REQUEST_CALLBACK的键加到   请求的url地址后面， 值为要回调的函数的名字.这个函数名叫   XSS_HTTP_REQUEST_数字_CALLBACK
        this.url = url + ((url.indexOf('?')!=-1) ? '&' : '?' ) + 'XSS_HTTP_REQUEST_CALLBACK=' + this.requestID + '_CALLBACK';    
        this.setReadyState(0);        
    },
    
    send: function(){
        var requestObject = this;
        //创建一个用于载入外部数据的  script 标签对象
        this.scriptObject = document.createElement('script');
        this.scriptObject.setAttribute('id',this.requestID);
        this.scriptObject.setAttribute('type','text/javascript');
        // 先不设置src属性，也先不将其添加到文档.

        // 异常情况： 创建一个在给定的时间 timeout 毫秒后触发的  setTimeout(), 如果在给定的时间内脚本没有载入完成，则取消载入.
        var timeoutWatcher = setTimeout(function() {
            // 如果脚本晚于我们指定的时间载入， 则将window中的rquestObject对象中的方法设置为空方法
            window[requestObject.requestID + '_CALLBACK'] = function() { };
            // 移除脚本以防止这个脚本的进一步载入。 
            requestObject.scriptObject.parentNode.removeChild(requestObject.scriptObject);
            // 因为以上加载的脚本的操作已经超时，并且 脚本标签已经移除，所以将当前  request对象的状态设置为  2,表示错误, 并设置错误文本 
            requestObject.status = 2;
            requestObject.statusText = 'Timeout after ' + requestObject.timeout + ' milliseconds.'            
            
            // 重新更新  request请求的就绪状态，但请注意，这时，  status 是2 ,而不是200,表示失败了.
            requestObject.setReadyState(2);
            requestObject.setReadyState(3);
            requestObject.setReadyState(4);
                    
        },this.timeout);
        
        
        // 在window对象中创建一个与请求中的回调方法名相同的方法，在回调时负责处理请求的其它部分. 
        window[this.requestID + '_CALLBACK'] = function(JSON) {
            // 当脚本载入时将执行这个方法同时传入预期的JSON对象. 
        
            // 当请求载入成功后，清除timeoutWatcher定时器. 
            clearTimeout(timeoutWatcher);

            //更新状态
            requestObject.setReadyState(2);
            requestObject.setReadyState(3);
            
            // 将状态设置为成功. 
            requestObject.responseJSON = JSON; 
            requestObject.status=1;
            requestObject.statusText = 'Loaded.' 
        
            // 最后更新状态为  4. 
            requestObject.setReadyState(4);
        }

        // 设置初始就绪状态
        this.setReadyState(1);
        
        // 现在再设置src属性并将其添加到文档头部，这样就会访问服务器下载脚本. 
        this.scriptObject.setAttribute('src',this.url);                    
        var head = document.getElementsByTagName('head')[0];
        head.appendChild(this.scriptObject);
        
    }
}
window['yc']['XssHttpRequest'] = XssHttpRequest;

/**
 * 设置Xssrequest对象的各个参数.
 */
function getXssRequestObject(url,options) {
    var req = new  XssHttpRequest();
    options = options || {};
    //默认超时时间
    options.timeout = options.timeout || 30000;
    req.onreadystatechange = function() {
        switch (req.readyState) {
            case 1:
                if(options.loadListener) {
                    options.loadListener.apply(req,arguments);
                }
                break;
            case 2:
                if(options.loadedListener) {
                    options.loadedListener.apply(req,arguments);
                }
                break;
            case 3:
                if(options.ineractiveListener) {
                    options.ineractiveListener.apply(req,arguments);
                }
                break;
            case 4:
                if (req.status == 1) {
                    // The request was successful
                    if(options.completeListener) {
                        options.completeListener.apply(req,arguments);
                    }
                } else {
                    if(options.errorListener) {
                        options.errorListener.apply(req,arguments);
                    }
                }
                break;
        }
    };
    req.open(url,options.timeout);
    return req;
}
window['yc']['getXssRequestObject'] = getXssRequestObject;

/**
 * 发送跨站请求:   JSONP的跨站请求只支持  get方式.
 */
 /*
	options对象结构：{
		timeout: 超时时间
		'loadListener':readyState=1时的回调函数
		'loadedLIstener':readyState=2时的回调函数
		'ineractiveListener':readyState=3时的回调函数

		以下是readyState=4 时的处理回调函数
		'completeListener':处理完成后的回调
		'errorListener':响应码不为200时的回调函数
	}
	*/
function xssRequest(url,options) {
    var req = getXssRequestObject(url,options);
    return req.send(null);
}
window['yc']['xssRequest'] = xssRequest;


///////////////////////////////////////////////////////////////////////////////////////////
//========================================JSON的操作======================================
///////////////////////////////////////////////////////////////////////////////////////////
		//将一个json格式的字符串转换成一个对象，且带过滤功能
		// function parseJSON(str,filter){
		// 	//var obj=JSON.parse(str);
		// 	var obj=eval("("+str+")");
		// 	var objfilter=function(obj){
		// 		for(var i in obj){
		// 			if(obj!=null && typeof obj[i]=="object"){
		// 				objfilter(obj[i]);
		// 			}else if(filter!=null && typeof filter=="function"){
		// 				obj[i]=filter(i,obj[i]);
		// 			}
		// 		}
		// 	}
		// 	objfilter(obj);
		// 	return obj;
		// }

		// function parseJSON(str,filter){
		// 	var result=eval("("+str+")");
		// 	if(filter!=null&& typeof(filter)=="function"){
		// 		for(var i in result){
		// 			//alert(typeof(result[i]));
		// 			if(typeof(result[i])=='object' && typeof result[i]!="function"){
		// 				var jsonstr=JSON.stringify(result[i]);
		// 				result[i]=parseJSON(jsonstr,filter);
		// 			}else{
		// 				result[i]=filter(i,result[i]);
		// 			}
		// 		}
		// 	}
		// 	console.log(result);
		// 	return result;
		// }

		function parseJSON(s,filter){
			var j;
			function walk(k,v){
				var i;
				if(v && typeof v==="object"){
					for(i in v){
						if(v.hasOwnProperty(i)){
							v[i]=walk(i,v[i]);
						}
					}
				}
				return filter(k,v);
			}
			if(/^(["'](\\.|[^"\\\n\r])*?["']|[,:{}\[\]0-9.\-+Eaeflnr-u\n\r\t])+?$/.test(s)){
				try{
					j=eval('('+ s +')');
				}catch(e){
					throw new SyntaxError("eval  parseJSON");
				}
			}else{
				throw new SyntaxError("parseJSON");
			}
			if(typeof filter === "function"){
				j=walk('',j);
			}
			return j;
		}

		window["yc"]["parseJSON"]=parseJSON;



})();

///////////////////////////////////////////////////////////////////////////////////////////
//========================================JSON的操作======================================
///////////////////////////////////////////////////////////////////////////////////////////

//将一个对象转换成json格式的字符串
Object.prototype.toJSONString=function(){
	var jsonarr=[];
	for(var i in this){
		if(this.hasOwnProperty(i)){
			jsonarr.push("\""+i+"\":"+this[i].toJSONString());
		}
	}
	var r=jsonarr.join(",\n");
	r="{"+r+"}";
	return r;
};
Array.prototype.toJSONString=function(){
	var json=[];
	for(var i=0;i<this.length;i++){
		json[i]=(this[i] != null) ? this[i].toJSONString() : "null";
	}
	return "["+json.join(",")+"]";
};
String.prototype.toJSONString=function(){
	return '"'+  this.replace(/(\\|\")/g,"\\$1").replace(/\n|\r|\t/g,function(){
		var a=arguments[0]; 
		return (a=='\n') ? '\\n' : (a=='\r') ? '\\r' : (a=='\t') ? '\\t' : "" })  +'"';
	//return '"'+this+'"';
};
Number.prototype.toJSONString=function(){return this};
Boolean.prototype.toJSONString=function(){return this};
Function.prototype.toJSONString=function(){return this};
RegExp.prototype.toJSONString=function(){return this};



Function.prototype.method=function(name,func){
	if(!this.prototype[name]){
		this.prototype[name]=func;
	}
	return this;
};
