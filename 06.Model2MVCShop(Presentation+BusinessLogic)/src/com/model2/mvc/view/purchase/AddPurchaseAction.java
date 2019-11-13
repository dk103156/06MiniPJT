package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;


public class AddPurchaseAction extends Action {

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession(true);
		
			Product product=new Product();
			product.setProdNo(Integer.parseInt( request.getParameter("prodNo") ) );
		
			User user = (User)session.getAttribute("user");
		
		Purchase purchase = new Purchase();
		
		purchase.setPurchaseProd(product);
		purchase.setBuyer(user);
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("receiverDate"));
		
		if(purchase == null) {
			System.out.println("purchase °¡ null ÀÓ");
		}else {
			System.out.println("purchase °¡ null ¾Æ´Ô => "+ purchase);
			purchase.setTranCode("1");
		}
		
		PurchaseService purchaseService=new PurchaseServiceImpl();
		purchaseService.addPurchase(purchase);
		
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
}