package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;


public class UpdateProductViewAction extends Action{

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int prodNo= Integer.parseInt(request.getParameter("prodNo"));
		
		ProductService productService=new ProductServiceImpl();
		Product product=productService.getProduct(prodNo);
		
		request.setAttribute("product", product);
		
		System.out.println("[ UpdateProductViewAction에 넘어온 productVO정보 확인 ]");
		System.out.println(product);
		
		return "forward:/product/updateProductView.jsp";
	}
}
