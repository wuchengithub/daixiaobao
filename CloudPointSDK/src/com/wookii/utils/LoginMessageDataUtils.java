package com.wookii.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.wookii.LoginAndRegActivity;
import com.wookii.protocollManager.ProtocolManager;

/**
 * 
 * @author wuchen
 * 
 */
public class LoginMessageDataUtils {

	public static String getUID(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.UId, 0);
		String uid = sp.getString("uid", null);
		return uid;
	}

	public static void insertUID(Context context, String uid) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.UId, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("uid", uid);
		editor.commit();
	}

	public static void deleteUID(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.UId, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	public static void inserttelNum(Context context, String telNum) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.telNum, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("telNum", telNum);
		editor.commit();
	}

	public static String getTelNum(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.telNum, 0);
		String telNum = sp.getString("telNum", null);
		return telNum;
	}

	public static void insertToken(Context context, String token) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.token, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("token", token);
		editor.commit();
	}

	public static String getToken(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.token, 0);
		String token = sp.getString("token", null);
		return token;
	}

	public static void insertNickName(Context context, String nickName) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.nickName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("nickName", nickName);
		editor.commit();
	}

	public static String getNickName(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.nickName, 0);
		String nickName = sp.getString("nickName", null);
		return nickName;
	}
	/**
	 * 临时token
	 * @param context
	 * @param token
	 */
	public static void insertInterimToken(Context context, boolean bool) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.interimToken, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("interimToken", bool);
		editor.commit();
	}

	public static boolean getInterimToken(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.interimToken, 0);
		boolean interimToken = sp.getBoolean("interimToken", false);
		return interimToken;
	}

	public static void insertStoreId(Activity context,
			String storeId) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.storeId, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("storeId", storeId);
		editor.commit();
	}

	public static String getStoreId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.storeId, 0);
		String nickName = sp.getString("storeId", null);
		return nickName;
	}

	public static void setIsVip(Context context,
			String isVip) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.isVip, 0);
		SharedPreferences.Editor editor = sp.edit();
		boolean b = false;
		if("N".equals(isVip)){
			b = false;
		} else {
			b = true;
		}
		editor.putBoolean("isVip", b);
		editor.commit();
		
	}
	
	public static boolean getIsVip(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ProtocolManager.isVip, 0);
		boolean b = sp.getBoolean("isVip", false);
		return b;
	}

	public static void fristInit(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				"initApp", 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("frist", true);
		editor.commit();
	}
	public static boolean getFristInit(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				"initApp", 0);
		return sp.getBoolean("frist", false);
	}
}
