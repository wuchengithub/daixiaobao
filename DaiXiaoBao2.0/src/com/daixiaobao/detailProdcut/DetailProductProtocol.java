package com.daixiaobao.detailProdcut;

import com.daixiaobao.protocol.MyBaseProtocol;

public class DetailProductProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "/admin/product/detail.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseDetailProduct.class;
	}

}
