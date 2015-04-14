package com.daixiaobao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.daixiaobao.Login.LoginAndRegActivity;


/**
 * 通用工具类
 * 
 * @author wood
 */
public class CommonUtil {

	public static final int LOGIN_REQUEST_CODE = 3;
	public static final int REGISTER_REQUEST_CODE = 5;

	public static int SUCCESS = 0;
	public static int PARAMETER_ERROR = 1;
	/** 参数格式不正确等 */
	public static int OTHER_ERROR = 2;
	public static int NO_MAP = 3;

	/**
	 * 开启登录界面
	 * 
	 * @param context
	 *            Activity context
	 * @param requestCode
	 */
	public static void openLoginView(Context context, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(context, LoginAndRegActivity.class);
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	/**
	 * 开启注册界面
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void openRegView(Context context, int requestCode) {
		Intent intent = new Intent();
		intent.putExtra("boolean", true);
		intent.setClass(context, LoginAndRegActivity.class);
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开地图显示位置
	 * 
	 * @param context
	 *            :Activity上下文
	 * @param longitude
	 *            :经度
	 * @param latitude
	 *            :纬度
	 * @param name
	 *            :地点名称
	 */
	public static int loadMap(Context context, String longitude,
			String latitude, String name) {

		if (strIsNullOrEmpty(longitude, latitude, name)) {
			return PARAMETER_ERROR;
		}
		longitude = longitude.trim();
		latitude = latitude.trim();
		name = name.trim();
		String uri = "geo:";
		uri = uri + longitude + "" + latitude + "?q=" + name;
		try {
			Uri mUri = Uri.parse(uri);
			Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
			((Activity) context).startActivity(intent);

		} catch (ActivityNotFoundException e) {
			return NO_MAP;
		} catch (Exception e) {
			e.printStackTrace();
			return OTHER_ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 字符串空判断
	 * 
	 * @param str
	 * @return boolean：str为空或者空字符串则返回true
	 */
	public static boolean strIsNullOrEmpty(String... str) {
		for (String s : str) {
			if (null == s || s.equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 调用打电话接口
	 * 
	 * @param con
	 *            ：activity上下文
	 * @param number
	 *            ：电话号码
	 */
	public static void doCall(Context context, String number) {
		if (null == context || strIsNullOrEmpty(number)) {
			return;
		}
		String numberStr = "tel:" + number.trim();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse(numberStr));
		((Activity) context).startActivity(intent);
	}
	/**
	 * 判断字符串是否含有特殊字符
	 * 
	 * @param str
	 * @return 如果含有特殊字符则返回true反之返回false
	 */
	public static boolean isMatch(String str) {
		boolean ret = false;
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		ret = m.find();
		return ret;
	}

	public static void openMainView(Context context) {
		Intent intent = new Intent("android.intent.action.main");
		((Activity) context).startActivity(intent);
	}

	public static void openMainView(Context context,
			int storeId) {
		Intent intent = new Intent("android.intent.action.main");
		intent.putExtra("storeId", storeId);
		((Activity) context).startActivity(intent);
	}

	public static void openMainView(Context context, int storeId,
			String businessName) {
		Intent intent = new Intent("android.intent.action.main");
		intent.putExtra("storeId", storeId);
		intent.putExtra("businessName", businessName);
		((Activity) context).startActivity(intent);
		
	}
}
