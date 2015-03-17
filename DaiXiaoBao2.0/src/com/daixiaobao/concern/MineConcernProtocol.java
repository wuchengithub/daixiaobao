package com.daixiaobao.concern;

import com.daixiaobao.protocol.MyBaseProtocol;

public class MineConcernProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "/admin/product/mylist.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseMineConcern.class;
	}

}
