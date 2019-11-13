package com.model2.mvc.service.product.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


/*
 *	FileName :  ProductServiceTest.java
 * ㅇ JUnit4 (Test Framework) 과 Spring Framework 통합 Test( Unit Test )
 * ㅇ Spring 은 JUnit 4를 위한 지원 클래스를 통해 스프링 기반 통합 테스트 코드를 작성 할 수 있다.
 * ㅇ @RunWith : Meta-data 를 통한 wiring(생성, DI) 할 객체 구현체 지정
 * ㅇ @ContextConfiguration : Meta-data location 지정
 * ㅇ @Test : 테스트 실행 소스 지정
 */
@RunWith(SpringJUnit4ClassRunner.class)

//==> Meta-Data 를 다양하게 Wiring 하자...
//@ContextConfiguration(locations = { "classpath:config/context-*.xml" })
@ContextConfiguration	(locations = {	"classpath:config/context-common.xml",
																	"classpath:config/context-aspect.xml",
																	"classpath:config/context-mybatis.xml",
																	"classpath:config/context-transaction.xml" })
//@ContextConfiguration(locations = { "classpath:config/context-common.xml" })
public class ProductServiceTest {

	//==>@RunWith, @ContextConfiguration 이용 Wiring, Test 할 instance DI
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	//@Test
	public void testAddProduct() throws Exception {
		
		Product product = new Product();
		
		product.setProdName("황정민 명대사");
		product.setProdDetail("드루와");
		product.setManuDate("20191101");
		product.setPrice(55555);
		product.setFileName("test.jpg");
		
		productService.addProduct(product);
		
		product = productService.getProduct(10047);

		//==> console 확인
		//System.out.println(product);
		
		//==> API 확인
		Assert.assertEquals(10047, product.getProdNo());
		Assert.assertEquals("황정민 명대사", product.getProdName());
		Assert.assertEquals("드루와", product.getProdDetail());
		Assert.assertEquals("20191101", product.getManuDate());
		Assert.assertEquals(55555, product.getPrice());
	}
	
	//@Test
	public void testGetProduct() throws Exception {
		
		Product product = new Product();
		//==> 필요하다면...
//		product.setProdName("황정민 명대사");
//		product.setProdDetail("드루와");
//		product.setManuDate("20191101");
//		product.setPrice(55555);
//		product.setFileName("test.jpg");
		
		product = productService.getProduct(10047);

		//==> console 확인
		//System.out.println(product);
		
		//==> API 확인
		Assert.assertEquals(10047, product.getProdNo());
		Assert.assertEquals("황정민 명대사", product.getProdName());
		Assert.assertEquals("드루와", product.getProdDetail());
		Assert.assertEquals("20191101", product.getManuDate());
		Assert.assertEquals(55555, product.getPrice());

		Assert.assertNotNull(productService.getProduct(10000));
		Assert.assertNotNull(productService.getProduct(10001));
	}
	
	//@Test
	 public void testUpdateProduct() throws Exception{
		 
		Product product = productService.getProduct(10047);
		Assert.assertNotNull(product);
		
		Assert.assertEquals("황정민 명대사", product.getProdName());
		Assert.assertEquals("드루와", product.getProdDetail());
		Assert.assertEquals("20191101", product.getManuDate());
		Assert.assertEquals(55555, product.getPrice());
		Assert.assertEquals("test.jpg", product.getFileName());

		product.setProdName("원빈 명대사");
		product.setProdDetail("금이빨 빼고 모조리");
		product.setManuDate("20191103");
		product.setPrice(11111);
		product.setFileName("test111.jpg");
		
		productService.updateProduct(product);
		
		product = productService.getProduct(10047);
		Assert.assertNotNull(product);
		
		//==> console 확인
		//System.out.println(product);
			
		//==> API 확인
		Assert.assertEquals("원빈 명대사", product.getProdName());
		Assert.assertEquals("금이빨 빼고 모조리", product.getProdDetail());
		Assert.assertEquals("20191103", product.getManuDate());
		Assert.assertEquals(11111, product.getPrice());
		Assert.assertEquals("test111.jpg", product.getFileName());
	 }
	 
	
	 //==>  주석을 풀고 실행하면....
	 //@Test
	 public void testGetProductListAll() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
	 	//System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("");
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	//==> console 확인
	 	//System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 //@Test
	 public void testGetProductListByProdNo() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("10006");
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
//		for (int i =0 ;  i < list.size() ; i++) {
//			System.out.println( "<"+ ( i +1 )+"> 번째 상품.."+ list.get(i).toString() );
//		}
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
//		for (int i =0 ;  i < list.size() ; i++) {
//			System.out.println( "<"+ ( i +1 )+"> 번째 상품.."+ list.get(i).toString() );
//		}
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 @Test
	 public void testGetProductListByProdName() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword("연꽃");
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(1, list.size());
	 	
		//==> console 확인
		for (int i =0 ;  i < list.size() ; i++) {
			System.out.println( "<"+ ( i +1 )+"> 번째 상품 => "+ list.get(i).toString() );
		}
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
		for (int i =0 ;  i < list.size() ; i++) {
			System.out.println( "<"+ ( i +1 )+"> 번째 상품 => "+ list.get(i).toString() );
		}
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }	 
	 

	 //@Test
	 public void testGetProductListByPrice() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("2");
	 	search.setSearchKeyword("123");
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
		for (int i =0 ;  i < list.size() ; i++) {
			System.out.println( "<"+ ( i +1 )+"> 번째 상품 => "+ list.get(i).toString() );
		}
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
		for (int i =0 ;  i < list.size() ; i++) {
			System.out.println( "<"+ ( i +1 )+"> 번째 상품 => "+ list.get(i).toString() );
		}
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }	 	 
	 
	 
	 
}