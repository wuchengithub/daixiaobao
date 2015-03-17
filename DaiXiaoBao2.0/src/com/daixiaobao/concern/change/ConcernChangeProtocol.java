package com.daixiaobao.concern.change;

import com.daixiaobao.protocol.MyBaseProtocol;

public class ConcernChangeProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "admin/product/agent.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseConcernChange.class;
	}

}
