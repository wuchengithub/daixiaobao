package com.daixiaobao.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

import com.daixiaobao.R;
import com.daixiaobao.categroy.RequestCategroy;
import com.daixiaobao.categroy.ResponseCatagroy;
import com.daixiaobao.concern.MineConcernProductActivity;
import com.daixiaobao.filter.FilterController;
import com.daixiaobao.filter.FilterController.OnFinishFilterListener;
import com.daixiaobao.filter.SearchConfig;
import com.daixiaobao.greenrobot.Group;
import com.daixiaobao.other.CatagroyHelper;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

/**
 * 可代理产品
 * @author Administrator
 *
 */
public class FilterFragment extends Fragment {
	
	private static final String TOP_LEVEL = "-1";
	protected static final String TAG = "ProxyProductFragment";
	private static final int LEVEL_ONE = 1;
	private static final int LEVEL_TWO = 2;
	private static final int LEVEL_THREE = 3;
	private ListView fristCgPull;
	protected List<List<HashMap<String, Object>>> childData;
	protected SimpleExpandableListAdapter eAdapter;
	private int currentGroupPosition;
	protected StringBuilder addressString = new StringBuilder();
	protected boolean barrier;
	protected ProxyCategroyListAdapter groupAdapter;
	private FragmentActivity context;
	protected FilterController filterController;
	private int storeId;
	private boolean fromPics;
	private SearchConfig config;
	private String businessName;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.context = getActivity();
		super.onAttach(activity);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		storeId = getArguments().getInt("storeId");
		businessName = getArguments().getString("businessName");
		View view = inflater.inflate(R.layout.fragment_proxy, container, false);
		if(storeId == -2) {//from upload pics
			fromPics = true;
			view.findViewById(R.id.search_popup_window_content_submit).setVisibility(View.GONE);
		}
		
		fristCgPull = (ListView)view.findViewById(R.id.proxy_categroy_one);
		//设置左侧list的item监听
		fristCgPull.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				groupAdapter.setSelectItem(arg2);
				groupAdapter.notifyDataSetInvalidated();
				HashMap<String, Object> item = (HashMap<String, Object>)arg0.getAdapter().getItem(arg2);
				String code = (String)item.get("code");
				createFilterController(code);
			}
			
		});
		reqCategroy(TOP_LEVEL, LEVEL_ONE);
		return view;	
		
	}
	
	private void createFilterController(String categoryCode) {
		filterController = new FilterController(context, categoryCode);
		filterController.getDataFromDB();
		filterController.setOnFinishFilterListener(new OnFinishFilterListener() {
			
			@Override
			public void onFinish(SearchConfig config) {
				// TODO Auto-generated method stub
				FilterFragment.this.config = config;
				openDetailView(config, storeId);
				
			}
			
		});
	}
	/**
	 * 记录浏览路径
	 * @param withLevel
	 * @param levelName
	 */
	protected void recordAdress(int withLevel, String levelName) {
		// TODO Auto-generated method stub
		switch (withLevel) {
		case LEVEL_ONE:
			addressString.replace(0, addressString.indexOf("/"), levelName + "/");
			break;
		case LEVEL_TWO:
			addressString.replace(addressString.indexOf("/"), addressString.lastIndexOf("/") , levelName + "/");
			break;
		case LEVEL_THREE:
			addressString.replace(addressString.indexOf("/"), 0, levelName + "/");;
			break;
		default:
			break;
		}
		Log.i(TAG, addressString.toString());
	}

	private void openDetailView(SearchConfig config, int storeId) {
		Intent intent = null;
		if(String.valueOf(storeId).equals(LoginMessageDataUtils.getStoreId(context))){
			intent = new Intent(context,MineConcernProductActivity.class);
		} else {
			intent = new Intent("android.intent.action.proxyList");
			intent.putExtra("businessName", businessName);
		}
		
		intent.putExtra("config", config);
		intent.putExtra("storeId", storeId);
		if(!fromPics) {
			getActivity().startActivity(intent);
		} else {
			//getActivity().setResult(0, intent);
			//getActivity().finish();
		}
	}
	/**
	 * 
	 * @param itemId
	 * @param order
	 */
	private void reqCategroy(String itemId, final int order) {
		CatagroyHelper catagroyHelper = CatagroyHelper.getInstance(getActivity(), new CatagroyHelper.OnCategroyHandleListener() {
			
			@Override
			public void onHandle(ResponseCatagroy obj) {
				if(barrier) return;
				// TODO Auto-generated method stub
				List<HashMap<String, Object>> groupData = prepareData(order,
						obj);
				switch (order) {
				case LEVEL_ONE:
					//加载一级菜单
					groupAdapter = prepareLeftListAdapter(groupData);
					groupAdapter.setSelectItem(0);//默认加载第一项
					fristCgPull.setAdapter(groupAdapter);
					
					//继续请求，加载二级类目
					try {
						HashMap<String, Object> fristItem = groupData.get(0);
						createFilterController((String)fristItem.get("code"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case LEVEL_TWO:
					break;
				case LEVEL_THREE:
					break;
				default:
					break;
				}
				CustomLoadingDialog.dismissDialog();
			}
		});
		catagroyHelper.invoke(new RequestCategroy(LoginMessageDataUtils.getToken(getActivity()), LoginMessageDataUtils.getUID(getActivity()), 
				DeviceTool.getUniqueId(getActivity()), itemId));
	}
	
	
	private List<HashMap<String, Object>> prepareData(final int order,
			ResponseCatagroy obj) {
		List<HashMap<String, Object>> groupData = new ArrayList<HashMap<String, Object>>();
		List<Group> group = obj.getGroup();
		if(group != null && group.size() != 0) {
			HashMap<String, Object> item = null;
			for (Group g : group) {
				item = new HashMap<String, Object>();
				item.put("code",g.getCode());
				item.put("name", g.getName());
				item.put("level", order + 1);
				item.put("image", g.getImage());
				groupData.add(item);
			}
		}
		return groupData;
	}
	

	private ProxyCategroyListAdapter prepareLeftListAdapter(
			List<HashMap<String, Object>> groupData) {
		return new ProxyCategroyListAdapter(getActivity(),groupData);
	}
	
	/*private void prepareRightListAdapter(
			List<HashMap<String, Object>> groupData) {
		//定义child
		childData = new ArrayList<List<HashMap<String,Object>>>();
		for (HashMap<String, Object> hashMap : groupData) {
			List<HashMap<String,Object>> childItem = new ArrayList<HashMap<String,Object>>();
			childData.add(childItem);
		}
		eAdapter = new SimpleExpandableListAdapter(getActivity(), groupData, 
				R.layout.fragment_proxy_one_list_content,
				new String[]{"name"}, new int[]{R.id.proxy_one_list_name}, 
				childData, R.layout.fragment_proxy_one_list_child_expand_content, 
				new String[]{"name"}, new int[]{R.id.proxy_one_list_child_name});
	}*/
	@Override
	public void onDestroy() {
		barrier = true;
		super.onDestroy();
		
	}

	public static FilterFragment newInstance(int storeId, String businessName) {
		FilterFragment f = new FilterFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("storeId", storeId);
		bundle.putString("businessName", businessName);
		f.setArguments(bundle);
		return f;
	}


	public SearchConfig getConfig() {
		return filterController.getConfig();
	}
}
