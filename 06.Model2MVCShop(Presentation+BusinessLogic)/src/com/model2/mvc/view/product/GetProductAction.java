package com.model2.mvc.view.product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;


public class GetProductAction extends Action{

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 최근 본 상품 구현!!!
		Cookie[] cookies = request.getCookies();
		
		for(int i=0; i<cookies.length; i++) {
			
			if(cookies[i] == null) {
				System.out.println("cookie null : " +request.getParameter("prodNo"));
				cookies[i] = new Cookie("history", request.getParameter("prodNo"));
				cookies[i].setMaxAge(-1);
				response.addCookie(cookies[i]);
			}
			else if (cookies[i] != null && cookies[i].getName().equals("history")) {
				System.out.println("cookie value : " + cookies[i].getValue());
				cookies[i] = new Cookie("history", cookies[i].getValue()+","+request.getParameter("prodNo"));
				cookies[i].setMaxAge(-1);
				response.addCookie(cookies[i]);
			}else {
				System.out.println("else : " + request.getParameter("prodNo"));
				cookies[i] = new Cookie("history", request.getParameter("prodNo"));
				cookies[i].setMaxAge(-1);
				response.addCookie(cookies[i]);
			}
			
		} // end of 최근 본 상품 구현
		
		int prodNo= Integer.parseInt(request.getParameter("prodNo"));
		
		ProductService productService = new ProductServiceImpl();
		Product product = productService.getProduct(prodNo);
		
		request.setAttribute("product", product);
		request.setAttribute("prodNo", prodNo);
		
		System.out.println("[ GetProductAction에 넘어온 productVO정보 확인 ]");
		System.out.println(product);
		
		if(request.getParameter("menu").equals("manage")) {
			return "forward:updateProductView.do";
		}else {
			return "forward:/product/getProduct.jsp";
		}
	}
}