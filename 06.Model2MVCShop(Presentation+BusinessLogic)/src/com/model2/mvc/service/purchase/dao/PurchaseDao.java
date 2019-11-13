package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;


public class PurchaseDao {
	
	public PurchaseDao(){
	}

	public void insertPurchase(Purchase purchase) throws Exception{
		
		Connection con = DBUtil.getConnection();

		String sql = 	"INSERT "+ 
								"INTO TRANSACTION "+ 
								"VALUES (seq_transaction_tran_no.NEXTVAL,?,?,?,?,?,?,?,?,SYSDATE,?)";
		
		PreparedStatement pstmt = con.prepareStatement(sql);
		
		pstmt.setInt(1, purchase.getPurchaseProd().getProdNo());
		pstmt.setString(2, purchase.getBuyer().getUserId());
		pstmt.setString(3, purchase.getPaymentOption());	
		pstmt.setString(4, purchase.getReceiverName());
		pstmt.setString(5, purchase.getReceiverPhone());
		pstmt.setString(6, purchase.getDivyAddr());
		pstmt.setString(7, purchase.getDivyRequest());
		pstmt.setString(8, purchase.getTranCode());
		pstmt.setString(9, purchase.getDivyDate());
		
		int confirm = pstmt.executeUpdate();
		
		if( confirm == 1 ) {
			System.out.println("상품 구매가 완료 되었습니다");
		}else {
			System.out.println("상품 구매가 완료되지 않았습니다.");
		}
		
		con.close();
		
	}

	public Purchase findPurchase(int tranNo) throws Exception{

		Connection con = DBUtil.getConnection();

		String sql = 	"SELECT "+ 
								"tran_no, prod_no, buyer_id, payment_option, receiver_name, receiver_phone, demailaddr, dlvy_request, tran_status_code, order_date, dlvy_date "+ 
								"FROM TRANSACTION "+ 
								"WHERE tran_no=?";
		
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, tranNo);

		ResultSet rs = pstmt.executeQuery();
		
		Product product = new Product();
		User user = new User();

		Purchase purchase = null;
		while (rs.next()) {
			purchase = new Purchase();
			
			purchase.setTranNo(rs.getInt("tran_no"));
			
			product.setProdNo(rs.getInt("prod_no"));
			purchase.setPurchaseProd(product);
			
			user.setUserId(rs.getString("buyer_id"));
			purchase.setBuyer(user);
			
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDivyAddr(rs.getString("demailaddr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setOrderDate(rs.getDate("order_date"));
			purchase.setDivyDate(rs.getString("dlvy_date"));

		}
		
		con.close();
		
		
		return purchase;
	}

	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		
		Map<String, Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		// Original Query 구성
		String sql = 	"SELECT * "+ 				
								"FROM TRANSACTION "+ 				
								"WHERE buyer_id = \'"+buyerId+"\'";
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0")) {
				sql += " AND PROD_NO LIKE '%" + search.getSearchKeyword()
						+ "%'";
			}else if (search.getSearchCondition().equals("1")) {
				sql += " AND UPPER(RECEIVER_NAME) LIKE UPPER('%" + search.getSearchKeyword()
						+ "%')";				
			}
		}
		sql += " ORDER BY tran_no";
		
		System.out.println("PurchaseDao :: Original SQL :: " + sql);
		
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("PurchaseDAO :: totalCount  :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println(search);
		
		List<Purchase> list = new ArrayList<Purchase>();
		
		Product product = new Product();
		User user = new User();

		Purchase purchase = null;
		
		while (rs.next()) {
			purchase = new Purchase();
			
			purchase.setTranNo(rs.getInt("tran_no"));
			
			product.setProdNo(rs.getInt("prod_no"));
			purchase.setPurchaseProd(product);
			
			user.setUserId(rs.getString("buyer_id"));
			purchase.setBuyer(user);
			
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDivyAddr(rs.getString("demailaddr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setOrderDate(rs.getDate("order_date"));
			purchase.setDivyDate(rs.getString("dlvy_date"));
			list.add(purchase);

		}

		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		pStmt.close();
		con.close();
			
		return map;
	}

	public HashMap<String, Object> getSaleList(Search search) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		return map;
	}

	public void updatePurchase(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = 	"UPDATE TRANSACTION "+ 
								"SET payment_option=?, receiver_name=?, receiver_phone=?, demailaddr=?, dlvy_request=?, dlvy_date=? "+ 
								"WHERE tran_no=?";
		
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, purchase.getPaymentOption());
		pstmt.setString(2, purchase.getReceiverName());
		pstmt.setString(3, purchase.getReceiverPhone());
		pstmt.setString(4, purchase.getDivyAddr());
		pstmt.setString(5, purchase.getDivyRequest());
		pstmt.setString(6, purchase.getDivyDate());
		pstmt.setInt(7, purchase.getTranNo());
		
		int confirm = pstmt.executeUpdate();
		
		if(confirm == 1) {
			System.out.println("[ 구매 정보 Update 완료]");
		}else {
			System.out.println("[ 구매 정보 Update 실패]");
		}
		con.close();		
	}

	public void updateTranCode(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE TRANSACTION SET tran_status_code=? WHERE tran_no=?";
		
		PreparedStatement pstmt = con.prepareStatement(sql);
		
		if(purchase.getTranCode().equals("1")) {
			pstmt.setString(1, "2");
		}else if(purchase.getTranCode().equals("2")){
			pstmt.setString(1, "3");			
		}
		pstmt.setInt(2, purchase.getTranNo());
		
		int confirm = pstmt.executeUpdate();
		
		if(confirm == 1) {
			System.out.println("배송 정보 수정이 완료되었습니다.");
		}else {
			System.out.println("배송 정보 수정이 완료되지 않았습니다.");
		}

	}
	
	
	// 게시판 Page 처리를 위한 전체 Row(totalCount)  return
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ " ) countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		
		if( rs.next() ){
			totalCount = rs.getInt(1); // 1번째 결과값을 totalCount에 넣음!
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		
		sql = 	"SELECT * "+ 
					"FROM (	SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("PurchaseDao :: make SQL :: "+ sql);	
		
		return sql;
	}		

}
