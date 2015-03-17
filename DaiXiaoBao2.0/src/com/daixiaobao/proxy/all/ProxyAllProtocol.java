package com.daixiaobao.proxy.all;

import com.daixiaobao.protocol.MyBaseProtocol;

public class ProxyAllProtocol extends MyBaseProtocol{

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return RequestProxyAllBean.class;
	}

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "admin/product/agent.htm";
	}

}
