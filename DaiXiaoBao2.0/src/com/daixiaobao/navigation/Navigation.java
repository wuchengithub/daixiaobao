package com.daixiaobao.navigation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daixiaobao.R;
import com.daixiaobao.navigation.NavigationRequest.NavigationBean;
import com.daixiaobao.navigation.NavigationRequest.NavigationBean.Item;
import com.daixiaobao.widget.CustomLoadingDialog;

public class Navigation extends Fragment {

	private ListView list;
	protected List<Item> data;
	protected String parentId;
	private List<String> locationNameList = new ArrayList<String>();
	private List<NavigationRequest> locationList = new ArrayList<NavigationRequest>();
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				NavigationBean obj = (NavigationBean) msg.obj;
				if (obj.getErrorCode() == 0) {
					if(nextLevel > 1){
						navigation_location_layout.setVisibility(View.VISIBLE);
						navigation_location.setText("当前位置：" + locationName);
					} else {
						navigation_location_layout.setVisibility(View.GONE);
					}
					data = obj.getGroup();
					parentId = obj.getParentId();
					list.setAdapter(new NavigationAdapter());
				} else if (obj.getErrorCode() == 2) {
				}
				Toast.makeText(context, obj.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
			CustomLoadingDialog.dismissDialog();

		}
	};
	private Activity context;
	protected int nextLevel;
	private View navigation_location_layout;
	protected String locationName = "";
	private TextView navigation_location;
	private static final String ID_ROOT = "0";
	
	public void onAttach(android.app.Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation, null);
		list = (ListView)view.findViewById(R.id.navigation_list);
		list.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NavigationAdapter navigationAdapter = (NavigationAdapter)arg0.getAdapter();
				long itemId = navigationAdapter.getItemId(arg2);
				String sortName = navigationAdapter.getSortName(arg2);
				if(navigationAdapter.isEnd(arg2)) {
					Intent intent = new Intent(context, NavigationListActivity.class);
					intent.putExtra("sortName", sortName);
					intent.putExtra("sortId", itemId);
					context.startActivity(intent);
				} else {
					locationName += sortName;
					request(String.valueOf(navigationAdapter.getParentId(arg2)));
				}
				
			}
		});
		navigation_location_layout = view.findViewById(R.id.navigation_location_layout);
		navigation_location =  (TextView)view.findViewById(R.id.navigation_location);
		view.findViewById(R.id.navigation_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index = -- nextLevel;
				locationName = "";
				NavigationRequest navigationRequest = locationList.get(index - 1);
				navigationRequest.request(context, handler, navigationRequest.getId());
			}
		});;
		nextLevel = 0;
		navigation_location.setText("当前位置：");
		request(ID_ROOT);
		return view;
	}
	
	void request(String id){
		nextLevel++;
		NavigationRequest navigationRequest = new NavigationRequest();
		navigationRequest.request(context, handler, id);
		locationList.add(navigationRequest);
	}
	class NavigationAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		public String getSortName(int position) {
			// TODO Auto-generated method stub
			return data.get(position).getSortName();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return data.get(position).getSortId();
		}

		
		public long getParentId(int position){
			return data.get(position).getSortId();
		}
		public boolean isEnd(int position){
			//0 true 1 false
			if(data.get(position).getIsEnd() == 0) {
				return true;
			} else {
				return false;
			}
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = context.getLayoutInflater().inflate(
						R.layout.navigation_list_content, null);
				holder.name = (TextView) convertView
						.findViewById(R.id.navigation_list_content_name);
				holder.message = (TextView) convertView
						.findViewById(R.id.navigation_list_content_message);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Item item = data.get(position);
			holder.name.setText(item.getSortName());
			holder.message.setText(item.getDescription());
			return convertView;
		}
		
		class  ViewHolder{
			public TextView name, message;
			
		}
		
	}
}
