package com.daixiaobao.protocol;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wookii.protocollManager.BaseProtocol;
import com.wookii.protocollManager.DataProtocolInterface;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.protocollManager.RequestProtocolTask;

public abstract class MyBaseProtocol extends BaseProtocol implements
DataProtocolInterface{
	
	
	@Override
	public void onResposeProcotolData(Object object) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		Object json = null;
		try {
			json = gson.fromJson(String.valueOf(object), getClazz());
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(mHandler != null){
			Message msg = Message.obtain();
			msg.obj = json;
			msg.what = ProtocolManager.NOTIFICATION;
			mHandler.sendMessage(msg);
		}
	}
	
	protected abstract  Class getClazz();

	private Handler mHandler;

	protected abstract String getMethodName();
	
	public void invoke(Object model,Handler handler) {

		this.mHandler = handler;
		//model to json
		Gson gson = new Gson();
		String json = gson.toJson(model);
		RequestProtocolTask httpRequestTask = new RequestProtocolTask(
				getMethodName(), json, this);
		Thread thread = new Thread(httpRequestTask);
		thread.start();

	}

}
