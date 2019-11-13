package com.model2.mvc.view.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;



public class ListPurchaseAction extends Action {

	@Override
	public String execute(	HttpServletRequest request, HttpServletResponse response) throws Exception {		
		
		Search search=new Search();
		
		int currentPage=1;
		
		if(request.getParameter("currentPage") != null){
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
		}
		
		search.setCurrentPage(currentPage);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		
		// web.xml  meta-data 로 부터 상수 추출 
		int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		int pageUnit  =  Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
		search.setPageSize(pageSize);
		
		//로그인 정보로 id가져옴
		HttpSession session = request.getSession(false);
		User user = (User)session.getAttribute("user");
		
		String buyerId= user.getUserId();
		
		// Business logic 수행
		PurchaseService purchaseService = new PurchaseServiceImpl();
		Map<String,Object> map = purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage	= 
				new Page( currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("ListProductAction :: "+resultPage);

		// Model 과 View 연결
		request.setAttribute("list", map.get("list"));
		request.setAttribute("resultPage", resultPage);
		request.setAttribute("search", search);
			//request.setAttribute("menu", (String)request.getParameter("menu"));
		
		return "forward:/purchase/listPurchase.jsp";
		}
	}
