package com.daixiaobao.other;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.daixiaobao.categroy.CategroyProtocol;
import com.daixiaobao.categroy.ResponseCatagroy;
import com.daixiaobao.db.DBHelper;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.LoginMessageDataUtils;

public class CatagroyHelper {

	protected static final int LOCAL = 0;
	private Context context;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ResponseCatagroy obj = new ResponseCatagroy();
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				if (msg.obj == null) {// 演示数据
					LoginMessageDataUtils.insertToken(context, null);
				} else {
					obj = (ResponseCatagroy) msg.obj;
					if(obj.getErrorCode() != ProtocolManager.ERROR_CODE_ZORE){
						return;
					}
				}
				break;
			case LOCAL:
				obj = (ResponseCatagroy) msg.obj;
				if(obj.getGroup().size() == 0 ) {
					protocol.invoke(model, handler);
					return;
				}
				break;
			default:
				break;
			}
			listener.onHandle(obj);
		};
	};
	private OnCategroyHandleListener listener;
	private Object model;

	private CatagroyHelper(Context context, OnCategroyHandleListener listener) {
		this.context = context;
		this.listener = listener;
	}

	private static CatagroyHelper instance = null;
	private static CategroyProtocol protocol;

	public synchronized static CatagroyHelper getInstance(Context context,
			OnCategroyHandleListener listener) {
		instance = new CatagroyHelper(context, listener);
		protocol = new CategroyProtocol();

		return instance;
	}

	public void invoke(Object model) {
		this.model = model;
		//从本地加载
		new Thread(){
			public void run() {
				ResponseCatagroy categroy = DBHelper.getInstance(context).getCategroy();
				Message msg = handler.obtainMessage();
				msg.obj = categroy;
				msg.what = LOCAL;
				handler.sendMessage(msg);
				
			};
		}.start();
	}

	public interface OnCategroyHandleListener {
		public void onHandle(ResponseCatagroy obj);
	}
}
