package com.daixiaobao.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daixiaobao.BaseApplication;
import com.daixiaobao.R;
import com.daixiaobao.company.ResponseCompany.Group;

public class CompanyAdapter extends BaseAdapter {

	public static final String STATUS_PROXY = "0";
	public static final String STATUS_UN_PROXY = "1";
	private static final String TAG = "MyProductAdapter";
	private LayoutInflater layoutInflater;
	private List<Group> data;
	public static HashMap<Integer, Boolean> checkedMap;

	public CompanyAdapter(List<Group> data, Context context) {
		// TODO Auto-generated constructor stub
		layoutInflater = ((Activity) context).getLayoutInflater();
		this.data = data;
	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return Long.parseLong(data.get(arg0).getCode());
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.company_list_content, null);
			holder.company = (TextView)convertView.findViewById(R.id.product_list_content_product_code);
			holder.shareTotal = (TextView)convertView.findViewById(R.id.company_share_total);
			holder.image = (ImageView)convertView.findViewById(R.id.company_pic);
			holder.agentTotal = (TextView)convertView.findViewById(R.id.company_agent_total);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Group item = data.get(position);
		Log.i(TAG, "" + position);
		holder.company.setText(item.getCompany());
		holder.shareTotal.setText("分享次数：" + item.getShareTotal());
		holder.agentTotal.setText("代理产品个数：" + item.getAgentTotal());
		BaseApplication.imageLoader.displayImage(item.getCompanyPicUrl(), holder.image, BaseApplication.options);
		return convertView;
	}

	final class ViewHolder {

		TextView agentTotal;
		ImageView image;
		TextView shareTotal;
		TextView company;

	}

}
