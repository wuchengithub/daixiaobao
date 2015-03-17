package com.daixiaobao.detailProdcut;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.daixiaobao.BaseApplication;
import com.daixiaobao.R;
import com.daixiaobao.concern.change.RequestConcernChange;
import com.daixiaobao.concern.change.ResponseConcernChange;
import com.daixiaobao.other.ConcernHelper;
import com.daixiaobao.other.ConcernHelper.OnConCernChangeHandleListener;
import com.daixiaobao.other.ShareCallBackHelper;
import com.daixiaobao.other.ShareCallBackHelper.OnShareCallBackListener;
import com.daixiaobao.other.Test;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.daixiaobao.widget.FlowIndicator;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class DetailProdcutActivity extends SherlockFragmentActivity {

	private static final String TAG = "DetailProdcutActivity";
	private static final String CONCERN = "0";
	private int loadCount;
	private ActionBar supportActionBar;
	protected MyPDAdapter myPDAdapter;
	protected int urlSize;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				ResponseDetailProduct obj = null;
				if ((ResponseDetailProduct) msg.obj == null) {
					obj = Test.creatTestDetailMessage(obj);
				} else {
					obj = (ResponseDetailProduct) msg.obj;
				}
				//allMessage.setText(obj.toMessageGroupString());
				priice.setText("￥" + obj.getPrice());
				discription.setText(obj.getDescription());
				List<String> urls = new ArrayList<String>();
				for (String url : obj.getImageUrls()) {
					if (url != null && !"".equals(url)) {
						urls.add(url);
					}
				}
				urlSize = urls.size();
				myPDAdapter = new MyPDAdapter(DetailProdcutActivity.this, urls,
						new MyloadCountListener() {

							@Override
							public void onCount() {
								// TODO Auto-generated method stub
								loadCount++;
								if (loadCount >= urlSize) {
									CustomLoadingDialog.dismissDialog();
								}
							}
						});
				myPDAdapter.setIndcator(indcator);
				viewPager.setAdapter(myPDAdapter);
				break;

			default:
				break;
			}

		};
	};

	private TextView allMessage, discription, priice;
	private ViewPager viewPager;
	private FlowIndicator indcator;
	private String code;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.prodcut_detail);
		File cacheDirectory = StorageUtils.getCacheDirectory(this);
		code = getIntent().getStringExtra("code");
		allMessage = (TextView) findViewById(R.id.product_detail_allmessage);
		discription = (TextView) findViewById(R.id.product_detail_discription);
		priice = (TextView) findViewById(R.id.product_detail_priice);
		viewPager = (ViewPager) findViewById(R.id.base_view_pager);
		indcator = (FlowIndicator) findViewById(R.id.indicator);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				indcator.setSeletion(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		viewPager.setOffscreenPageLimit(1);
		getDetaileMessage(code);
		findViewById(R.id.custon_back).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		findViewById(R.id.custon_share).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!(loadCount >= urlSize)) {
							Toast.makeText(DetailProdcutActivity.this, "稍等，图片加载中", Toast.LENGTH_LONG)
									.show();
							return;
						}
						ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
						cmb.setText(discription.getText().toString());
						DiscCacheAware discCache = BaseApplication.imageLoader
								.getDiscCache();
						ArrayList<Uri> imageArray = new ArrayList<Uri>();
						for (String key : myPDAdapter.getUrls()) {
							File file = discCache.get(key);
							imageArray.add(Uri.fromFile(file));
							// Log.i(TAG, file.toString());
						}
						Intent intent = new Intent();
						ComponentName localComponentName = new ComponentName(
								"com.tencent.mm",
								"com.tencent.mm.ui.tools.ShareToTimeLineUI");
						intent.setComponent(localComponentName);
						intent.setAction("android.intent.action.SEND_MULTIPLE");
						intent.setType("image/*");
						intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
								imageArray);
						try {
							startActivityForResult(intent, 0x333);
							Toast.makeText(DetailProdcutActivity.this, "请直接长按文本框粘贴产品描述！",
									Toast.LENGTH_LONG).show();
						} catch (Exception e) {
							// TODO Auto-generated catch block

						}
					}
				});
	}

	private void getDetaileMessage(String code) {
		// TODO Auto-generated method stub
		//CustomLoadingDialog.showProgress(this, "", "正在获取商品详情", false, false);
		DetailProductProtocol detailProductProtocol = new DetailProductProtocol();
		detailProductProtocol.invoke(
				new RequestDetailProduct(LoginMessageDataUtils.getToken(this),
						LoginMessageDataUtils.getUID(this), DeviceTool
								.getUniqueId(this), code), handler);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case 0:

			break;
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		// Log.i(TAG, "requestCode:" + requestCode + "resultCode:" +
		// resultCode);
		if (resultCode == 0) {// 完成分享
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	interface MyloadCountListener {
		public void onCount();
	}

	class MyPDAdapter extends PagerAdapter {

		private List<String> urls;
		private Context context;
		private FlowIndicator indcator;
		private MyloadCountListener listener;

		public MyPDAdapter(Context context, List<String> urls,
				MyloadCountListener listener) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.urls = urls;
			this.listener = listener;
		}

		public List<String> getUrls() {
			// TODO Auto-generated method stub
			return urls;
		}

		public void setIndcator(FlowIndicator indcator) {
			// TODO Auto-generated method stub
			this.indcator = indcator;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			indcator.setCount(urls.size());
			return urls.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);

		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return "";
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			PhotoView imageView = new PhotoView(context);
			//ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			BaseApplication.imageLoader.displayImage(urls.get(position),
					imageView, BaseApplication.options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							listener.onCount();
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							listener.onCount();
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub

						}
					});
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

	}
}
