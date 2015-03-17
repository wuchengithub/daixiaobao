package com.daixiaobao.company;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daixiaobao.R;
import com.daixiaobao.adapter.CompanyAdapter;
import com.daixiaobao.company.ResponseCompany.Group;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

/**
 * 代理商
 * @author Administrator
 *
 */
public class CompanyFragment extends Fragment {
	
	private static final String COUNT = "10";
	protected static final int DESC = 1;
	protected static final int ASC = 0;
	protected static final String TAG = "CompanyFragment";
	private int order = ASC;
	private PullToRefreshGridView pull;
	protected List<Group> data;
	protected CompanyAdapter adapter;
	protected boolean barrier;

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(barrier)return;
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				ResponseCompany obj = null;
				if(msg.obj == null){
				} else {
					obj = (ResponseCompany)msg.obj;
					if(obj.getErrorCode() == ProtocolManager.ERROR_CODE_ZORE){
						if(data == null){
							data = new ArrayList<Group>();
							adapter = new CompanyAdapter(data, getActivity());
						}
						Group[] group = obj.getGroup();
						if(group.length != 0){
							for (Group _g : group) {
								data.add(_g);
							}
							pull.setAdapter(adapter);
						}
					} else {
					}
					Toast.makeText(getActivity(), obj.getMessage() + "", Toast.LENGTH_LONG).show();
				}
				break;

			default:
				break;
			}
			CustomLoadingDialog.dismissDialog();
			pull.onRefreshComplete();
		};
	};

	private ToggleButton toggle;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_company, container, false);
		pull = (PullToRefreshGridView)view.findViewById(R.id.company_list);
		toggle = (ToggleButton)view.findViewById(R.id.company_order_toggle);
		toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				changeListOrder(isChecked);
			}
		});
		pull.setOnRefreshListener(new OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				refreshData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				requestCompanyList();
			}

		} );
		pull.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long code) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("android.intent.action.company.detail");
				intent.putExtra("code", String.valueOf(code));
				startActivity(intent);
			}
		});
		requestCompanyList();
		return view;
	}

	protected void refreshData() {
		// TODO Auto-generated method stub
		data = null;
		requestCompanyList();
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
	
	private void requestCompanyList() {
		// TODO Auto-generated method stub
		CustomLoadingDialog.showProgress(getActivity(), "", "获取商家数据", false, true);
		CompanyProtocol companyProtocol = new CompanyProtocol();
		RequestCompany requestCompany = new RequestCompany(LoginMessageDataUtils.getToken(getActivity()), 
				LoginMessageDataUtils.getUID(getActivity()), DeviceTool.getUniqueId(getActivity()), 
				getCurrentLength(), COUNT, String.valueOf(order));
		companyProtocol.invoke(requestCompany, handler);
	}

	private String getCurrentLength() {
		// TODO Auto-generated method stub
		if(data != null){
			return String.valueOf(data.size());
		}
		return "0";
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		barrier = true;
		super.onDestroy();
	}
}
