package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;



public class UpdateProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {

		Product product = new Product();
		
		product.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		System.out.println("[ UpdateProductAction에 넘어온 productVO정보 확인 ]");
		System.out.println(product);
		
		ProductService productService=new ProductServiceImpl();
		productService.updateProduct(product);
		
		request.setAttribute("product", product);
		request.setAttribute("regDate", request.getParameter("regDate")); // EL, JSTL 쓰니까 안넘겨져서 이렇게 추가함...
		
		return "forward:/product/readProduct.jsp";
	}
}