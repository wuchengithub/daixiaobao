package com.daixiaobao.concern;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.daixiaobao.adapter.ProxyListAdapter;
import com.daixiaobao.concern.ResponseMineConcern.Group;
import com.daixiaobao.filter.SearchConfig;
import com.daixiaobao.other.Test;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

/**
 * 关注的代理产品，我代理的产品
 * 
 * @author Administrator
 * 
 */
public class MineConcernProductActivity extends SherlockFragmentActivity {

	protected static final int DESC = 1;
	protected static final int ASC = 0;
	private ToggleButton toggle;
	private int order = ASC;
	private static final String count = "10";
	private static final String TAG = "MineConcernProductFragment";
	private boolean barrier;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (barrier)
				return;
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				ResponseMineConcern obj = null;
				if (msg.obj == null) {// 演示数据
					obj = Test.createTestData(obj);
				} else {
					obj = (ResponseMineConcern) msg.obj;
				}
				if (obj.getErrorCode() == ProtocolManager.ERROR_CODE_ZORE) {
					MyProductAdapter objectToAdapter = objectToAdapter(obj);
				}
				Toast.makeText(activity, obj.getMessage(),
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			pullList.onRefreshComplete();
		};
	};
	private PullToRefreshGridView pullList;
	private SearchConfig config;
	private MenuItem populateItem;
	private Activity activity = this;
	private MyProductAdapter adapter;
	private ArrayList<Group> data;
	private int index;
	private int storeId;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_mine_concern);
		String title = getIntent().getStringExtra("title");
		storeId = getIntent().getIntExtra("storeId", -1);
		this.config = (SearchConfig) getIntent().getSerializableExtra("config");
		pullList = (PullToRefreshGridView) findViewById(R.id.mine_concern_product_list);
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

		});
		pullList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				openItemDetail(arg2);
			}
		});
		((TextView)findViewById(R.id.custon_title)).setText("我的产品");
		findViewById(R.id.custon_back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getDataFromServer();
	}


	protected void openItemDetail(int position) {
		// TODO Auto-generated method stub
		String code = getData().get(position).getCode();
		Intent intent = new Intent("android.intent.action.detail");
		intent.putExtra("code", code);
		activity.startActivity(intent);
	}
	public ArrayList<Group> getData() {
		// TODO Auto-generated method stub
		return data;
	}
	/**
	 * 排序列表
	 * 
	 * @param isChecked
	 */
	protected void changeListOrder(boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
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
	public void setIndexZero(int i) {
		// TODO Auto-generated method stub
		index = i;
	}
	private void getDataFromServer() {
		RequestMineConcern requestMineConcern = new RequestMineConcern(
				LoginMessageDataUtils.getToken(activity),
				String.valueOf(storeId),
				DeviceTool.getUniqueId(activity),
				String.valueOf(getIndex()), count,
				String.valueOf(order), config);
		MineConcernProtocol protocol = new MineConcernProtocol();
		protocol.invoke(requestMineConcern, handler);
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

	private void getDataFromServer(SearchConfig config) {
		this.config = config;
		refreshData();
	}

	public interface OnProductConcernChangeListener {
		public void onConcernChange(String prodcutCode, int position,
				String status);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		barrier = true;
		super.onDestroy();
	}

	protected MyProductAdapter objectToAdapter(ResponseMineConcern obj) {
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
			adapter = new MyProductAdapter(data, activity);
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
						if (ProxyListAdapter.STATUS_PROXY.equals(item.getStatus())) {
							checked = true;
						}
						MyProductAdapter.checkedMap.put(i, checked);
						MyProductAdapter.expandMap.put(i, View.GONE);
					}
					i++;
				}
			}
			adapter.notifyDataSetChanged();
		}
		return adapter;
	}
	
}
