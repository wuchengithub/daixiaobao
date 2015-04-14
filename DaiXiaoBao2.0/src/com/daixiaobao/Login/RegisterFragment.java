package com.daixiaobao.Login;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wookii.ResourcesIdFactoryUtils;
import com.wookii.model.RegisterRequest;
import com.wookii.protocol.RegisiterProtocol;
import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.CountDownTimerUtil;
import com.wookii.utils.DeviceTool;

public class RegisterFragment extends Fragment {
	public final static int UserRegisterTel_RESULT_CODE = 2;
	public static final String YANGZHENGMA = "YZM";
	protected static final int REGISTER_DEVICE = 0x123;
	private static final int PIC_VIEW = 0x124;
	private LoginAndRegActivity mParentActivity;

	private EditText mUserNameET, mTokenET, mPasswordET, mConfimPwdET;
	private Button mButtonSubmit; 
	private Button mButtonGetToken;
	private CheckBox user_agreement_check_box;
	private TextView user_agreement, user_agree;
	private String userName;
	private String toastInfo;
	private ProgressDialog pd;
	private String LOGIN_FROM_CP = "0";
	ProtocolManager dataManager;
	private ImageView yanZhengMaView;

	private CountDownTimerUtil countDownTimer;

	public static RegisterFragment newInstance() {
		return new RegisterFragment();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (pd != null) {
				if (pd.isShowing()) {
					pd.dismiss();
				}
			}
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				toastInfo = msg.obj.toString();

				Intent intent = new Intent();
				intent.putExtra("JSON", toastInfo);
				// mParentActivity.setResult(UserRegisterTel_RESULT_CODE,
				// intent);
				mParentActivity.showToast(toastInfo);
				//切换到登陆界面
				mParentActivity.changeCurrentViewPagerItem(0);
			/*	dataManager = new ProtocolManager(mParentActivity, mHandler);
				String deviceId = DeviceTool.getUniqueId(mParentActivity);
				String userName = mUserNameET.getText().toString();
				String pwd = mPasswordET.getText().toString();
				// first_login 可选 0：非首次登录 1：首次登录 默认值为 0
				dataManager.login(userName, pwd, deviceId, "1", "",
						LOGIN_FROM_CP);*/
				// mParentActivity.finish();
				break;
			/*case ProtocolManager.LOGIN_SUCCESS:
				UserInfo userInfo = (UserInfo) msg.obj;
				LoginMessageDataUtils.insertUID(mParentActivity,
						userInfo.getUid());
				LoginMessageDataUtils.inserttelNum(mParentActivity,
						mUserNameET.getText().toString());
				LoginMessageDataUtils.insertToken(mParentActivity,
						userInfo.getToken());
				LoginMessageDataUtils.insertNickName(mParentActivity,
						userInfo.getNickName());
				Intent intent4Broadcast = new Intent();
				intent4Broadcast.setAction("com.cloudpoint.taskReceiver");
				// 十一活动注册成功直接登录并打开我的礼包详情
				// intent4Broadcast.putExtra("guide", "guide");
				mParentActivity.sendBroadcast(intent4Broadcast);
				mParentActivity.finish();
				break;*/
			case ProtocolManager.NETWORK_ERROR:
				break;

			case ProtocolManager.TIMEOUT_ERROR:
				mParentActivity.showToast("我需要休息一会！");
				break;
			case ProtocolManager.NOTIFICATION_RESPONSE_ERRORINFO:
				toastInfo = msg.obj.toString();
				mParentActivity.showToast(toastInfo);
				break;
			case ProtocolManager.NOTIFICATION_SUBMIT_GETCODE:
				
				toastInfo = msg.obj.toString();
				// userRegisterTelActivity.insertYZM_CODE(toastInfo);
				mParentActivity.showToast("验证码已经发至您的手机，请查收。");
				// 倒计时
				if (null == countDownTimer) {
					countDownTimer = new CountDownTimerUtil(mHandler,
							60 * 1000, 1000);
				}
				countDownTimer.start();
				break;
			case CountDownTimerUtil.TIMER_FINISH:
				mButtonGetToken.setEnabled(true);
				//mButtonGetToken.setText("获取验证码");
				break;
			case CountDownTimerUtil.TIMER_TICK:
				mButtonGetToken.setEnabled(false);
				//mButtonGetToken.setText("获取验证码(" + msg.arg1 + ")");
				break;
			case PIC_VIEW:
				Bitmap decodeStream = BitmapFactory.decodeStream((InputStream) msg.obj);
				yanZhengMaView.setImageBitmap(decodeStream);
				mButtonGetToken.setVisibility(View.INVISIBLE);
				break;
			default:
				toastInfo = msg.obj.toString();
				mParentActivity.showToast(toastInfo);
				super.handleMessage(msg);
				break;
			}

		}
	};
	private ImageView yanZhengMaRef;
	private EditText phone_number;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mParentActivity = (LoginAndRegActivity) activity;
		dataManager = new ProtocolManager(mParentActivity, mHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				mParentActivity.getApplicationContext(), "cpsdk_register");
		View view = inflater.inflate(layoutId, container, false);

		mUserNameET = (EditText) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"input_username"));

		mTokenET = (EditText) view.findViewById(ResourcesIdFactoryUtils.getId(
				mParentActivity.getApplicationContext(), "input_token"));
		mPasswordET = (EditText) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"input_password"));
		mConfimPwdET = (EditText) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"confirm_password"));
		/**
		 * add 2.0 3 电话号，邀请码
		 */
		phone_number = (EditText) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"phone_number"));
		mButtonGetToken = (Button) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"get_token_button"));
		
		yanZhengMaView = (ImageView) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"yanzhengma_view"));
		//重新获取验证码
		yanZhengMaRef = (ImageView) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"yanzhenma_ref"));
		yanZhengMaRef.setOnClickListener(mOnGetTokenClickListener);
		
		mButtonGetToken.setOnClickListener(mOnGetTokenClickListener);

		mButtonSubmit = (Button) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"btnId_for_submit_login"));
		mButtonSubmit.setOnClickListener(mOnSubmitClickListener);

		user_agreement_check_box = (CheckBox) view
				.findViewById(ResourcesIdFactoryUtils.getId(
						mParentActivity.getApplicationContext(),
						"user_agreement_check_box"));
		user_agree = (TextView) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(), "user_agree"));
		user_agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (user_agreement_check_box.isChecked()) {

					mButtonSubmit.setEnabled(true);
					mButtonGetToken.setEnabled(true);
					user_agreement_check_box.setChecked(false);
				} else {
					mButtonSubmit.setEnabled(false);
					mButtonGetToken.setEnabled(false);
					user_agreement_check_box.setChecked(true);
				}
			}
		});
		user_agreement_check_box
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							mButtonSubmit.setEnabled(true);
							mButtonGetToken.setEnabled(true);
						} else {
							mButtonSubmit.setEnabled(false);
							mButtonGetToken.setEnabled(false);
						}
					}
				});
		user_agreement = (TextView) view.findViewById(ResourcesIdFactoryUtils
				.getId(mParentActivity.getApplicationContext(),
						"user_agreement"));
		user_agreement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri
						.parse("http://api.cpo2o.com/v1/API/word/copyright.html");
				intent.setData(content_url);
				// intent.setClassName("com.android.browser",
				// "com.android.browser.BrowserActivity");
				startActivity(intent);
			}
		});
		return view;
	}

	/**
	 * 获取验证码按钮监听
	 */
	private OnClickListener mOnGetTokenClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
				if (pd == null) {
					pd = ProgressDialog.show(mParentActivity, "",
							"正在获取验证码...", true, true);
				} else {
					pd.show();
				}
				new Thread(){
					

					private ArrayList<NameValuePair> params;

					public void run() {
						HttpPost httpPost = new HttpPost(BaseProtocol.HTTP_REQUEST_URL + "kaptcha.htm");
						String encode = "UTF-8";
						String result = null;
						int ConnectionTimeout = 1000 * 10;
						int SoTimeout = 1000 * 10;
						int SocketBufferSize = 10240;
						HttpResponse httpResponse = null;
						String json = "{\"deviceId\":\""+ DeviceTool.getUniqueId(mParentActivity.getApplicationContext()) + "\"}";
						try {
							httpPost.addHeader("Content-Type", "application/json");
							HttpParams httpParams = new BasicHttpParams();
							//httpParams.setParameter("Content-type", "application/json");
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
							
							httpPost.setEntity(new StringEntity(json));
							httpPost.setParams(httpParams);
							httpResponse = new DefaultHttpClient().execute(httpPost);

							int statuScode = httpResponse.getStatusLine().getStatusCode();
							if (statuScode == 200) {
								
								Message msg = Message.obtain();
								msg.what = PIC_VIEW;
								msg.obj = httpResponse.getEntity().getContent();
								mHandler.sendMessage(msg);
							}
						} catch (ClientProtocolException e) {
							e.printStackTrace();
							Message msg = Message.obtain();
							msg.what = ProtocolManager.TIMEOUT_ERROR;
							mHandler.sendMessage(msg);
						} catch (IOException e) {
							e.printStackTrace();
							Message msg = Message.obtain();
							msg.what = ProtocolManager.TIMEOUT_ERROR;
							mHandler.sendMessage(msg);
						} 
					};
					
				}.start();
			}

	};

	private OnClickListener mOnSubmitClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			TelephonyManager tm = (TelephonyManager) mParentActivity
					.getSystemService(Context.TELEPHONY_SERVICE);
			String userNameString = mUserNameET.getText().toString();
			String YZMCode = mTokenET.getText().toString();
			String pwd = mPasswordET.getText().toString();
			String pwd2 = mConfimPwdET.getText().toString();
			String phoneNumber = phone_number.getText().toString();
			if (TextUtils.isEmpty(userNameString)) {
				toastInfo = "用户名不能为空";
				Toast.makeText(mParentActivity, toastInfo, Toast.LENGTH_LONG)
						.show();
			}  /*else if (!checkMobile(phoneNumber)) {
				toastInfo = "手机号格式错误";
				Toast.makeText(mParentActivity, toastInfo, Toast.LENGTH_LONG)
						.show();
			}*/ else if (TextUtils.isEmpty(pwd)) {
				toastInfo = "密码不能为空";
				Toast.makeText(mParentActivity, toastInfo, Toast.LENGTH_LONG)
						.show();
			} else if (6 > pwd.length() || 11 < pwd.length()) {
				toastInfo = "密码不能小于6位或大于11位";
				Toast.makeText(mParentActivity, toastInfo, Toast.LENGTH_LONG)
						.show();
			} else if (TextUtils.isEmpty(pwd2)) {
				toastInfo = "确认密码不能为空";
				Toast.makeText(mParentActivity, toastInfo, Toast.LENGTH_LONG)
						.show();
			} else if (6 > pwd2.length() || 11 < pwd2.length()) {
				toastInfo = "确认密码不能小于6位或大于11位";
				Toast.makeText(mParentActivity, toastInfo, Toast.LENGTH_LONG)
						.show();
			} else if (!pwd.equals(pwd2)) {
				toastInfo = "两次输入的密码不一致，请检查";
				Toast.makeText(mParentActivity, toastInfo, Toast.LENGTH_LONG)
						.show();
			} else {
				//访问服务器祖册
				String deviceId = DeviceTool.getUniqueId(mParentActivity);
				RegisterRequest rr = new RegisterRequest(deviceId, userNameString, pwd, YZMCode, pwd2, phoneNumber, "");
				RegisiterProtocol regisiterProtocol = new RegisiterProtocol();
				regisiterProtocol.invokeRegister(rr, mHandler);
				pd = ProgressDialog.show(mParentActivity, "", "注册中...", true,
						true);
			}
		}
	};

	public void insertYZM_CODE(String codeId) {
		SharedPreferences sp = mParentActivity.getSharedPreferences(
				YANGZHENGMA, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("codeId", codeId);
		editor.commit();
	}

	public String getYZM_CODE() {
		SharedPreferences sp = mParentActivity.getSharedPreferences(
				YANGZHENGMA, 0);
		String code = sp.getString("codeId", null);
		return code;
	}

	public void deleteYZM_CODE() {
		SharedPreferences sp = mParentActivity.getSharedPreferences(
				YANGZHENGMA, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	public static boolean checkMobile(String mobile) {  
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";  
        return Pattern.matches(regex,mobile);  
    }
	
}
