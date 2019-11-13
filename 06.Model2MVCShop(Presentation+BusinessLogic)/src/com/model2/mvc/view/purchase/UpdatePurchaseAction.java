package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;



public class UpdatePurchaseAction extends Action {

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Purchase purchase = new Purchase();
		PurchaseService purchaseService = new PurchaseServiceImpl();
		
		int tranNo= Integer.parseInt(request.getParameter("tranNo"));
		purchase = purchaseService.getPurchase(tranNo);
			System.out.println("tranNo 로 찾은 정보 : " + purchase);
		purchase.getBuyer().setUserId(request.getParameter("buyerId"));
		
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("divyDate"));

		purchaseService.updatePurchase(purchase);
		
		System.out.println("수정 후 purchase 정보 : " + purchase);
		
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
}