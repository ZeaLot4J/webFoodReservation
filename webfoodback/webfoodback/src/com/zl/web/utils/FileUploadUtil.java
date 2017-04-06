package com.zl.web.utils;

import java.util.Collection; 
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.jsp.PageContext;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.SmartUpload;

public class FileUploadUtil {
	public static String PATH = "../pics";
	private static final String ALLOWED = "doc,txt,jpg,png";
	private static final String DENIED = "exe,bat,jsp";
	private static final int MAXFILESIZE = 10 * 1024 * 1024;
	private static final int TOTALMAXFILESIZE = 100 * 1024 * 1024;

	public Map<String, String> fileUpload(PageContext pageContext) {
		Map<String, String> map = new HashMap<String, String>();
		// 新建一个 SmartUpload 对象
		SmartUpload su = new SmartUpload();
		try {
			// 初始化su对象
			su.initialize(pageContext);
			// 文件上传控制
			// 限制文件大小
			su.setMaxFileSize(MAXFILESIZE);
			// 限制上传文件总长度
			su.setTotalMaxFileSize(TOTALMAXFILESIZE);
			// 限制上传文件类型(通过扩展名)
			su.setAllowedFilesList(ALLOWED);// 允许上传文件类型
			su.setDeniedFilesList(DENIED);// 禁止上传文件类型
			// 设置编码集
			su.setCharset("utf-8");
			// 上传文件
			su.upload();
			// 将上传文件保存到指定目录
			Request request = su.getRequest();// 此时的request 是smartupload中的
			// 获取表单中所有的普通表单元素的元素名
			Enumeration<String> names = request.getParameterNames();
			// 根据元素名取出表单数据
			String key;
			while (names.hasMoreElements()) {
				key = names.nextElement();
				map.put(key, request.getParameter(key));
			}
			// 处理上传文件
			Files files = su.getFiles();

			String fieldName = "photo";// 上传文件的那个文本框的name属性的属性值

			// 说明用户有文件要上传
			if (files != null && files.getCount() > 0) {
				String filePath = pageContext.getServletContext().getRealPath("/") + PATH;
				java.io.File f = new java.io.File(filePath);
				if (!f.exists()) {
					f.mkdirs();
				}
				Collection<File> collection = files.getCollection();
				String fileName = "";
				String picPath = "";
				for (File fl : collection) {// 注意 此时也是smartupload中的file
					if (!fl.isMissing()) {// 如果上传的时候没有丢失数据
						// 上传的文件名重命名
						fileName = new Date().getTime() + "" + new Random().nextInt(9999) + "." + fl.getFileExt();

						// 将用户上传的文件保存到服务器
						fl.saveAs(filePath + "/" + fileName);

						// 将保存到服务器的文件的相对于项目于的路劲保存到数据库
						picPath += PATH + "/" + fileName + ",";
					}
					fieldName = fl.getFieldName();
				}
				if (picPath.contains(",")) {// 说明有文件上传
					picPath = picPath.substring(0, picPath.lastIndexOf(","));
				}
				map.put(fieldName, picPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}

}
