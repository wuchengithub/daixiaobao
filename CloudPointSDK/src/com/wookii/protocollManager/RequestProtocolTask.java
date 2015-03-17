package com.wookii.protocollManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.JsonReader;
import android.util.Log;

import com.wookii.model.RegisterRequest;
import com.wookii.protocol.RegisiterProtocol;

public class RequestProtocolTask implements Runnable {

	String http_request_method;
	// Map<String,String> params;
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	DataProtocolInterface dataProcotolInterface;

	BaseProtocol baseProtocol;
	private RegisterRequest model;
	private String json;

	public RequestProtocolTask(String http_request_method,
			List<NameValuePair> params,
			DataProtocolInterface dataProcotolInterface) {
		super();
		this.http_request_method = http_request_method;
		this.params = params;
		this.dataProcotolInterface = dataProcotolInterface;

		baseProtocol = new BaseProtocol();
		baseProtocol.setProcotolInterface(dataProcotolInterface);
	}

	public RequestProtocolTask(String pARAM_METHOD_NAME, String json,
			DataProtocolInterface regisiterProtocol) {
		
		super();
		this.http_request_method = pARAM_METHOD_NAME;
		this.json = json;
		this.dataProcotolInterface = regisiterProtocol;
		baseProtocol = new BaseProtocol();
		baseProtocol.setProcotolInterface(dataProcotolInterface);
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Object object = BaseProtocol.startInvoke(http_request_method, json);
		if (object != null) {
			baseProtocol.getProcotolInterface().onResposeProcotolData(object);
		} else {
			baseProtocol.getProcotolInterface().onResposeProcotolData(null);
		}
	}

}
