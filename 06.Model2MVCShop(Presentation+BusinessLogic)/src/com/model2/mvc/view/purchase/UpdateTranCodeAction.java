package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;



public class UpdateTranCodeAction extends Action {

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {

		Purchase purchase = new Purchase();
		
		HttpSession session = request.getSession(false);
		User user = (User)session.getAttribute("user");
		
		purchase.setTranNo(Integer.parseInt(request.getParameter("tranNo")));
		purchase.setTranCode(request.getParameter("tranCode"));
		System.out.println("[ UpdateTranCodeActionÀÇ trancode Ãâ·Â ]");
		System.out.println(purchase.getTranNo());
		System.out.println(purchase.getTranCode());
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		purchaseService.updateTranCode(purchase);
		
		
		if(user.getRole().equals("admin")) {
			return "redirect:listProduct.do?menu=manage";
		}
			return "redirect:listPurchase.do";
		
	}
}