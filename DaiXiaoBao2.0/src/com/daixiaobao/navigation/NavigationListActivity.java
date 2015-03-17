package com.daixiaobao.navigation;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.daixiaobao.R;
import com.daixiaobao.concern.change.RequestConcernChange;
import com.daixiaobao.concern.change.ResponseConcernChange;
import com.daixiaobao.filter.SearchConfig;
import com.daixiaobao.navigation.ResponseNavigationList.Group;
import com.daixiaobao.other.ConcernHelper;
import com.daixiaobao.other.ConcernHelper.OnConCernChangeHandleListener;
import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.proxy.all.RequestProxyAllBean;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class NavigationListActivity extends SherlockFragmentActivity {

	protected static final int DESC = 1;
	protected static final int ASC = 0;
	private ToggleButton toggle;
	private int order = ASC;
	private static final String count = "2";
	private static final String TAG = "ProxyListFragment";
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "onHandler");
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				ResponseNavigationList obj = null;
				obj = (ResponseNavigationList)msg.obj;
				if(obj != null && obj.getErrorCode() == ProtocolManager.ERROR_CODE_ZORE){
					NavigationListAdapter objectToAdapter = objectToAdapter(obj, NavigationListActivity.this);
					objectToAdapter.setOnProductCencernChangeListener(new OnProductConcernChangeListener(){

						@Override
						public void onConcernChange(String prodcutCode, int position, String status) {
							//改变产品关注
							changeProductConcern(prodcutCode, position, status);
						}});
					
				} 
				Toast.makeText(NavigationListActivity.this, obj.getMessage(),
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
	private NavigationListAdapter adapter;
	private ArrayList<Group> data;
	private int index;
	private MenuItem populateItem;
	private long sortId;
	private String sortName;
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		populateItem = menu.add(Menu.NONE, 1, 0, "一键代理");
        populateItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == 1){
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


	protected NavigationListAdapter objectToAdapter(ResponseNavigationList obj,
			NavigationListActivity fragment) {
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
			adapter = new NavigationListAdapter(data, this);
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
						if (item.getStatus().equals(NavigationListAdapter.STATUS_PROXY)) {
							checked = true;
						}
						NavigationListAdapter.checkedMap.put(i, checked);
						NavigationListAdapter.expandMap.put(i, View.GONE);
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
		sortId = getIntent().getLongExtra("sortId", -1);
		sortName = getIntent().getStringExtra("sortName");
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
		((TextView)findViewById(R.id.custon_title)).setText(sortName);
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
					if (status.equals(NavigationListAdapter.STATUS_PROXY)) {
						checked = true;
					}
//					Log.i(TAG, "position：" + position + " status:" + checked);
					//状态变换
					NavigationListAdapter.checkedMap.put(position, checked);
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
		RequestNavigationList requestMineConcern = 
				new RequestNavigationList(LoginMessageDataUtils.getToken(this),
				LoginMessageDataUtils.getUID(this),
				DeviceTool.getUniqueId(this),String.valueOf(getIndex()), count, String.valueOf(order), config, String.valueOf(sortId));
		NavigationListProtocol protocol = new NavigationListProtocol();
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
