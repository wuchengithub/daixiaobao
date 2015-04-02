package com.daixiaobao.proxy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.daixiaobao.R;
import com.daixiaobao.concern.MineConcernProductActivity;
import com.daixiaobao.filter.SearchConfig;
import com.wookii.utils.LoginMessageDataUtils;

/**
 * 过滤和筛选
 * 
 * @author Administrator
 * 
 */
public class FilterAndSearchActivity extends SherlockFragmentActivity implements
		OnQueryTextListener {

	private int storeId;
	private ActionBar supportActionBar;
	private String businessName;
	private boolean fromPics;
	private SearchView searchView;
	private ViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storeId = getIntent().getIntExtra("storeId", -1);
		businessName = getIntent().getStringExtra("businessName");
		// set the Above View
		setContentView(R.layout.activity_proxy_product);

		viewPager = (ViewPager) findViewById(R.id.filter_view_pager);
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		((TextView) findViewById(R.id.custon_title)).setText(businessName);
		findViewById(R.id.custon_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});

		searchView = (SearchView) findViewById(R.id.product_search);
		searchView.setOnQueryTextListener(this);
		searchView.setIconifiedByDefault(false);
		searchView.setSubmitButtonEnabled(true);
		searchView.setFocusable(false);
	}

	class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f = null;
			switch (position) {
			case 0:
				f = FilterFragment.newInstance(storeId, businessName);
				break;
			case 1:
				f = new EmptyFragment();
				break;

			default:
				break;
			}
			return f;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return "";
		}

		@Override
		public int getCount() {
			return 2/* indicator.getChildCount() */;
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		SearchConfig searchConfig = new SearchConfig();
		searchConfig.setProductCode(query);
		Intent intent = null;
		if (String.valueOf(storeId).equals(
				LoginMessageDataUtils.getStoreId(this))) {
			intent = new Intent(this, MineConcernProductActivity.class);
		} else {
			intent = new Intent("android.intent.action.proxyList");
			intent.putExtra("businessName", businessName);
		}

		intent.putExtra("config", searchConfig);
		intent.putExtra("storeId", storeId);
		if (!fromPics) {
			startActivity(intent);
		} else {
			// getActivity().setResult(0, intent);
			// getActivity().finish();
		}
		return true;
	}
	
	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText.length() != 0) {
			if(viewPager.getCurrentItem() == 0) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
			}
		} else {
			if(viewPager.getCurrentItem() == 1) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
			}
			
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if(viewPager.getCurrentItem() == 1) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
				searchView.setQuery("", false);
			} else {
				finish();
			}
			return true;
		}
		return false;
	}
}
