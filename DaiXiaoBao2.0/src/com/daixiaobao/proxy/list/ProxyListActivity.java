package com.daixiaobao.proxy.list;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.daixiaobao.R;
import com.daixiaobao.adapter.ProxyListAdapter;
import com.daixiaobao.concern.change.RequestConcernChange;
import com.daixiaobao.concern.change.ResponseConcernChange;
import com.daixiaobao.filter.FilterController;
import com.daixiaobao.filter.SearchConfig;
import com.daixiaobao.other.ConcernHelper;
import com.daixiaobao.other.ConcernHelper.OnConCernChangeHandleListener;
import com.daixiaobao.other.Test;
import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.proxy.all.ProxyAllProtocol;
import com.daixiaobao.proxy.all.RequestProxyAllBean;
import com.daixiaobao.proxy.list.ResponseProxyList.Group;
import com.daixiaobao.search.SearchController;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

/**
 * @author Administrator
 *
 */
public class ProxyListActivity extends SherlockFragmentActivity {

	protected static final int DESC = 1;
	protected static final int ASC = 0;
	private ToggleButton toggle;
	private int order = ASC;
	private static final String count = "10";
	private static final String TAG = "ProxyListFragment";
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "onHandler");
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				ResponseProxyList obj = null;
				obj = (ResponseProxyList)msg.obj;
				if(obj != null && obj.getErrorCode() == ProtocolManager.ERROR_CODE_ZORE){
					ProxyListAdapter objectToAdapter = objectToAdapter(obj, ProxyListActivity.this);
					objectToAdapter.setOnProductCencernChangeListener(new OnProductConcernChangeListener(){

						@Override
						public void onConcernChange(String prodcutCode, int position, String status) {
							// TODO Auto-generated method stub
							//修改价格
							changeProductConcern(prodcutCode, position, status);
						}});
					
				} 
				Toast.makeText(ProxyListActivity.this, obj.getMessage(),
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			CustomLoadingDialog.dismissDialog();
			pullList.onRefreshComplete();
		};
	};
	private PullToRefreshGridView pullList;
	private SearchConfig config;
	private ProxyListAdapter adapter;
	private ArrayList<Group> data;
	private int index;
	private MenuItem populateItem;
	private int storeId;
	private String businessName;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		populateItem = menu.add(Menu.NONE, 1, 0, "一键代理");
        populateItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == 1){
			//一键代理
			CustomLoadingDialog.showProgress(this, null, "正在批量代理", false, false);
			MyBaseProtocol protocol = new ProxyAllProtocol();
			protocol.invoke(new RequestProxyAllBean(LoginMessageDataUtils.getToken(this), LoginMessageDataUtils.getUID(this), 
					DeviceTool.getUniqueId(this), 
					"0", getProxyId()), new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					CustomLoadingDialog.dismissDialog();
					switch (msg.what) {
					case ProtocolManager.NOTIFICATION:
						RequestProxyAllBean obj = null;
						obj = (RequestProxyAllBean)msg.obj;
						if(obj != null && obj.getErrorCode() == ProtocolManager.ERROR_CODE_ZORE){
							refreshData();
							Toast.makeText(ProxyListActivity.this, obj.getMessge(), Toast.LENGTH_SHORT).show();
						}
						break;
					default:
						break;
					}
					super.handleMessage(msg);
				}
			});
		}
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}
	public String[] getProxyId(){
		List<String> temp = new ArrayList<String>();
		for (Group item : data) {
			if(item.getStatus().equals("1")){
				temp.add(item.getCode());
			} else {
				continue;
			}
		}
		String[] array = new String[temp.size()];
		int i = 0;
		for (String code : temp) {
			array[i] = code;
			i ++;
		}
		return array;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}


	protected ProxyListAdapter objectToAdapter(ResponseProxyList obj,
			ProxyListActivity proxyListFragment) {
		// TODO Auto-generated method stub
		if(adapter == null){
			data = new ArrayList<Group>();
			Group[] group = obj.getGroup();
			if (group != null) {

				for (Group item : group) {
					if (item != null) {
						data.add(item);
					}

				}
			}
			adapter = new ProxyListAdapter(data, this);
			pullList.setAdapter(adapter);
		} else {
			Group[] group = obj.getGroup();
			if (group != null) {
				int i = data.size();
				for (Group item : group) {
					if(item != null){
						data.add(item);
						//同步数据
						//0为已代理，1为未代理
						boolean checked = false;
						if (item.getStatus().equals(ProxyListAdapter.STATUS_PROXY)) {
							checked = true;
						}
						ProxyListAdapter.checkedMap.put(i, checked);
						ProxyListAdapter.expandMap.put(i, View.GONE);
					}
					i++;
				}
			}
			adapter.notifyDataSetChanged();
		}
		return adapter;
	}

	public int getIndex() {
		// TODO Auto-generated method stub
		if(data == null){
			return 0;
		}
		if(data.size() == 0){
			return 0;
		}
		if(adapter == null){
			return 0;
		}
		index = data.size();
		return index;
	}

	public ArrayList<Group> getData() {
		// TODO Auto-generated method stub
		return data;
	}

	public void setIndexZero(int i) {
		// TODO Auto-generated method stub
		index = i;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.fragment_mine_concern);
		storeId = getIntent().getIntExtra("storeId", -1);
		businessName = getIntent().getStringExtra("businessName");
		this.config = (SearchConfig) getIntent().getSerializableExtra("config");
		pullList = (PullToRefreshGridView)findViewById(R.id.mine_concern_product_list);
		pullList.setOnRefreshListener(new OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				refreshData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				getDataFromServer();
			}

		} );
		pullList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				openItemDetail(arg2);
			}});
		((TextView)findViewById(R.id.custon_title)).setText(businessName);
		findViewById(R.id.custon_back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//没数据执行刷新，有数据
		getDataFromServer();
		super.onCreate(savedInstanceState);
	}


	protected void openItemDetail(int position) {
		// TODO Auto-generated method stub
		String code = getData().get(position).getCode();
		Intent intent = new Intent("android.intent.action.detail");
		intent.putExtra("code", code);
		startActivity(intent);
	}

	/**
	 * 排序列表
	 * @param isChecked
	 */
	protected void changeListOrder(boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			order = DESC;
		} else {
			order = ASC;
		}
		refreshData();
	}

	private void refreshData() {
		setIndexZero(0);
		release();
		getDataFromServer();
	}
	
	private void release() {
		// TODO Auto-generated method stub
		adapter = null;
	}


	/**
	 * 
	 * @param prodcutCode
	 * @param status 
	 */
	private void changeProductConcern(String prodcutCode, final int position, final String status) {
		CustomLoadingDialog.showProgress(this, "", "正在执行", false, true);
		// TODO Auto-generated method stub
		ConcernHelper instance = ConcernHelper.newInstance(this, new OnConCernChangeHandleListener() {
			
			@Override
			public void onHandle(ResponseConcernChange obj) {
				// TODO Auto-generated method stub
				CustomLoadingDialog.dismissDialog();
				//处理关注/取消
				if(obj.getErrorCode() == ProtocolManager.ERROR_CODE_ZORE){
					//成功
					boolean checked = false;
					if (status.equals(ProxyListAdapter.STATUS_PROXY)) {
						checked = true;
					}
//					Log.i(TAG, "position：" + position + " status:" + checked);
					//状态变换
					ProxyListAdapter.checkedMap.put(position, checked);
				}
			}
		});
		
		instance.invoke(new RequestConcernChange(LoginMessageDataUtils.getToken(this),
				LoginMessageDataUtils.getUID(this),
				DeviceTool.getUniqueId(this), prodcutCode, status));
		
	}
	private void getDataFromServer() {
		// TODO Auto-generated method stub
		CustomLoadingDialog.showProgress(this, "", "读取列表中", false, true);
		RequestProxyList requestMineConcern = 
				new RequestProxyList(LoginMessageDataUtils.getToken(this),
				LoginMessageDataUtils.getUID(this),
				DeviceTool.getUniqueId(this),String.valueOf(getIndex()), count, String.valueOf(order), config, String.valueOf(storeId));
		ProxyListProtocol protocol = new ProxyListProtocol();
		if(String.valueOf(storeId) .equals(LoginMessageDataUtils.getStoreId(this))){
			protocol.setMethodName("/admin/product/mylist.htm");
		}
		protocol.invoke(requestMineConcern, handler);
	}

	public interface OnProductConcernChangeListener{
		public void onConcernChange(String prodcutCode, int position, String status);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	


	
}
