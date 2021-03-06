package com.wookii.Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;

public class HttpAction {

	private static HttpAction httpAction = null;

	public static HttpAction getInstance() {

		if (null == httpAction) {
			httpAction = new HttpAction();
		}
		return httpAction;
	}

	// POST
	public String excuteHttpConnectionPost(String urlstr) {

		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader insReader = null;

		try {
			url = new URL(urlstr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Charset", "utf-8");
			connection.setReadTimeout(120000);

			int statusCode = connection.getResponseCode();
			long contentLength = connection.getContentLength();

			if (statusCode == HttpStatus.SC_OK) {

				// Object object = connection.getContent();
				insReader = new InputStreamReader(connection.getInputStream());

				BufferedReader bufferedReader = new BufferedReader(insReader);

				StringBuffer strBuffer = new StringBuffer();

				String line = null;

				while ((line = bufferedReader.readLine()) != null) {

					strBuffer.append(line);

				}

				result = strBuffer.toString();

				// result = object.toString();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (insReader != null) {
				try {
					insReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}

}
