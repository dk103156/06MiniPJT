package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;


//==> 구매관리 Controller
@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	
	@RequestMapping("/addPurchaseView.do")
	public String addPurchaseView(@RequestParam("prodNo") int prodNo, Model model, HttpServletRequest request) throws Exception {

		System.out.println("/addPurchaseView.do");

		HttpSession session = request.getSession();
		
		Product product=productService.getProduct(prodNo);
		User user = (User)session.getAttribute("user");
		model.addAttribute("product", product);
		model.addAttribute("user", user);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	
	@RequestMapping("/addPurchase.do")
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase, @RequestParam("prodNo") int prodNo, Model model, HttpServletRequest request ) throws Exception {

		System.out.println("/addPurchase.do");
		//Business Logic
		Product product =productService.getProduct(prodNo);
		User user= (User)request.getSession().getAttribute("user");
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		purchaseService.addPurchase(purchase);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase( @RequestParam("tranNo") int tranNo , Model model ) throws Exception {
		
		System.out.println("/getPurchase.do");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	
	
	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView( @ModelAttribute("purchase") Purchase purchase, @RequestParam("tranNo") int tranNo , Model model ) throws Exception{

		System.out.println("/updatePurchaseView.do");
		//Business Logic
		purchase = purchaseService.getPurchase(tranNo);
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase( @ModelAttribute("purchase") Purchase purchase, @RequestParam("tranNo") int tranNo, @RequestParam("prodNo") int prodNo, Model model, HttpServletRequest request ) throws Exception{

		System.out.println("/updatePurchase.do");
		//Business Logic		
		User user= (User)request.getSession().getAttribute("user");
		Product product =productService.getProduct(prodNo);
		
		purchase.setPurchaseProd(product);
		purchase.setBuyer(user);
		purchase.setTranNo(tranNo);		
		
		purchaseService.updatePurchase(purchase);
		
		// Model 과 View 연결
		model.addAttribute("purchase", purchaseService.getPurchase(tranNo));
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listPurchase.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		User user =(User)request.getSession().getAttribute("user");
		Map<String , Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	
	@RequestMapping("/listSale.do")
	public String listSale(@ModelAttribute("search") Search search, Model model ) throws Exception {
		
		System.out.println("/listSale.do");
		
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String, Object> map = purchaseService.getSaleList(search);
		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "forward:/purchase/listSale.jsp";
	}
	
	@RequestMapping("/updateTranCode.do")
	public String updateTranCode( @ModelAttribute("purchase") Purchase purchase, @RequestParam("tranNo") int tranNo, @RequestParam("tranCode") String tranCode, HttpServletRequest request) throws Exception {
		
		System.out.println("/updateTranCode.do");
		
		// Business logic 수행
		User user =(User)request.getSession().getAttribute("user");
		
		purchase.setTranNo(tranNo);
		purchase.setTranCode(tranCode);

		purchase =purchaseService.getPurchase(tranNo);
		
		purchaseService.updateTranCode(purchase);

		
		if(user.getRole().equals("admin")) {
			return "redirect:listProduct.do?menu=manage";
		}
			return "redirect:listPurchase.do";
		
	}
	
}