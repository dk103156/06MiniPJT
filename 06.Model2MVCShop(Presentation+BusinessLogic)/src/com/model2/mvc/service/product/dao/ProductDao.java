package com.model2.mvc.service.product.dao;

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


public class ProductDao {
	
	///Field
	
	///Constructor
	public ProductDao(){
	}
	
	///Method
	public void insertProduct(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = 	"INSERT "+ 
								"INTO PRODUCT "+
								"VALUES (seq_product_prod_no.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		
		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setString(3, product.getManuDate().replace("-", ""));	
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getFileName());
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
	}

	public Product findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = 	"SELECT "+ 
								"prod_no, prod_name, prod_detail, manufacture_day, price, image_file, reg_date "+ 
								"FROM PRODUCT "+ 
								"WHERE prod_no = ?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, prodNo);

		ResultSet rs = pStmt.executeQuery();

		Product product = null;
		
		while (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
		}
		
		rs.close();
		pStmt.close();
		con.close();

		return product;
	}

	public Map<String,Object> getProductList(Search search) throws Exception {
		
		Map<String, Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		// Original Query 구성
		String sql = 	"SELECT p.*, t.tran_status_code, t.tran_no "+ 
								"FROM product p, transaction t "+
								"WHERE p.prod_no = t.prod_no(+)";
		
		if (search.getSearchCondition() != null) {
			if ( search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " AND p.prod_no LIKE '%" + search.getSearchKeyword() + "%'";
			}else if ( search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("") ) {
				sql += " AND UPPER(p.prod_name) LIKE UPPER('%" + search.getSearchKeyword() + "%')";
			}else if ( search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("") ) {
				sql += " AND p.price LIKE '%" + search.getSearchKeyword() + "%'";				
			}
		}
		sql += " ORDER BY p.prod_no";
		
		System.out.println("ProductDao :: Original SQL :: " + sql);
		
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDao :: totalCount :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);

		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println(search);
		
		List<Product> list = new ArrayList<Product>();
		ArrayList<Purchase> list2 = new ArrayList<Purchase>();
		
		while(rs.next()){
				Product product = new Product();
				Purchase purchase = new Purchase();
				product.setProdNo(rs.getInt("prod_no"));
				product.setProdName(rs.getString("prod_name"));
				product.setPrice(rs.getInt("price"));
				product.setRegDate(rs.getDate("reg_date"));
				purchase.setTranCode(rs.getString("tran_status_code"));
				purchase.setTranNo(rs.getInt("tran_no"));
				list.add(product);
				list2.add(purchase);
		}
		
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);
		map.put("list2", list2);
		
		rs.close();
		pStmt.close();
		con.close();

		return map;
	}

	public void updateProduct(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = 	"UPDATE PRODUCT "+ 
								"SET prod_name=?, prod_detail=?, manufacture_day=?, price=?, image_file=? "+ 
								"WHERE prod_no=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setString(3, product.getManuDate().replace("-", ""));			
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getFileName());
		pStmt.setInt(6, product.getProdNo());
		
		int confirm = pStmt.executeUpdate();
		
		if(confirm == 1) {
			System.out.println("[ Update 완료]");
		}else {
			System.out.println("[ Update 실패]");
		}
		pStmt.close();
		con.close();
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
		
		System.out.println("ProductDao :: make SQL :: "+ sql);	
		
		return sql;
	}	
	
	
}