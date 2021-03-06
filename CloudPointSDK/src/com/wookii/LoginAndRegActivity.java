package com.wookii;

import java.util.ArrayList;
import java.util.List;

import com.wookii.LoginFragment.OnLoginListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginAndRegActivity extends FragmentActivity {

	public static final int REQUEST_CODE_LOGIN = 10;

	private static final int LOGIN = 0;
	private static final int REGISTER = 1;

	private LoginFragment mLoginFragment = null;
	private RegisterFragment mRegFragment = null;

	/** 页面list **/
	private List<Fragment> fragmentList;
	/** 页面title list **/
	private List<String> titleList;
	private ViewPager viewPager;
	private TextView tvLogin, tvReg;
	private ImageView ivLoginLine, ivRegLine;
	private MyPagerAdapter myPagerAdapter;

	private Intent intent;
	/** 引导页传入的参数 */
	private boolean haveCard;
	/** 引导页传入的参数 */
	private boolean retOrNot;

	private int colorWhite;
	private int colorSwitchLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layoutId = ResourcesIdFactoryUtils.getLayoutId(
				getApplicationContext(), "cpsdk_login_and_reg_activity");
		setContentView(layoutId);

		InitView();
		setUpViewPager();
	}

	private void InitView() {
		tvLogin = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "tv_login_reg_login"));
		tvReg = (TextView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "tv_login_reg_ret"));
		tvLogin.setOnClickListener(new MyOnClickListener(0));
		tvReg.setOnClickListener(new MyOnClickListener(1));
		ivLoginLine = (ImageView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "iv_login_reg_loginline"));
		ivRegLine = (ImageView) findViewById(ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "iv_login_reg_regline"));
		colorWhite = ResourcesIdFactoryUtils.getColorId(
				getApplicationContext(), "WHITE");
		colorSwitchLine = ResourcesIdFactoryUtils.getColorId(
				getApplicationContext(), "switch_line");
	}

	private void setUpViewPager() {

		fragmentList = new ArrayList<Fragment>();
		titleList = new ArrayList<String>();

		int viewPagerID = ResourcesIdFactoryUtils.getId(
				getApplicationContext(), "login_reg_pager");
		viewPager = (ViewPager) findViewById(viewPagerID);

		if (null == mLoginFragment){
			mLoginFragment = LoginFragment.newInstance();
			mLoginFragment.setOnLoginListener(new OnLoginListener(){

				@Override
				public void onLogin(int code) {
					// TODO Auto-generated method stub
					setResult(code);
				}});
		} 
			
		
		if (null == mRegFragment)
			mRegFragment = RegisterFragment.newInstance();
		fragmentList.add(mLoginFragment);
		fragmentList.add(mRegFragment);
		titleList.add("登录");
		titleList.add("注册");
		myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
				fragmentList, titleList);
		viewPager.setAdapter(myPagerAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		intent = getIntent();
		haveCard = intent.getBooleanExtra("haveCard", false);
		retOrNot = intent.getBooleanExtra("retOrNot", false);
		boolean regActivity = intent.getBooleanExtra("boolean", false);
		if (regActivity) {
			// show RegisterFragment
			viewPager.setCurrentItem(REGISTER);
			ivLoginLine.setBackgroundColor(getResources().getColor(colorWhite));
			ivRegLine.setBackgroundColor(getResources().getColor(
					colorSwitchLine));
		} else {
			// show LoginFragment
			ivLoginLine.setBackgroundColor(getResources().getColor(
					colorSwitchLine));
			ivRegLine.setBackgroundColor(getResources().getColor(colorWhite));
			viewPager.setCurrentItem(LOGIN);
		}
	}

	public void changeCurrentViewPagerItem(int index){
		viewPager.setCurrentItem(index);
	}
	private class MyPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragmentList;
		private List<String> titleList;

		public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,
				List<String> titleList) {
			super(fm);
			this.fragmentList = fragmentList;
			this.titleList = titleList;
		}

		@Override
		public long getItemId(int position) {

			return super.getItemId(position);
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 每个页面的title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return (titleList.size() > position) ? titleList.get(position) : "";
		}

		/**
		 * 页面的总个数
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				ivLoginLine.setBackgroundColor(getResources().getColor(
						colorSwitchLine));
				ivRegLine.setBackgroundColor(getResources()
						.getColor(colorWhite));
				break;
			case 1:
				// if (!haveCard && !retOrNot) {
				// // 引导页
				// Intent intent = new Intent();
				// intent.setClass(LoginAndRegActivity.this,
				// HaveCardOrNotActivity.class);
				// startActivity(intent);
				// finish();
				// }
				ivLoginLine.setBackgroundColor(getResources().getColor(
						colorWhite));
				ivRegLine.setBackgroundColor(getResources().getColor(
						colorSwitchLine));
				break;
			}
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
	}

	public boolean isHaveCard() {
		return haveCard;
	}

	public void setHaveCard(boolean haveCard) {
		this.haveCard = haveCard;
	}

	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//setResult(Activity.RESULT_CANCELED, intent);
		super.onDestroy();
	}
}
