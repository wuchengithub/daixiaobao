package com.wookii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HaveCardOrNotActivity extends Activity {
	Button btn;
	TextView tv;
	boolean haveCard = false;
	boolean retOrNot = false;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				getApplicationContext(), "have_card_or_not_activity");
		setContentView(layoutId);

		intent = getIntent();
		intent.getBooleanExtra("haveCard", false);

		btn = (Button) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "have_card_or_not_btn"));
		tv = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "have_card_or_not_tv"));

		TextView tv1 = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "tv1"));
		TextView tv2 = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "tv2"));
		TextView tv3 = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "tv3"));

		SpannableString ss1 = new SpannableString("已有大玩家会员卡绑定，马上送5枚游戏币；");
		ss1.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						ResourcesIdFactoryUtils.getColorId(
								getApplicationContext(), "blue_text"))), 14,
				15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 14, 15,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv1.setText(ss1);

		SpannableString ss2 = new SpannableString("新办大玩家会员卡（24小时内）即绑即送20枚游戏币；");
		ss2.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						ResourcesIdFactoryUtils.getColorId(
								getApplicationContext(), "blue_text"))), 19,
				21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss2.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 19, 21,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv2.setText(ss2);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				haveCard = true;
				retOrNot = true;
				Intent i = new Intent();
				i.putExtra("boolean", true);
				i.putExtra("haveCard", haveCard);
				i.putExtra("retOrNot", retOrNot);
				i.setClass(HaveCardOrNotActivity.this,
						LoginAndRegActivity.class);
				finish();
				startActivityForResult(i, 1);
			}
		});

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				haveCard = false;
				retOrNot = false;
				Intent i = new Intent();
				i.putExtra("haveCard", haveCard);
				i.putExtra("retOrNot", retOrNot);
				i.setClass(HaveCardOrNotActivity.this, HaveNoCardActivity.class);
				finish();
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
