package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.model2.mvc.service.product.ProductService;


//==> 상품관리 Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
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
	
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception {

		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct( @ModelAttribute("product") Product product, @RequestParam("manuDate") String manuDate, Model model ) throws Exception {

		System.out.println("/addProduct.do");
		//Business Logic
		product.setManuDate(manuDate.replace("-", ""));
		productService.addProduct(product);
		
		model.addAttribute("product", product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct( @RequestParam("prodNo") int prodNo , @RequestParam("menu") String menu, Model model, HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
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
		
		System.out.println("/getProduct.do");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		model.addAttribute("prodNo", prodNo);
		
		if(menu.equals("manage")) {
			return "forward:updateProductView.do";
		}else {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct( @ModelAttribute("product") Product product , Model model, @RequestParam("manuDate") String manuDate) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		product.setManuDate(manuDate.replace("-", ""));
		productService.updateProduct(product);
		model.addAttribute("product", productService.getProduct(product.getProdNo()) );	
		//System.out.println("여기에요 \n : " +productService.getProduct(product.getProdNo()).getRegDate());
		
		return "forward:/product/readProduct.jsp";
	}
	
	@RequestMapping("/listProduct.do")
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("list2", map.get("list2"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";
	}
}