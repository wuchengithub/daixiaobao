package com.daixiaobao.version;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

@SuppressLint("NewApi")
public class CompleteReceiver extends BroadcastReceiver {

	private DownloadManager downloadManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		if(action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE )) {
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);         
			SharedPreferences sp = context.getSharedPreferences(
					"downloadId", 0);
			long targetId = sp.getLong("downloadId", 0);
			//TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
			if(targetId != id) return;
			
			Query query = new Query();
			query.setFilterById(id);
			downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			Cursor cursor = downloadManager.query(query);
			
			int columnCount = cursor.getColumnCount();
			String filename = null;
			String localUri = null;
			//TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
			while(cursor.moveToNext()) {
				for (int j = 0; j < columnCount; j++) {
					String columnName = cursor.getColumnName(j);
					String string = cursor.getString(j);
					if(columnName.equals("local_filename")) {
						filename = string;
					} else if(columnName.equals("local_uri")){
						localUri = string;
					}
					
				}
			}
			cursor.close();
			//弹出安装
			// 创建URI
			File file = null;
			Uri uri = null;
			if(filename == null){
				uri = Uri.parse(localUri);
			} else {
				file = new File(filename);
				uri = Uri.fromFile(file);
			}
			// 创建Intent意图
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 设置Uri和类型
			installIntent.setDataAndType(uri,
					"application/vnd.android.package-archive");
			context.startActivity(installIntent);
		
		}
	}
}
