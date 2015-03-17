package com.wookii.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Handler;

public class CommonUtil {
	private static CommonUtil cu = null;

	private CommonUtil() {
	}

	public static synchronized CommonUtil getInstance() {
		if (cu == null) {
			cu = new CommonUtil();
		}
		return cu;
	}

	private Handler handler;

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
	 * 判断是否中国电信号码
	 * 
	 * @param num
	 *            ：电话号码
	 * @return true：中国电信号码
	 */
	public static boolean isChinaTelecomNum(String num) {
		// 电信：133、153、180、189、（1349卫通）
		if (strIsNullOrEmpty(num)) {
			return false;
		}
		num = num.trim();
		if (num.startsWith("133") || num.startsWith("153")
				|| num.startsWith("180") || num.startsWith("189")
				|| num.startsWith("1349"))
			return true;
		return false;
	}

	/**
	 * 判断字符串是否含有特殊字符
	 * 
	 * @param str
	 * @return 如果含有特殊字符则返回true反之返回false
	 */
	public static boolean isMatch(String str) {
		boolean ret = false;
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		ret = m.find();
		return ret;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
