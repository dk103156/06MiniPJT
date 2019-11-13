package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;



public class AddPurchaseViewAction extends Action{

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int prodNo= Integer.parseInt(request.getParameter("prod_no"));		
		
		ProductService productService = new ProductServiceImpl();
		Product product = productService.getProduct(prodNo);
		
		HttpSession session = request.getSession(false);
		User user = (User)session.getAttribute("user");
		
		session.setAttribute("user",  user);
		request.setAttribute("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
}
