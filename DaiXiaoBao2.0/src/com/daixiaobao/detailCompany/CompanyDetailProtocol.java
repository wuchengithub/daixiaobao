package com.daixiaobao.detailCompany;

import com.daixiaobao.protocol.MyBaseProtocol;

public class CompanyDetailProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "admin/party/supplierInfo.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseDetailCompany.class;
	}

}
