package com.daixiaobao.other;

import com.daixiaobao.categroy.CategroyProtocol;
import com.daixiaobao.categroy.ResponseCatagroy;
import com.daixiaobao.concern.change.ConcernChangeProtocol;
import com.daixiaobao.concern.change.ResponseConcernChange;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.wookii.protocollManager.ProtocolManager;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class CatagroyHelper {

	private Context context;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ResponseCatagroy obj = new ResponseCatagroy();
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				if (msg.obj == null) {// 演示数据
					Toast.makeText(context, "没有找到您想要的内容", Toast.LENGTH_LONG).show();
					obj = new ResponseCatagroy();
					obj.setGroup(new ResponseCatagroy.Group[]{});
				} else {
					obj = (ResponseCatagroy) msg.obj;
					if(obj.getErrorCode() != ProtocolManager.ERROR_CODE_ZORE){
						Toast.makeText(context, obj.getMessage() + "", Toast.LENGTH_LONG).show();
						CustomLoadingDialog.dismissDialog();
						return;
					}
				}
				break;

			default:
				break;
			}
			listener.onHandle(obj);
		};
	};
	private OnCategroyHandleListener listener;

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
		protocol.invoke(model, handler);
	}

	public interface OnCategroyHandleListener {
		public void onHandle(ResponseCatagroy obj);
	}
}
