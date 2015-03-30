package com.daixiaobao;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.daixiaobao.center.MainCenterFragment;
import com.daixiaobao.friend.ApplyListFragment;
import com.daixiaobao.friend.FriendFragment;
import com.daixiaobao.friend.SupplierActivity;
import com.daixiaobao.more.MoreFragment;
import com.daixiaobao.navigation.Navigation;
import com.daixiaobao.navigation.NavigationActivity;
import com.daixiaobao.proxy.ProxyProductFragment;
import com.daixiaobao.widget.ConformDialog;
import com.example.testpic.PublishedActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends SlidingFragmentActivity {
	private String[] TAB_NAME = new String[] { "好友", "申请列表", "资源导航", "设置" };
	private Fragment mContent;
	private Handler handler = new Handler();
	private SlidingMenu sm;
	private ActionBar supportActionBar;
	private Menu menu;
	private ViewPager pager;
	private RadioGroup indicator;
	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "main");
		if (mContent == null)
			mContent = new ContentFragment();
		// set the Above View
		setContentView(R.layout.content_frame);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MainCenterFragment()).commit();

		// customize the SlidingMenu
		configurationSlidingMenu();
		// check version
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.update(this);
		
		
		/*findViewById(R.id.home_navigation).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						NavigationActivity.class);
				startActivity(intent);
			}
		});*/
		
		
		//indicator = (RadioGroup) findViewById(R.id.home_indicator);
		pager = (ViewPager) findViewById(R.id.base_view_pager);
		FragmentPagerAdapter adapter = new FristHomeAdapter(
				getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(3);
		findViewById(R.id.home_new_product).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								PublishedActivity.class);
						startActivity(intent);
					}
				});
		;

		findViewById(R.id.custon_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						toggle();
					}
				});
	}

	class FristHomeAdapter extends FragmentPagerAdapter {
		public FristHomeAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f = null;
			switch (position) {
			case 0:
				f = new FriendFragment();
				break;
			/*case 1:
				f = new ApplyListFragment();
				;
				break;
			case 2:
				f = new Navigation();
				break;
			case 3:
				f = new MoreFragment();
				break;*/
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
			return 1/*indicator.getChildCount()*/;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "上传").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 0:

			break;
		case 1:

			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void configurationSlidingMenu() {
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setShadowWidth(50);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 设置动画zoom效果
		sm.setBehindCanvasTransformer(new CanvasTransformer() {

			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				// TODO Auto-generated method stub
				canvas.translate(
						0,
						canvas.getHeight()
								* (1 - interp.getInterpolation(percentOpen)));
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Dialog dialog = null;
			ConformDialog.Builder customBuilder = new ConformDialog.Builder(
					this);
			customBuilder
					.setTitle("提示")
					.setMessage("是否要退出代销宝?")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
									dialog.dismiss();
								}

							});
			dialog = customBuilder.create();
			dialog.show();
			return true;
		} else {
			return false;
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
