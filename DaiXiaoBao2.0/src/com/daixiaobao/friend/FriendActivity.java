package com.daixiaobao.friend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.daixiaobao.CommonUtil;
import com.daixiaobao.R;
import com.daixiaobao.friend.ResponseBusinessList.Data;
import com.daixiaobao.widget.ConformDialog;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.example.testpic.PublishedActivity;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.umeng.update.UmengUpdateAgent;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class FriendActivity extends SherlockFragmentActivity{

	
	private SwipeListView listView;
	protected int storeId;
	protected BusinessAdapter businessAdapter;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_friend);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		listView = (SwipeListView)findViewById(R.id.friend_supply_list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				storeId = (int) arg0.getAdapter().getItemId(arg2);
				String businessName = businessAdapter.getBusinessName(arg2);
				CommonUtil.openMainView(FriendActivity.this, storeId, businessName);
			}
		});
		listView.setSwipeListViewListener(new BaseSwipeListViewListener(){
			@Override
			public void onClickFrontView(int position) {
				storeId = (int) listView.getAdapter().getItemId(position);
				String businessName = businessAdapter.getBusinessName(position);
				CommonUtil.openMainView(FriendActivity.this, storeId, businessName);
			}
		});
		SearchView searchView = (SearchView)findViewById(R.id.friend_search_supply);
		int palteId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
		View palteView = searchView.findViewById(palteId);
		if(palteView != null) {
			palteView.setBackgroundResource(R.color.WHITE);
		}
	}
	
	@Override
	protected void onResume() {
		getDataFromServer();
		super.onResume();
	}
	public  void getDataFromServer() {
		CustomLoadingDialog.showProgress(this, "", "读取列表中", false, true);
		RequestBusinessList requesBusinessList  = 
				new RequestBusinessList(LoginMessageDataUtils.getToken(this),
				DeviceTool.getUniqueId(this),LoginMessageDataUtils.getUID(this), "0" ,"10");
		BusinessListProtocol protocol = new BusinessListProtocol();
		protocol.invoke(requesBusinessList, new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.obj != null) {
					ResponseBusinessList obj = (ResponseBusinessList)msg.obj;
					if(obj.getErrorCode() == 0){
						Data[] datas = obj.getDatas();
						if(datas != null) {
							ArrayList<Data> data = toList(datas);
							//businessAdapter = new BusinessAdapter(FriendActivity.this, data);
							listView.setAdapter(businessAdapter);
						}
					} else  if (obj.getErrorCode() == 2) {
						LoginMessageDataUtils.insertToken(FriendActivity.this, null);
						CommonUtil.openLoginView(FriendActivity.this, CommonUtil.LOGIN_REQUEST_CODE);
					}	else  if (obj.getErrorCode() == 1) {//token失效
						LoginMessageDataUtils.insertToken(FriendActivity.this, null);
						CommonUtil.openLoginView(FriendActivity.this, CommonUtil.LOGIN_REQUEST_CODE);
					}				
				}
				CustomLoadingDialog.dismissDialog();
				
			}
		});
	}
	
	protected ArrayList<Data> toList(Data[] datas) {
		ArrayList<Data> list = new ArrayList<Data>();
		for (Data data : datas) {
			list.add(data);
		}
		return list;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0, "上传")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add(0, 1, 0,"添加好友")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add(0, 2, 0,"申请列表")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add(0, 3, 0,"我的店铺")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		/*menu.add(0, 4, 0,"资源导航")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		menu.add(0, 5, 0,"货源查询")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);*/
		menu.add(0, 6, 0,"设置中心")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(FriendActivity.this, PublishedActivity.class);
			startActivity(intent);
			break;
		case 1:
			Intent Supplier = new Intent(FriendActivity.this, SupplierActivity.class);
			startActivity(Supplier);
			break;
		case 2:
			Intent apply = new Intent(FriendActivity.this, ApplyList.class);
			startActivity(apply);
			break;
		case 3:
			CommonUtil.openMainView(FriendActivity.this, Integer.parseInt(LoginMessageDataUtils.getStoreId(this)), LoginMessageDataUtils.getUID(this));
			break;
		/*case 4:
			Intent navigation = new Intent(FriendActivity.this, Navigation.class);
			startActivity(navigation);
			break;
		case 5:
			CommonUtil.openMainView(FriendActivity.this, Integer.parseInt("1"), "");
			break;*/
		case 6:
			Intent settings = new Intent(FriendActivity.this, MoreActivity.class);
			startActivity(settings);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Dialog dialog = null;
			ConformDialog.Builder customBuilder = new ConformDialog.Builder(
					FriendActivity.this);
			customBuilder.setTitle("提示").setMessage("是否要退出代销宝?")
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							finish();
							dialog.dismiss();
						}

					});
			dialog = customBuilder.create();
			dialog.show();
			return true;
		} else {
			return false;
		}
	}
}
