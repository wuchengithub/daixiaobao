package com.daixiaobao.widget;

import com.daixiaobao.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconButton extends LinearLayout {

	public IconButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public IconButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.IconButton);
		ImageView icon = new ImageView(context);
		TextView text = new TextView(context);
		text.setText(obtainStyledAttributes.getText(R.styleable.IconButton_android_text));
		text.setTextColor(obtainStyledAttributes.getColor(R.styleable.IconButton_android_textColor, -16777216));
		icon.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.IconButton_android_icon));
		addView(icon);
		addView(text);
	}
}
