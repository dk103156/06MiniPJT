package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


public class ListSaleAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		Search search=new Search();
		
		int page=1;
		if(request.getParameter("page") != null)
			page=Integer.parseInt(request.getParameter("page"));
		
		search.setPage(page);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		
		String pageUnit=getServletContext().getInitParameter("pageSize");
		search.setPageUnit(Integer.parseInt(pageUnit));
		
		ProductService service = new ProductServiceImpl();
		HashMap<String,Object> map = service.getProductList(search);

		request.setAttribute("map", map);
		request.setAttribute("searchVO", search);
		
				return "forward:/product/listProduct.jsp";;;;;;

		}
	}
