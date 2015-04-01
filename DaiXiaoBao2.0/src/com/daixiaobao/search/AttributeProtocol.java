package com.daixiaobao.search;

import com.daixiaobao.protocol.MyBaseProtocol;

public class AttributeProtocol extends MyBaseProtocol {

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return AttributeBean.class;
	}

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "admin/product/search.htm";
	}

}
