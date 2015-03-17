package com.daixiaobao.search;

import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

import android.content.Context;
import android.os.Handler;

public class SearchController {
	
	private Context context;
	private SearchDataProtocol protocol;

	public SearchController(Context context, String code) {
		this.context = context;
		/*protocol = new SearchDataProtocol();
		protocol.invoke(new SearchDataBean(LoginMessageDataUtils.getToken(context), LoginMessageDataUtils.getUID(context), 
				DeviceTool.getDeviceId(context), code), handler);*/
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	private OnSearchDataComeBackListener listener;
	
	public interface OnSearchDataComeBackListener {
		public void onSearchDataBack();
	}
	public void setOnSearchDataComeBackListener(OnSearchDataComeBackListener listener){
		this.listener = listener;
	}
}
