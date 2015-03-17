package com.wookii.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.wookii.R;
import com.wookii.ResourcesIdFactoryUtils;

public class ToggleBar extends LinearLayout implements
		MyToggleButton.OnToggleStatusChangeListener {

	protected static final String TAG = "ToggleBar";
	private Context context;
	private HashMap<Object, View> childKeyMap = new HashMap<Object, View>();
	private HashMap<Object, View> childValueMap = new HashMap<Object, View>();
	private ArrayList<HashMap<String, Object>> data;
	private ArrayList<HashMap<String, Object>> suqencedData;
	private ArrayList<HashMap<Object, Object>> child;
	private OnToggleChangedListener listener;

	public ToggleBar(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public ToggleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initChild(ArrayList<HashMap<Object, Object>> child,
			LinearLayout.LayoutParams params, boolean bool) {
		this.child = child;
		for (HashMap<Object, Object> hashMap : child) {
			MyToggleButton checkBox = new MyToggleButton(context);

			checkBox.setPadding(50, 5, 50, 5);

			Iterator<Object> iterator = hashMap.keySet().iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				checkBox.setTag(key);
				String text = (String) hashMap.get(key);
				checkBox.setText(text);
				childKeyMap.put(key, checkBox);
				childValueMap.put(text, checkBox);
			}
			checkBox.setBackgroundResource(ResourcesIdFactoryUtils.getColorId(
					context, "yellow_title_none"));
			MyToggleStatusChangeListener listener2 = new MyToggleStatusChangeListener();
			checkBox.setOnToggleStatusChangeListener(listener2);
			addView(checkBox, params);
		}
		if (bool) {
			setOption(1);
		} else {
			setOption(0);
		}

	}

	/**
	 * 设置成被选中状态
	 * 
	 * @param position
	 */
	private void setOption(int position) {
		// TODO Auto-generated method stub
		MyToggleButton view = (MyToggleButton) childKeyMap.get(position);
		view.setBackgroundResource(ResourcesIdFactoryUtils.getColorId(context,
				"yellow_title_press"));
		view.setFlag(true);
	}

	class MyToggleStatusChangeListener implements
			MyToggleButton.OnToggleStatusChangeListener {

		@Override
		public boolean onStatusChange(View v, int status) {
			// TODO Auto-generated method stub
			int tag = Integer.parseInt(v.getTag().toString());
			if (listener != null) {
				listener.onChanged(tag);
			}
			// SOSLog.i(TAG, "tag" + tag,SOSLog.INTERDICT_WRITE);
			Iterator<Object> iterator = childKeyMap.keySet().iterator();
			while (iterator.hasNext()) {

				int key = Integer.parseInt(iterator.next().toString());
				;
				MyToggleButton view = (MyToggleButton) childKeyMap.get(key);
				if (!(key == tag)) {// 排除自己
					view.setFlag(false);
					view.setBackgroundResource(ResourcesIdFactoryUtils
							.getColorId(context, "yellow_title_none"));
				} else {
					((MyToggleButton) view).changeBackground(view);
				}
			}
			// SOSLog.i(TAG, "status:" + status, SOSLog.INTERDICT_WRITE);
			if (data != null) {
				ToggleBar.this.onStatusChange(v, status);
			}
			if (!((MyToggleButton) v).getFlag()) {
				((MyToggleButton) v).setFlag(true);
				return false;
			}
			return true;

		}

	}

	@Override
	public boolean onStatusChange(View v, int status) {
		// TODO Auto-generated method stub
		return false;
	}

	public interface OnToggleChangedListener {
		void onChanged(int index);
	}

	public void setSequenceData(ArrayList<HashMap<String, Object>> data) {
		this.data = data;
	}

	public void setOnToggleChangeListener(OnToggleChangedListener listener) {
		this.listener = listener;

	}
}
