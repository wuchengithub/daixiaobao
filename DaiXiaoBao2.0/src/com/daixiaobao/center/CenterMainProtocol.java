package com.daixiaobao.center;

import com.daixiaobao.protocol.MyBaseProtocol;

public class CenterMainProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "admin/main.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseCenterMain.class;
	}

}
