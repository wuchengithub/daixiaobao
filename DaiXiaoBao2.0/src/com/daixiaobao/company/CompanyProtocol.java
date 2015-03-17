package com.daixiaobao.company;

import com.daixiaobao.protocol.MyBaseProtocol;

public class CompanyProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "admin/party/supplier.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseCompany.class;
	}

}
