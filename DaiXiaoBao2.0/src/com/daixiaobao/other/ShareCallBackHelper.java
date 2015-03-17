package com.daixiaobao.other;

import com.daixiaobao.concern.change.ConcernChangeProtocol;
import com.daixiaobao.concern.change.ResponseConcernChange;
import com.daixiaobao.concern.change.ShareToCallBackProtocol;
import com.wookii.protocollManager.ProtocolManager;

import android.content.Context;
import android.os.Handler;

public class ShareCallBackHelper {

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
	private OnShareCallBackListener listener;

	private ShareCallBackHelper(Context context,
			OnShareCallBackListener listener) {
		this.context = context;
		this.listener = listener;
		this.protocol = new ShareToCallBackProtocol();
	}

	private static ShareCallBackHelper instance = null;
	private ShareToCallBackProtocol protocol;

	public synchronized static ShareCallBackHelper newInstance(Context context,
			OnShareCallBackListener listener) {
		instance = new ShareCallBackHelper(context, listener);
		return instance;
	}

	public void invoke(Object model) {
		protocol.invoke(model, handler);
	}

	public interface OnShareCallBackListener {
		public void onHandle(ResponseConcernChange obj);
	}
}
