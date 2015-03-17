package com.daixiaobao.other;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.daixiaobao.changePrice.ChangePriceBean;
import com.daixiaobao.changePrice.ChangePriceProtocol;
import com.daixiaobao.concern.change.RequestConcernChange;
import com.daixiaobao.concern.change.ResponseConcernChange;
import com.daixiaobao.other.ConcernHelper.OnConCernChangeHandleListener;
import com.daixiaobao.widget.ChangePriceConformDialog;
import com.daixiaobao.widget.ConformDialog;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class ChangePriceHelper {

	public static void changePrice(final Context context, final String[] ids, final Handler handler, String price) {
		Dialog dialog = null;
		final ChangePriceConformDialog.Builder customBuilder = new ChangePriceConformDialog.Builder(
				context);
		customBuilder.setMessage("请输入金额")
		.setOriPrice(price)
		.setPositiveButton("修改价格", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case Dialog.BUTTON_NEGATIVE:
							break;
						case Dialog.BUTTON_POSITIVE:
							String transformType = customBuilder.getTransformType();
							String price = customBuilder.getPrice();
							peotocol(context, ids, transformType,price, handler);
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
	
	public static void peotocol(Context context, String[] productIds, String transformType, String price, Handler handler){
		ChangePriceProtocol protocol = new ChangePriceProtocol();
		protocol.invoke(new ChangePriceBean(LoginMessageDataUtils.getToken(context),
				LoginMessageDataUtils.getUID(context), DeviceTool.getUniqueId(context), 
				null, productIds, transformType, price), handler);
	}
	
	
	public static void collectAndChangePrice(final Context context, final String[] ids, final Handler handler, String price) {
		Dialog dialog = null;
		final ChangePriceConformDialog.Builder customBuilder = new ChangePriceConformDialog.Builder(
				context);
		customBuilder.setMessage("请输入新的价格(元)")
		.setOriPrice(price)
		.setPositiveButton("收藏到我的店铺", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case Dialog.BUTTON_NEGATIVE:
							break;
						case Dialog.BUTTON_POSITIVE:
							String transformType = customBuilder.getTransformType();
							String price = customBuilder.getPrice();
							ConcernHelper newInstance = ConcernHelper.newInstance(context, new OnConCernChangeHandleListener() {
								
								@Override
								public void onHandle(ResponseConcernChange obj) {
									// TODO Auto-generated method stub
									Message msg = Message.obtain();
									msg.obj = obj;
									handler.sendMessage(msg);
								}
							});
							newInstance.invoke(new RequestConcernChange(LoginMessageDataUtils.getToken(context),
									LoginMessageDataUtils.getUID(context),
									DeviceTool.getUniqueId(context), 
									ids[0], price));
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
}
