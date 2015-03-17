package com.daixiaobao.categroy;

import com.daixiaobao.protocol.MyBaseProtocol;

public class CategroyProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "/admin/product/categorys.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseCatagroy.class;
	}

}
