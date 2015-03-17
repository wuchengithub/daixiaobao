package com.daixiaobao.navigation;

import com.daixiaobao.protocol.MyBaseProtocol;

public class NavigationListProtocol extends MyBaseProtocol {
	
	private String methodName = "/admin/product/malllist.htm";

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
		return ResponseNavigationList.class;
	}

}
