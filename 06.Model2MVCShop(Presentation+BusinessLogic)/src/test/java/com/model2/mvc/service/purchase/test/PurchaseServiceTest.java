package com.model2.mvc.service.purchase.test;

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
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;


/*
 *	FileName :  PurchaseServiceTest.java
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
public class PurchaseServiceTest {

	//==>@RunWith,@ContextConfiguration 이용 Wiring, Test 할 instance DI
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;

	//@Test
	public void testAddPurchase() throws Exception {
				
		Purchase purchase = new Purchase();
		Product product = new Product();
		User user = new User();
		
			product.setProdNo(10050);
		purchase.setPurchaseProd(product);
			user.setUserId("user19");
		purchase.setBuyer(user);
		purchase.setPaymentOption("1");
		purchase.setReceiverName("손오공");
		purchase.setReceiverPhone("111-1111-1111");
		purchase.setDivyAddr("천상계");
		purchase.setDivyRequest("근두운");
		purchase.setTranCode("1");
		purchase.setDivyDate("2019-11-11");
		
		purchaseService.addPurchase(purchase);
		
		purchase = purchaseService.getPurchase(10073); // tranNo

		//==> console 확인
		//System.out.println(purchase);
		
		//==> API 확인
		Assert.assertEquals(10050, purchase.getPurchaseProd().getProdNo());
		Assert.assertEquals("user19", purchase.getBuyer().getUserId());
		Assert.assertEquals("1  ", purchase.getPaymentOption());
		Assert.assertEquals("손오공", purchase.getReceiverName());
		Assert.assertEquals("111-1111-1111", purchase.getReceiverPhone());
		Assert.assertEquals("천상계", purchase.getDivyAddr());
		Assert.assertEquals("근두운", purchase.getDivyRequest());
		Assert.assertEquals("1  ", purchase.getTranCode());
		Assert.assertEquals("2019-11-11 00:00:00.0", purchase.getDivyDate());
	}
	
	//@Test
	public void testGetPurchase() throws Exception {
		
		Purchase purchase = new Purchase();
//		Product product = new Product();
//		User user = new User();
		//==> 필요하다면...
//			product.setProdNo(10050);
//		purchase.setPurchaseProd(product);
//			user.setUserId("user19");
//		purchase.setBuyer(user);
//		purchase.setPaymentOption("1");
//		purchase.setReceiverName("손오공");
//		purchase.setReceiverPhone("111-1111-1111");
//		purchase.setDivyAddr("천상계");
//		purchase.setDivyRequest("근두운");
//		purchase.setTranCode("1");
//		purchase.setDivyDate("2019-11-11");
		
		purchase = purchaseService.getPurchase(10073); // tranNo

		//==> console 확인
		//System.out.println(purchase);
		
		//==> API 확인
		Assert.assertEquals(10050, purchase.getPurchaseProd().getProdNo());
		Assert.assertEquals("user19", purchase.getBuyer().getUserId());
		Assert.assertEquals("1  ", purchase.getPaymentOption());
		Assert.assertEquals("손오공", purchase.getReceiverName());
		Assert.assertEquals("111-1111-1111", purchase.getReceiverPhone());
		Assert.assertEquals("천상계", purchase.getDivyAddr());
		Assert.assertEquals("근두운", purchase.getDivyRequest());
		Assert.assertEquals("1  ", purchase.getTranCode());
		Assert.assertEquals("2019-11-11 00:00:00.0", purchase.getDivyDate());

		Assert.assertNotNull(purchaseService.getPurchase(10051));
		Assert.assertNotNull(purchaseService.getPurchase(10052));
	}
	
	//@Test
	 public void testUpdatePurchase() throws Exception{
		 
		Purchase purchase = purchaseService.getPurchase(10073); // tranNo
		Assert.assertNotNull(purchase);
		
		Assert.assertEquals(10050, purchase.getPurchaseProd().getProdNo());
		Assert.assertEquals("user19", purchase.getBuyer().getUserId());
		Assert.assertEquals("1  ", purchase.getPaymentOption());
		Assert.assertEquals("손오공", purchase.getReceiverName());
		Assert.assertEquals("111-1111-1111", purchase.getReceiverPhone());
		Assert.assertEquals("천상계", purchase.getDivyAddr());
		Assert.assertEquals("근두운", purchase.getDivyRequest());
		Assert.assertEquals("1  ", purchase.getTranCode());
		Assert.assertEquals("2019-11-11 00:00:00.0", purchase.getDivyDate());

		purchase.setPaymentOption("2");
		purchase.setReceiverName("구미호");
		purchase.setReceiverPhone("999-9999-9999");
		purchase.setDivyAddr("숲속");
		purchase.setDivyRequest("간땡이");
		purchase.setDivyDate("2019-11-22");
		
		purchaseService.updatePurchase(purchase);		
		
		purchase = purchaseService.getPurchase(10073); // tranNo
		Assert.assertNotNull(purchase);
		
		//==> console 확인
		//System.out.println(purchase);
			
		//==> API 확인
		Assert.assertEquals("2  ", purchase.getPaymentOption());
		Assert.assertEquals("구미호", purchase.getReceiverName());
		Assert.assertEquals("999-9999-9999", purchase.getReceiverPhone());
		Assert.assertEquals("숲속", purchase.getDivyAddr());
		Assert.assertEquals("간땡이", purchase.getDivyRequest());
		Assert.assertEquals("1  ", purchase.getTranCode());
		Assert.assertEquals("2019-11-22 00:00:00.0", purchase.getDivyDate());
	 }
	 
	 
	 //@Test
	 public void testUpdateTranCode() throws Exception{
		 
		Purchase purchase = purchaseService.getPurchase(10072); // tranNo
		Assert.assertNotNull(purchase);		

		purchase.setTranCode("1");

		purchaseService.updateTranCode(purchase);		
		
		purchase = purchaseService.getPurchase(10072); // tranNo
		Assert.assertNotNull(purchase);
		
		//==> console 확인
		System.out.println(purchase);
			
		//==> API 확인
		Assert.assertEquals("2  ", purchase.getTranCode());
	 }
	 
	
	 //==>  주석을 풀고 실행하면....
	 @Test
	 public void testGetPurchaseListAll() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	Map<String, Object> map = purchaseService.getPurchaseList(search, "user19");
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
		for (int i =0 ;  i < list.size() ; i++) {
			System.out.println( "<"+ ( i +1 )+"> 번째 구매목록 => "+ list.get(i).toString() );
		}
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("");
	 	map = purchaseService.getPurchaseList(search, "user19");
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	//==> console 확인
//		for (int i =0 ;  i < list.size() ; i++) {
//			System.out.println( "<"+ ( i +1 )+"> 번째 구매목록 => "+ list.get(i).toString() );
//		}
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 
	 //@Test
	 public void testGetPurchaseListByProdNo() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("10043");
	 	Map<String,Object> map = purchaseService.getPurchaseList(search, "user01");
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(1, list.size());
	 	
		//==> console 확인
//		for (int i =0 ;  i < list.size() ; i++) {
//			System.out.println( "<"+ ( i +1 )+"> 번째 구매목록 => "+ list.get(i).toString() );
//		}
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = purchaseService.getPurchaseList(search, "user01");
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
//		for (int i =0 ;  i < list.size() ; i++) {
//			System.out.println( "<"+ ( i +1 )+"> 번째 구매목록 => "+ list.get(i).toString() );
//		}
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 
	 //@Test
	 public void testGetPurchaseListByReceiverName() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword("손오공");
	 	Map<String,Object> map = purchaseService.getPurchaseList(search, "user19");
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
		for (int i =0 ;  i < list.size() ; i++) {
			System.out.println( "<"+ ( i +1 )+"> 번째 구매목록 => "+ list.get(i).toString() );
		}
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = purchaseService.getPurchaseList(search, "user19");
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
		for (int i =0 ;  i < list.size() ; i++) {
			System.out.println( "<"+ ( i +1 )+"> 번째 구매목록 => "+ list.get(i).toString() );
		}
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }	 


}