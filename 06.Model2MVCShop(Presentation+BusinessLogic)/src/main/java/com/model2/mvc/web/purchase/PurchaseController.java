package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	
//	@RequestMapping("/getUser.do")
//	public String getUser( @RequestParam("userId") String userId , Model model ) throws Exception {
//		
//		System.out.println("/getUser.do");
//		//Business Logic
//		User user = userService.getUser(userId);
//		// Model 과 View 연결
//		model.addAttribute("user", user);
//		
//		return "forward:/user/getUser.jsp";
//	}
//	
//	@RequestMapping("/updateUserView.do")
//	public String updateUserView( @RequestParam("userId") String userId , Model model ) throws Exception{
//
//		System.out.println("/updateUserView.do");
//		//Business Logic
//		User user = userService.getUser(userId);
//		// Model 과 View 연결
//		model.addAttribute("user", user);
//		
//		return "forward:/user/updateUser.jsp";
//	}
//	
//	@RequestMapping("/updateUser.do")
//	public String updateUser( @ModelAttribute("user") User user , Model model , HttpSession session) throws Exception{
//
//		System.out.println("/updateUser.do");
//		//Business Logic
//		userService.updateUser(user);
//		
//		String sessionId=((User)session.getAttribute("user")).getUserId();
//		if(sessionId.equals(user.getUserId())){
//			session.setAttribute("user", user);
//		}
//		
//		return "redirect:/getUser.do?userId="+user.getUserId();
//	}
//	
//	@RequestMapping("/loginView.do")
//	public String loginView() throws Exception{
//		
//		System.out.println("/loginView.do");
//
//		return "redirect:/user/loginView.jsp";
//	}
//	
//	@RequestMapping("/login.do")
//	public String login(@ModelAttribute("user") User user , HttpSession session ) throws Exception{
//		
//		System.out.println("/login.do");
//		//Business Logic
//		User dbUser=userService.getUser(user.getUserId());
//		
//		if( user.getPassword().equals(dbUser.getPassword())){
//			session.setAttribute("user", dbUser);
//		}
//		
//		return "redirect:/index.jsp";
//	}
//	
//	@RequestMapping("/logout.do")
//	public String logout(HttpSession session ) throws Exception{
//		
//		System.out.println("/logout.do");
//		
//		session.invalidate();
//		
//		return "redirect:/index.jsp";
//	}
//	
//	@RequestMapping("/checkDuplication.do")
//	public String checkDuplication( @RequestParam("userId") String userId , Model model ) throws Exception{
//		
//		System.out.println("/checkDuplication.do");
//		//Business Logic
//		boolean result=userService.checkDuplication(userId);
//		// Model 과 View 연결
//		model.addAttribute("result", new Boolean(result));
//		model.addAttribute("userId", userId);
//
//		return "forward:/user/checkDuplication.jsp";
//	}
//	
//	@RequestMapping("/listUser.do")
//	public String listUser( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
//		
//		System.out.println("/listUser.do");
//		
//		if(search.getCurrentPage() ==0 ){
//			search.setCurrentPage(1);
//		}
//		search.setPageSize(pageSize);
//		
//		// Business logic 수행
//		Map<String , Object> map=userService.getUserList(search);
//		
//		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
//		System.out.println(resultPage);
//		
//		// Model 과 View 연결
//		model.addAttribute("list", map.get("list"));
//		model.addAttribute("resultPage", resultPage);
//		model.addAttribute("search", search);
//		
//		return "forward:/user/listUser.jsp";
//	}
	
}