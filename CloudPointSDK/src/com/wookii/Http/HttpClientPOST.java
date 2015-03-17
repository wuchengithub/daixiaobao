package com.wookii.Http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpClientPOST {

	private static final String TAG = "HttpClientPOST";

	private static HttpClientPOST httpClientPOST = null;

	private final String encode = "UTF-8";
	private final int ConnectionTimeout = 1000 *20;
	private int SoTimeout = 1000 *20;
	private final int SocketBufferSize = 10240;

	public static HttpClientPOST getInstance() {

		if (null == httpClientPOST) {
			httpClientPOST = new HttpClientPOST();
		}
		return httpClientPOST;
	}

	List<NameValuePair> params = new ArrayList<NameValuePair>();

	public String excuteHttpConnectionPost(String urlstr,
			List<NameValuePair> params) {

		HttpPost httpPost = new HttpPost(urlstr);
		this.params = params;

		String result = null;

		HttpResponse httpResponse = null;
		try {

			HttpParams httpParams = new BasicHttpParams();

			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

			HttpProtocolParams.setContentCharset(httpParams, encode);
			HttpProtocolParams.setHttpElementCharset(httpParams, encode);
			HttpProtocolParams.setUseExpectContinue(httpParams, true);
			HttpClientParams.setRedirecting(httpParams, true);
			HttpConnectionParams.setConnectionTimeout(httpParams,
					ConnectionTimeout);
			HttpConnectionParams.setSoTimeout(httpParams, SoTimeout);
			HttpConnectionParams.setSocketBufferSize(httpParams,
					SocketBufferSize);

			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpPost.setParams(httpParams);

			httpResponse = new DefaultHttpClient().execute(httpPost);

			int statuScode = httpResponse.getStatusLine().getStatusCode();
			if (statuScode == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
				// System.out.println("result:" + result);

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public Object excuteHttpConnectionPost(String requestUrl, String json) {
		// TODO Auto-generated method stub
		HttpPost httpPost = new HttpPost(requestUrl);

		String result = null;

		HttpResponse httpResponse = null;
		try {
			httpPost.addHeader("Content-Type", "application/json");
			HttpParams httpParams = new BasicHttpParams();
			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams, encode);
			HttpProtocolParams.setHttpElementCharset(httpParams, encode);
			HttpProtocolParams.setUseExpectContinue(httpParams, true);
			HttpClientParams.setRedirecting(httpParams, true);
			HttpConnectionParams.setConnectionTimeout(httpParams,
					ConnectionTimeout);
			HttpConnectionParams.setSoTimeout(httpParams, SoTimeout);
			HttpConnectionParams.setSocketBufferSize(httpParams,
					SocketBufferSize);

			httpPost.setEntity(new StringEntity(json, HTTP.UTF_8));
			Log.i(requestUrl, json);
			httpPost.setParams(httpParams);

			httpResponse = new DefaultHttpClient().execute(httpPost);

			int statuScode = httpResponse.getStatusLine().getStatusCode();
			if (statuScode == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i(TAG, result);

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}
