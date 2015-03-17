package com.daixiaobao.other;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.daixiaobao.proxy.list.ProxyListFragment;
import com.daixiaobao.version.RequestVreiosn;
import com.daixiaobao.version.ResponseVersion;
import com.daixiaobao.version.VersionProtocol;
import com.daixiaobao.widget.ConformDialog;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class VersionHelper {

	private static String SAVENAME;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				ResponseVersion obj = null;
				if(msg.obj == null){
					
				}else {
					
					obj = (ResponseVersion)msg.obj;
					if(obj.getUpdate().equals("1")){//有更新
						String url = obj.getUrl();
						conformUpdate(url);
					} else {
						Toast.makeText(context, "现在是最新版本了", Toast.LENGTH_SHORT).show();
					}
				}
				break;

			default:
				break;
			}
		};
	};
	private Context context;
	public static final String REGULAR_EXPRESSION = "§";
	private static final String ANDROID = "0";
	public void check(Context context) {
		// TODO Auto-generated method stub
		this.context = context;
		String[] versionCodeAndName = getLocalVersions(
				context).split("§");
		if (versionCodeAndName.length > 0) {
			VersionProtocol protocol = new VersionProtocol();
			protocol.invoke(new RequestVreiosn(versionCodeAndName[0], LoginMessageDataUtils.getUID(context), 
					DeviceTool.getUniqueId(context), ANDROID), handler);
		}
	}

	
	/**
	 * 获取本地然间版本信息 返回格式为 versionCode + "&" + versionName
	 * 
	 * @return
	 */
	public static String getLocalVersions(Context context) {
		PackageManager manager = context.getPackageManager();
		StringBuilder sb = new StringBuilder();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(context),
					0);
			// 版本号
			sb.append(info.versionCode);
			sb.append(REGULAR_EXPRESSION);
			// 版本名
			sb.append(info.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public static String getPackageName(Context context) {
		return context.getPackageName();
	}
	
	private void conformUpdate(final String url) {
		Dialog dialog = null;
		ConformDialog.Builder customBuilder = new ConformDialog.Builder(
				context);
		customBuilder.setTitle("更新提示").setMessage("有新版本，是否更新?")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 退出dialog
						dialog.dismiss();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case Dialog.BUTTON_NEGATIVE:
							break;
						case Dialog.BUTTON_POSITIVE:
							download(url);
							break;
						default:
							break;
						}
						dialog.dismiss();
					}

				});
		dialog = customBuilder.create();
		dialog.show();
	}
	/**
	 * 获取文件名
	 */
	public static  String getFileName(String downloadUrl) {
		String filename = downloadUrl.substring(downloadUrl
				.lastIndexOf('/') + 1);
		SAVENAME = filename;
		return filename;
	}
	
	@SuppressLint("NewApi")
	private void download(final String url) {

		// 是否存在
		String fileName = getFileName(url);
		String path = Environment.getExternalStorageDirectory() + "/"
				+ Environment.DIRECTORY_DOWNLOADS + "/" + fileName;
		File file = new File(path);
		if (file.exists()) {
			Uri uri = Uri.fromFile(file);
			// 创建Intent意图
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 设置Uri和类型
			installIntent.setDataAndType(uri,
					"application/vnd.android.package-archive");
			context.startActivity(installIntent);
			return;
		}

		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.GINGERBREAD) {
			// 2.3以下的使用浏览器跳转
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(url);
			intent.setData(content_url);
			context.startActivity(intent);
		} else {
			// 使用内部下载器
			DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			DownloadManager.Request request = new DownloadManager.Request(
					Uri.parse(url));
			// 设置允许使用的网络类型，这里是移动网络和wifi都可以
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
					| DownloadManager.Request.NETWORK_WIFI);

			// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
			// request.setShowRunningNotification(false);

			// 不显示下载界面
			request.setVisibleInDownloadsUi(true);
			// 文件将存放在外部存储的确实download文件内，如果无此文件夹，创建之，如果有，下面将返回false。不同的手机不同Android版本的SD卡的挂载点可能会不一样，因此通过系统方式获取。
			Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DOWNLOADS).mkdir();
			request.setDestinationInExternalPublicDir(
					Environment.DIRECTORY_DOWNLOADS, fileName);
			// request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
			// 有的系统不支持此方法
			long id = downloadManager.enqueue(request);
			// TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
			SharedPreferences sp = context.getSharedPreferences("downloadId", 0);
			SharedPreferences.Editor editor = sp.edit();
			editor.putLong("downloadId", id);
			editor.commit();
		}

	}
}
