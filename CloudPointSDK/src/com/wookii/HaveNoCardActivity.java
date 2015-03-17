package com.wookii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HaveNoCardActivity extends Activity {
	Button btn;
	TextView tv;
	boolean haveCard = false;
	boolean retOrNot = false;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				getApplicationContext(), "have_no_card_activity");
		setContentView(layoutId);

		intent = getIntent();
		intent.getBooleanExtra("haveCard", false);

		btn = (Button) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "have_card_or_not_btn"));
		tv = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "have_card_or_not_tv"));

		TextView tv1 = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "tv1"));
		SpannableString ss1 = new SpannableString("现在办理即可多得20枚游戏币！\n请到店内收银台咨询！");
		ss1.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						ResourcesIdFactoryUtils.getColorId(
								getApplicationContext(), "blue_text"))), 8, 10,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 8, 10,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv1.setText(ss1);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				haveCard = true;
				retOrNot = true;
				Intent i = new Intent();
				i.putExtra("boolean", true);
				i.putExtra("haveCard", haveCard);
				i.putExtra("retOrNot", retOrNot);
				i.setClass(HaveNoCardActivity.this, LoginAndRegActivity.class);
				finish();
				startActivityForResult(i, 1);
			}
		});

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				haveCard = false;
				retOrNot = true;
				Intent i = new Intent();
				i.putExtra("boolean", true);
				i.putExtra("haveCard", haveCard);
				i.putExtra("retOrNot", retOrNot);
				i.setClass(HaveNoCardActivity.this, LoginAndRegActivity.class);
				finish();
				startActivityForResult(i, 1);
			}
		});
	}

}
