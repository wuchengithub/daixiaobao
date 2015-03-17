package com.daixiaobao.friend;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.daixiaobao.CommonUtil;
import com.daixiaobao.R;
import com.daixiaobao.friend.ResponseBusinessList.Data;
import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.proxy.ProxyProductActivity;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class FriendFragment extends SherlockFragment {

	private SwipeListView listView;
	protected int storeId;
	protected BusinessAdapter businessAdapter;
	private Activity context;
	private View friendView;

	@Override
	public void onAttach(Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_friend, null);
		listView = (SwipeListView) view.findViewById(R.id.friend_supply_list);
		listView.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onClickFrontView(int position) {
				storeId = (int) listView.getAdapter().getItemId(position);
				String businessName = businessAdapter.getBusinessName(position);
				Intent intent = new Intent(context, ProxyProductActivity.class);
				intent.putExtra("storeId", storeId);
				intent.putExtra("businessName", businessName);
				((Activity) context).startActivity(intent);
			}
		});
		SearchView searchView = (SearchView) view
				.findViewById(R.id.friend_search_supply);
		int palteId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		View palteView = searchView.findViewById(palteId);
		if (palteView != null) {
			palteView.setBackgroundResource(R.color.WHITE);
		}
		view.findViewById(R.id.friend_add_friend).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent Supplier = new Intent(context,
								SupplierActivity.class);
						startActivity(Supplier);
					}
				});
		friendView = view.findViewById(R.id.friend_find_new_friend);
		friendView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ApplyList.class);
				context.startActivity(intent);
			}
		});
		
		return view;
	}

	
	
	protected void findFriend() {
		// TODO Auto-generated method stub
		ApplyProtocol protocol = new ApplyProtocol();
		protocol.invoke(new BeanApplyList(LoginMessageDataUtils.getToken(context), 
				DeviceTool.getUniqueId(context)), new Handler(){
			

			@Override
			public void handleMessage(Message msg) {
				if(msg.obj != null) {
					BeanApplyList obj = (BeanApplyList)msg.obj;
					if(obj.getErrorCode() == 0){
						if(obj.getGroup().size() > 0) {
							friendView.setVisibility(View.VISIBLE);
						} else {
							friendView.setVisibility(View.GONE);
						}
						
					} else  if (obj.getErrorCode() == 2) {
					}
					Toast.makeText(context, obj.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	class ApplyProtocol extends MyBaseProtocol{

		@Override
		protected Class getClazz() {
			// TODO Auto-generated method stub
			return BeanApplyList.class;
		}

		@Override
		protected String getMethodName() {
			// TODO Auto-generated method stub
			return "/admin/seller/appls.htm";
		}
		
	}
	
	@Override
	public void onResume() {
		getDataFromServer();
		findFriend();
		super.onResume();
	}

	public void getDataFromServer() {
		CustomLoadingDialog.showProgress(context, "", "读取列表中", false, true);
		RequestBusinessList requesBusinessList = new RequestBusinessList(
				LoginMessageDataUtils.getToken(context),
				DeviceTool.getUniqueId(context),
				LoginMessageDataUtils.getUID(context), "0", "10");
		BusinessListProtocol protocol = new BusinessListProtocol();
		protocol.invoke(requesBusinessList, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					ResponseBusinessList obj = (ResponseBusinessList) msg.obj;
					if (obj.getErrorCode() == 0) {
						Data[] datas = obj.getDatas();
						if (datas != null) {
							ArrayList<Data> data = toList(datas);
							businessAdapter = new BusinessAdapter(
									FriendFragment.this, data);
							listView.setAdapter(businessAdapter);
						}
					} else if (obj.getErrorCode() == 2) {
						LoginMessageDataUtils.insertToken(context, null);
						CommonUtil.openLoginView(context,
								CommonUtil.LOGIN_REQUEST_CODE);
					} else if (obj.getErrorCode() == 1) {// token失效
						LoginMessageDataUtils.insertToken(context, null);
						CommonUtil.openLoginView(context,
								CommonUtil.LOGIN_REQUEST_CODE);
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

}
