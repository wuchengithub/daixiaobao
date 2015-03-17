package com.daixiaobao.changePrice;

import com.daixiaobao.protocol.MyBaseProtocol;

public class ChangePriceProtocol extends MyBaseProtocol {

	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return ChangePriceBean.class;
	}

	@Override
	protected String getMethodName() {
		// TODO Auto-generated method stub
		return "/admin/product/changePrice.htm";
	}

}
