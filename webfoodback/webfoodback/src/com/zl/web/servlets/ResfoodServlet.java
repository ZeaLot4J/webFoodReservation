package com.zl.web.servlets;

import java.io.IOException;  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import com.zl.bean.Resfood;
import com.zl.biz.impl.ResfoodBizImpl;
import com.zl.web.model.JsonModel;
import com.zl.web.utils.FileUploadUtil;
import com.zl.biz.IResfoodBiz;


public class ResfoodServlet extends BasicServlet {
	private static final long serialVersionUID = 1L;

	private IResfoodBiz resfoodBiz=new ResfoodBizImpl();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if( "".equals(op)){
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}else if( "findAll".equals(op)){
				findAll(request,response);
			}else if("findGoodsInfoByPage".equals(op)){
				findGoodsInfoByPage(request,response);
			}else if("addGoods".equals(op)){
				addGoods(request,response);
			}else if("delGoods".equals(op)){
				delGoods(request,response);
			}else if("updateGoods".equals(op)){
				updateGoods(request,response);
			}else if("goodsInfoSearch".equals(op)){
				goodsInfoSearch(request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void goodsInfoSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String fname=request.getParameter("fname");

		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		
		Integer pageFirst=(page-1)*rows; 
		
		Map<String, String> params=new HashMap<String,String>();
		

		if(fname!=null &&  !"".equals(fname)){
			params.put("fnamelike","%"+fname+"%");
		}
		
		List<Resfood> list=new ArrayList<Resfood>();
		list=resfoodBiz.findGoods(params, pageFirst, rows);
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("total", 5);
		map.put("rows", list);
		
		this.outPage(response, map);
	}

	private void updateGoods(HttpServletRequest request, HttpServletResponse response) {

		FileUploadUtil upload=new FileUploadUtil();

		PageContext pageContext=JspFactory.getDefaultFactory().getPageContext(this, request, response, null, true, 1024, true);
		
		Map<String, String> map=upload.fileUpload(pageContext);
		
		int result=resfoodBiz.updategoods(map.get("fname"), Double.parseDouble(map.get("normprice")),
				Double.parseDouble(map.get("realprice")), map.get("detail"), map.get("fphoto"),map.get("fid") );
		
		this.out(response, String.valueOf(result));
		
	}

	private void delGoods(HttpServletRequest request, HttpServletResponse response) {
		String fid=request.getParameter("fids");
		
		int result=resfoodBiz.delgoods(fid);
		this.out(response, result);
	}

	/**
	 * 添加商品信息
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void addGoods(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		
		FileUploadUtil upload=new FileUploadUtil();

		PageContext pageContext=JspFactory.getDefaultFactory().getPageContext(this, req, resp, null, true, 1024, true);
		
		Map<String, String> map=upload.fileUpload(pageContext);
		
	
		
		int result;
		try {
			result = resfoodBiz.addgoods(map.get("fname"), Double.parseDouble(map.get("normprice")),
					Double.parseDouble(map.get("realprice")), map.get("detail"), map.get("fphoto") );
			this.out(resp, String.valueOf(result));

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	
	//分页查询
	private void findGoodsInfoByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int pageFirst=0;
		
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));

		pageFirst=(page-1)*rows; // 计算每页开始的第一条索引   mysql第一条从0开始  
		List<Resfood> list=resfoodBiz.findByPage(pageFirst, rows);
		

		Map<String, Object> map=new HashMap<String, Object>();
		map.put("total", resfoodBiz.totalCount());
		map.put("rows", list);
		
		this.outPage(response, map);
	}



	private void findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		JsonModel jm=new JsonModel();
		try {
			List<Resfood> list=resfoodBiz.findAll();
			// code:1  -> obj :存数据
			// code:0  -> msg :错误信息 
			jm.setCode(1);
			jm.setObj(list);
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrorMsg( e.getMessage() );
		}
		super.outJson(jm, response);
	}

}
