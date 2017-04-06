package com.zl.web.utils;
import java.io.IOException;  
import java.io.InputStream;
import java.util.Properties;
import com.zl.web.utils.LogUtil;
//单例模式  特点:构造函数私有化
public class MyProperties extends Properties{
	private static final long serialVersionUID = 5239841073504532445L;
	private static MyProperties myproperties;
	private static String propertyFileName="db.properties";   //注意:  db.properties文件必须放在src下，这个src后面会被 编译到 bin下，bin就是一个系统的类路径.
	//单例模式
	private MyProperties(){
		//类加载器,是一个类,这个类用于处理类路径下的一些操作
		InputStream iis=MyProperties.class.getClassLoader().getResourceAsStream(propertyFileName);
		try {
			load(iis);
		} catch (IOException e) {
			LogUtil.log.error("error to read properties file", e);
			throw new RuntimeException(e);
		}
	}
	// 确保单例 synchronized:当多线程访问时，保证一次只能有一个请求访问 这个方法 
	public synchronized static MyProperties getInstance(){
		if( myproperties==null){
			myproperties=new MyProperties();
		}
		return myproperties;
	}
}
