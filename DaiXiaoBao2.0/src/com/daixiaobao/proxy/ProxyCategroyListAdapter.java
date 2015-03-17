package com.daixiaobao.proxy;

import java.util.HashMap;
import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daixiaobao.BaseApplication;
import com.daixiaobao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProxyCategroyListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<HashMap<String, Object>> data;
	private FragmentActivity context;

	public ProxyCategroyListAdapter(FragmentActivity activity,
			List<HashMap<String, Object>> groupData) {
		// TODO Auto-generated constructor stub
		this.layoutInflater = activity.getLayoutInflater();
		this.context = activity;
		this.data = groupData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Long.parseLong((String) data.get(position).get("code"));
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	private int selectItem = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.fragment_proxy_left_list_content, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.proxy_left_list_name);
			holder.image = (ImageView) convertView
					.findViewById(R.id.proxy_left_list_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position == selectItem) {//改变背景色
			holder.name.setTextColor(context.getResources().getColor(R.color.new_yese));
		} else {
			holder.name.setTextColor(context.getResources().getColor(R.color.new_shenhong));
		}
		holder.name.setText((String) data.get(position).get("name"));
		ImageLoader.getInstance().displayImage(
				(String) data.get(position).get("image"), holder.image,
				BaseApplication.options);
		return convertView;
	}

	final class ViewHolder {

		ImageView image;
		TextView name;

	}
}
