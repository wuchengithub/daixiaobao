package com.example.testpic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.daixiaobao.R;
import com.daixiaobao.filter.SearchConfig;
import com.daixiaobao.navigation.Navigation;
import com.daixiaobao.navigation.NavigationRequest;
import com.daixiaobao.navigation.NavigationRequest.NavigationBean;
import com.daixiaobao.navigation.NavigationRequest.NavigationBean.Item;
import com.daixiaobao.proxy.FilterFragment;
import com.daixiaobao.widget.ConformDialog;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class PublishedActivity extends SherlockFragmentActivity {

	private static final int SEND = 0;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private EditText address;
	private EditText price;
	private EditText price2;
	private EditText productDesc;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(PublishedActivity.this, (String) msg.obj,
					Toast.LENGTH_LONG).show();
		};
	};
	private List<Item> data;
	private View fragmentContainer;
	private Button chooiceNext;
	private OnClickListener next;
	private OnClickListener send;
	private FilterFragment proxyProductFragment;
	private CheckBox isaljj;
	private Spinner spinnerDangkou;
	protected Navigation navigation;
	protected boolean isSyncALJJ;
	private View actionbar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectimg);
		fragmentContainer = findViewById(R.id.publish_content);
		isaljj = (CheckBox) findViewById(R.id.isaljj);
		if(LoginMessageDataUtils.getIsVip(this)){
			isaljj.setVisibility(View.VISIBLE);
		}
		isaljj.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				isSyncALJJ = isChecked;
				if (isChecked) {
					
				} else {
				}
			}
		});
		proxyProductFragment = FilterFragment.newInstance(-2, "");
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.publish_content, proxyProductFragment).commit();
		Init();
		actionbar = findViewById(R.id.custom_actionbar);
		findViewById(R.id.custon_back).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { menu.add(0, 0,
	 * 0, "发布").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); return true; }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			conform();
			break;
		case SEND:
			send();
			break;
		default:
			break;
		}
		return true;
	}

	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(PublishedActivity.this, actionbar);
					
				} else {
					Intent intent = new Intent(PublishedActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

		address = (EditText) findViewById(R.id.address);
		price = (EditText) findViewById(R.id.price);
		price2 = (EditText) findViewById(R.id.price2);
		chooiceNext = (Button) findViewById(R.id.next);
		productDesc = (EditText) findViewById(R.id.product_desc);
		next = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(productDesc.getText().toString())) {
					productDesc.requestFocus();
					productDesc.setError("请填写产品描述");
					return;
				}
				if (TextUtils.isEmpty(address.getText().toString())) {
					address.requestFocus();
					address.setError("请填写地址");
					return;
				}
				if (TextUtils.isEmpty(price.getText().toString())) {
					price.requestFocus();
					price.setError("请填写进货价");
					return;
				}
				if (TextUtils.isEmpty(price2.getText().toString())) {
					price2.requestFocus();
					price2.setError("请填写代理价");
					return;
				}
				if (Bimp.drr.size() == 0) {
					Toast.makeText(PublishedActivity.this, "请添加商品图片",
							Toast.LENGTH_LONG).show();
					return;
				}
				toggle();
				/*
				 * Intent intent = new Intent(PublishedActivity.this,
				 * ChooiceActivity.class); startActivityForResult(intent,
				 * 0x123);
				 */
			}

		};
		send = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				config = proxyProductFragment.getConfig();
				if (TextUtils.isEmpty(config.getBrandId())) {
					Toast.makeText(PublishedActivity.this, "请选择品牌",
							Toast.LENGTH_LONG).show();
					return;
				}

				
				if (TextUtils.isEmpty(config.getAfterSalesId())) {
					Toast.makeText(PublishedActivity.this, "请选择售后",
							Toast.LENGTH_LONG).show();
					return;
				}
				 
				send();
			}
		};
		chooiceNext.setOnClickListener(next);

	}

	private void toggle() {
		if (fragmentContainer.isShown()) {
			fragmentContainer.setVisibility(View.INVISIBLE);
			chooiceNext.setOnClickListener(next);
			chooiceNext.setText("下一步");
		} else {
			fragmentContainer.setVisibility(View.VISIBLE);
			chooiceNext.setOnClickListener(send);
			chooiceNext.setText("发布产品");
		}
	}

	private void send() {
		CustomLoadingDialog.showProgress(PublishedActivity.this, null,
				"正在发布产品", false, true);
		// 高清的压缩图片全部就在 list 路径里面了
		// 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面
		new Thread() {
			@Override
			public void run() {
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).substring(
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					list.add(FileUtils.SDPATH + Str + ".JPEG");
				}
				try {
					post(list,
							"http://api.lady365.com.cn/admin/product/create.htm",
							handler);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		Bimp.drr.clear();
		Bimp.max = 0;
		Bimp.bmp.clear();
		super.onDestroy();
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAsDropDown(parent);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PublishedActivity.this,
							LocalPicActivity.class);
					startActivityForResult(intent, 0x124);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";
	private SearchConfig config = new SearchConfig();

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String string = Environment.getExternalStorageDirectory() + "/myimage/";
		File f = new File(string);
		f.mkdirs();
		File file = new File(f, String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
		case 0x123:
			if (resultCode == 0 && data != null) {
				this.config = (SearchConfig) data
						.getSerializableExtra("config");
			} else {

			}
			break;
		case 0x124:
			if (resultCode == 0 && data != null) {

			}
		}
	}

	public String post(List<String> pathToOurFile, String urlServer,
			Handler handler) throws ClientProtocolException, IOException,
			JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		// 设置通信协议版本
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		// File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径

		// String pathToOurFile = path.getPath()+File.separator+"ak.txt";
		// //uploadfile
		// String urlServer = "http://192.168.1.88/test/upload.php";
		String requestJson = toJson(config);
		Log.i("config json", requestJson);
		MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
		HttpPost httppost = new HttpPost(urlServer);
		for (String files : pathToOurFile) {
			String path = createFilePath(files);
			String fileName = getFileName(files);
			File file = new File(path, fileName);
			ContentBody cbFile = new FileBody(file);
			mpEntity.addPart("Filedata", cbFile);
		}
		mpEntity.addPart("json", new StringBody(requestJson));
		httppost.setEntity(mpEntity);
		System.out.println("executing request " + httppost.getRequestLine());

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println(response.getStatusLine());// 通信Ok
		String json = "";
		String code = "";
		if (resEntity != null) {
			MobclickAgent.onEvent(this, "uploadproduct");
			// 完成上传服务器后 .........
			FileUtils.deleteDir();
			CustomLoadingDialog.dismissDialog();

			try {
				json = EntityUtils.toString(resEntity, "utf-8");
				JSONObject p = null;
				p = new JSONObject(json);
				code = (String) p.get("errorCode");
				Message msg = Message.obtain();
				msg.what = 0;
				msg.obj = (String) p.get("message");
				handler.sendMessage(msg);
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (resEntity != null) {
			resEntity.consumeContent();
		}

		httpclient.getConnectionManager().shutdown();
		return path;
	}

	private String getFileName(String files) {
		return files.substring(files.lastIndexOf("/") + 1, files.length());
	}

	private String createFilePath(String files) {
		String path = files.substring(0, files.lastIndexOf("/"));
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
		return path;
	}

	private String toJson(SearchConfig config) {
		ChooiceBean bean = new ChooiceBean();
		bean.setAddress(address.getText().toString());
		bean.setAttrs(config.getAttribute());
		bean.setAfterSalesId(config.getAfterSalesId());
		bean.setBrandId(config.getBrandId());
		bean.setCategoryId(config.getCategoryId());
		bean.setContent(productDesc.getText().toString());
		bean.setDeviceId(DeviceTool.getUniqueId(this));
		bean.setPrcie2(price2.getText().toString());
		bean.setPrice(price.getText().toString());
		bean.setToken(LoginMessageDataUtils.getToken(this));
		bean.setSyncALJJ(isSyncALJJ);
		Gson gson = new Gson();
		return gson.toJson(bean);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (fragmentContainer.isShown()) {
				toggle();
			} else {
				conform();
			}
			return true;
		} else {
			return false;
		}
	}

	private void conform() {
		Dialog dialog = null;
		ConformDialog.Builder customBuilder = new ConformDialog.Builder(
				PublishedActivity.this);
		customBuilder.setTitle("提示").setMessage("是否要放弃编辑?")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 退出dialog
						dialog.dismiss();
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
						dialog.dismiss();
					}

				});
		dialog = customBuilder.create();
		dialog.show();
	}

	class SpinnerAdapter extends BaseAdapter {

		private List<Item> data;
		private HashMap<Integer, Boolean> choiceMap = new HashMap<Integer, Boolean>();

		public SpinnerAdapter(List<Item> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public String getItemIdForStr(int position) {
			return String.valueOf(data.get(position).getSortId());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = PublishedActivity.this.getLayoutInflater()
						.inflate(R.layout.spinner_content_two, null);
				holder.name = (TextView) convertView
						.findViewById(R.id.spinner_content_dangkou);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(data.get(position).getSortName());
			return convertView;
		}

		final class ViewHolder {
			TextView name;
		}
	}
}
