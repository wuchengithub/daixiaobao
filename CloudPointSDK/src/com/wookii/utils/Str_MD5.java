package com.wookii.utils;

import java.security.MessageDigest;

public class Str_MD5 {

	public final static int YIWUQILIU = 1576;

	public static String tokenToMD5(String uid, String token, String otherCode) {
		String tokenToMD5 = "";

		int uidInt = Integer.parseInt(uid);

		int result = uidInt + YIWUQILIU;
		String resultStr = result + token + otherCode + "";
		tokenToMD5 = MD5(resultStr);
		return tokenToMD5;
	}

	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	// ����ļ����㷨
	public static String encryptmd5(String str) {
		char[] a = str.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'l');
		}
		String s = new String(a);
		return s;
	}
}
