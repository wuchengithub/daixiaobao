package com.wookii.protocollManager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.util.Log;

import com.wookii.Http.HttpClientPOST;

public class BaseProtocol {

	public static final String HTTP_REQUEST_URL = "http://api.lady365.com.cn/";
	//public static final String HTTP_REQUEST_URL = "http://192.168.0.104:86/";
	//public static final String HTTP_REQUEST_URL = "http://mikoo.88ip.cn:86/";

	List<NameValuePair> params = new ArrayList<NameValuePair>();

	public static Object startInvoke(String methodName,
			List<NameValuePair> params) {

		String requestUrl = HTTP_REQUEST_URL + methodName;

		// String strUrl = strtoUrl(requestUrl,params);

		HttpClientPOST httpClientPOST = HttpClientPOST.getInstance();

		Object object = httpClientPOST.excuteHttpConnectionPost(requestUrl,
				params);
		return object;
	}

	// ƴдURL
	public static String strtoUrl(String path, Map<String, String> params) {

		if (params == null) {
			return path;
		}

		StringBuilder sb = new StringBuilder(path);
		sb.append('?');
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue())).append('&');
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	private DataProtocolInterface procotolInterface;

	public static final String TAG = BaseProtocol.class.getSimpleName();

	public DataProtocolInterface getProcotolInterface() {
		return procotolInterface;
	}

	public void setProcotolInterface(DataProtocolInterface procotolInterface) {
		this.procotolInterface = procotolInterface;
	}

	public static Object startInvoke(String http_request_method, String json) {
		// TODO Auto-generated method stub
		String requestUrl = HTTP_REQUEST_URL + http_request_method;

		// String strUrl = strtoUrl(requestUrl,params);

		HttpClientPOST httpClientPOST = HttpClientPOST.getInstance();

		Object object = httpClientPOST.excuteHttpConnectionPost(requestUrl,
				json);

		return object;
	}

}
