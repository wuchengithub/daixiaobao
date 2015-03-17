package com.daixiaobao.version;

import com.daixiaobao.protocol.MyBaseProtocol;

public class VersionProtocol extends MyBaseProtocol {

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "upgrade.htm";
	}

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ResponseVersion.class;
	}

}
