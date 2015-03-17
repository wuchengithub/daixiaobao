package com.daixiaobao.other;

import com.daixiaobao.concern.change.ConcernChangeProtocol;
import com.daixiaobao.concern.change.ResponseConcernChange;
import com.wookii.protocollManager.ProtocolManager;

import android.content.Context;
import android.os.Handler;

public class ConcernHelper {

	private Context context;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ResponseConcernChange obj = new ResponseConcernChange();
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				if (msg.obj == null) {// 演示数据
					obj.setErrorCode(0);
				} else {
					obj = (ResponseConcernChange) msg.obj;
				}
				break;

			default:
				break;
			}
			listener.onHandle(obj);
		};
	};
	private OnConCernChangeHandleListener listener;

	private ConcernHelper(Context context,
			OnConCernChangeHandleListener listener) {
		this.context = context;
		this.listener = listener;
		this.protocol = new ConcernChangeProtocol();
	}

	private static ConcernHelper instance = null;
	private ConcernChangeProtocol protocol;

	public synchronized static ConcernHelper newInstance(Context context,
			OnConCernChangeHandleListener listener) {
		instance = new ConcernHelper(context, listener);
		return instance;
	}

	public void invoke(Object model) {
		protocol.invoke(model, handler);
	}

	public interface OnConCernChangeHandleListener {
		public void onHandle(ResponseConcernChange obj);
	}
}
