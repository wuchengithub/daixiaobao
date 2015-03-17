package com.daixiaobao.friend;

import com.daixiaobao.protocol.MyBaseProtocol;

public class BusinessListProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "/admin/seller/list.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseBusinessList.class;
	}

}
