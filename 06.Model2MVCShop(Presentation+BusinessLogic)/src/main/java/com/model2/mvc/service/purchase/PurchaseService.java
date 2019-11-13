package com.model2.mvc.service.purchase;

import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;


//==> 구매관리에서 서비스할 내용 추상화/캡슐화한 Service  Interface Definition  
public interface PurchaseService {
	
	// 구매
	public void addPurchase(Purchase purchase) throws Exception;
	
	// 구매 정보 확인
	public Purchase getPurchase(int tranNo) throws Exception;
	
		public Purchase getPurchase2(int prodNo) throws Exception;
	
	// 구매 정보 리스트
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception;
	
		public HashMap<String,Object> getSaleList(Search search) throws Exception;
	
	// 구매 정보 수정
	public void updatePurchase(Purchase purchase) throws Exception;
	
	// TranCode 수정
	public void updateTranCode(Purchase purchase) throws Exception;
	
}