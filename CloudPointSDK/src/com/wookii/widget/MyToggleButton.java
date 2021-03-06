package com.wookii.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wookii.R;
import com.wookii.ResourcesIdFactoryUtils;

/**
 * 
 * @author wuchen
 * 
 */
public class MyToggleButton extends LinearLayout {

	private int toggleStatus;
	private boolean flag;
	private TextView text;
	private Context context;

	public MyToggleButton(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				context.getApplicationContext(), "global_toggle_bar");
		LayoutInflater.from(context).inflate(layoutId, this, true);
		text = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				context.getApplicationContext(), "globalbar_content_text"));
	}

	public View getTextView() {
		return text;
	}

	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public void setToggleStatus(int toggleStatus) {
		this.toggleStatus = toggleStatus;
	}

	public int getToggleStatus() {
		return toggleStatus;
	}

	public void setOnToggleStatusChangeListener(
			final OnToggleStatusChangeListener listener) {

		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getFlag()) {
					if (getToggleStatus() == 0) {
						setToggleStatus(1);
					} else {
						setToggleStatus(0);
					}
				}
				boolean onStatusChange = listener.onStatusChange(v,
						getToggleStatus());
				if (onStatusChange) {
					changeBackground(v);
				}
			}
		});
	}

	public void changeBackground(View v) {
		setBackgroundResource(ResourcesIdFactoryUtils.getColorId(context,
				"yellow_title_press"));
	}

	public interface OnToggleStatusChangeListener {
		public static final int ASC = 0;
		public static final int DESC = 1;

		public boolean onStatusChange(View v, int status);
	}

	public void setFlag(boolean flag) {
		// TODO Auto-generated method stub
		this.flag = flag;
	}

	public boolean getFlag() {
		// TODO Auto-generated method stub
		return flag;
	}

	public void setText(String str) {
		// TODO Auto-generated method stub
		text.setText(str);
	}

	public Object getText() {
		// TODO Auto-generated method stub
		return text.getText();
	}
}
