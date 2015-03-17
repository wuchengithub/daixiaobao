package com.daixiaobao.other;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.daixiaobao.concern.MyProductAdapter;
import com.daixiaobao.concern.ResponseMineConcern;
import com.daixiaobao.concern.ResponseMineConcern.Group;
import com.daixiaobao.proxy.list.ResponseProxyList;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

public class DataHelper {

	public static final int OBJ_TO_DATA = 0x333;
	public static MyProductAdapter adapter = null;
	private static ArrayList<Group> data;
	private static int index;

	public static MyProductAdapter objectToAdapter(final ResponseMineConcern obj,
			Context context, PullToRefreshGridView pullList) {
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
			adapter = new MyProductAdapter(data, context);
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
						if (item.getStatus().equals(MyProductAdapter.STATUS_PROXY)) {
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
	
	public static void release(){
		adapter = null;
	}

	public static int getIndex() {
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

	public static ArrayList<Group> getData() {
		// TODO Auto-generated method stub
		return data;
	}

	public static void setIndexZero(int i) {
		// TODO Auto-generated method stub
		index = i;
	}

	public static MyProductAdapter objectToProxyAdapter(ResponseProxyList obj,
			FragmentActivity activity) {
		// TODO Auto-generated method stub
		return null;
	}
}
