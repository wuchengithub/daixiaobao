package com.daixiaobao;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class BaseApplication extends Application{
	public static final String WX_APP_ID = "wxd4d7146476f8dd43";
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions options= new DisplayImageOptions.Builder()
	/*.showImageForEmptyUri(R.drawable.default_icon)
	.showImageOnFail(R.drawable.default_icon)*/
	.showImageOnLoading(android.R.drawable.ic_menu_crop)
	.cacheInMemory(false)
	.cacheOnDisc(true)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.build();
	private SharedPreferences mPrefs;
	private static IWXAPI api;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//错误日志
		//CrashHandler.getInstance().init(getApplicationContext());
		ImageLoaderConfiguration createDefault = ImageLoaderConfiguration.createDefault(getApplicationContext());
		Builder builder = new ImageLoaderConfiguration.Builder(getApplicationContext());
		builder.threadPoolSize(9);
		ImageLoaderConfiguration build = builder.build();
		imageLoader.init(createDefault);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
    	api = WXAPIFactory.createWXAPI(this, BaseApplication.WX_APP_ID, false);
    	// 将该app注册到微信
	    api.registerApp(BaseApplication.WX_APP_ID); 
		super.onCreate();
	}
	
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams(); 

	public WindowManager.LayoutParams getWindowParams() {
		return windowParams;
	}
	
}
