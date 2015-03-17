package com.daixiaobao.proxy.list;

import com.daixiaobao.protocol.MyBaseProtocol;

public class ProxyListProtocol extends MyBaseProtocol {
	
	private String methodName = "/admin/product/storelist.htm";

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return methodName;
	}

	public void setMethodName(String methodName){
		this.methodName = methodName;
	}
	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseProxyList.class;
	}

}
