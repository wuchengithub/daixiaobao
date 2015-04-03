package com.daixiaobao.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daixiaobao.BaseApplication;
import com.daixiaobao.R;
import com.daixiaobao.concern.change.RequestConcernChange;
import com.daixiaobao.concern.change.ResponseConcernChange;
import com.daixiaobao.other.ChangePriceHelper;
import com.daixiaobao.other.ShareCallBackHelper;
import com.daixiaobao.other.ShareCallBackHelper.OnShareCallBackListener;
import com.daixiaobao.proxy.list.ProxyListActivity.OnProductConcernChangeListener;
import com.daixiaobao.proxy.list.ResponseProxyList.Group;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

public class ProxyListAdapter extends BaseAdapter {

	public static final String STATUS_PROXY = "0";
	public static final String STATUS_UN_PROXY = "1";
	private static final String TAG = "MyProductAdapter";
	private LayoutInflater layoutInflater;
	private List<Group> data;
	public static HashMap<Integer, Boolean> checkedMap;
	public static HashMap<Integer, Integer> expandMap;
	private OnProductConcernChangeListener listener;
	private Resources resources;
	private Context context;
	int currentImageIndex = 0;
	public ProxyListAdapter(List<Group> data, Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		layoutInflater = ((Activity) context).getLayoutInflater();
		this.resources = context.getResources();
		this.data = data;
		checkedMap = new HashMap<Integer,Boolean>();
		expandMap = new HashMap<Integer, Integer>();
		Log.i(TAG, "data.size()" + data.size());
		int i = 0;
		for(Group item : data){
			if(item == null) continue;
			//0为已代理，1为未代理
			boolean checked = false;
			if (STATUS_PROXY.equals(item.getStatus())) {
				checked = true;
			}
			checkedMap.put(i, checked);
			expandMap.put(i, View.GONE);
			i++;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	private synchronized void setCurrentImageIndex(int currentImageIndex) {
		// TODO Auto-generated method stub
		this.currentImageIndex = currentImageIndex;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.product_list_content, null);
			holder.code = (TextView)convertView.findViewById(R.id.product_list_content_product_code);
			holder.dangkou = (TextView)convertView.findViewById(R.id.product_list_content_dangkou);
			holder.concernToggle = (CheckBox)convertView.findViewById(R.id.product_list_content_concern);
			holder.expandToggle = (CheckBox)convertView.findViewById(R.id.product_list_content_expand);
			holder.address = (TextView)convertView.findViewById(R.id.product_list_content_detail_address);
			holder.outDescription = (TextView)convertView.findViewById(R.id.product_list_content_detail_out_description);
			holder.image = (ImageView)convertView.findViewById(R.id.product_list_content_image);
			holder.price = (TextView)convertView.findViewById(R.id.company_share_total);
			holder.image_count = (TextView)convertView.findViewById(R.id.company_agent_total);
			holder.date = (TextView)convertView.findViewById(R.id.product_list_content_date);
			holder.attr = (TextView)convertView.findViewById(R.id.product_list_attr);
			holder.mesage_weixin = (TextView)convertView.findViewById(R.id.product_list_content_detail_message_weixin);
			holder.mesage_qq = (TextView)convertView.findViewById(R.id.product_list_content_detail_message_qq);
			holder.mesage_phone = (TextView)convertView.findViewById(R.id.product_list_content_detail_message_phone);
			holder.mesage_email = (TextView)convertView.findViewById(R.id.product_list_content_detail_message_email);
			holder.afterSales = (TextView)convertView.findViewById(R.id.product_list_content_after_sales);
			holder.share = (ImageView)convertView.findViewById(R.id.product_list_content_share);
			holder.detail = (View)convertView.findViewById(R.id.product_list_content_detail);
			holder.change = (ImageView)convertView.findViewById(R.id.product_list_content_change);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Group item = data.get(position);
		Log.i(TAG, "" + position);
		final String[] images = item.getImages();
		holder.detail.setVisibility(expandMap.get(position));
		holder.share.setOnClickListener(new View.OnClickListener() {
			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.dangkou.setTextColor(resources.getColor(R.color.red));
				currentImageIndex = 0;
				//BaseApplication.imageLoader.clearDiscCache();
				CustomLoadingDialog.showProgress(context, null, "加载图片……", false, true);
				ClipboardManager cmb = (ClipboardManager) context.getSystemService(((Activity)context).CLIPBOARD_SERVICE);
				cmb.setText(item.getDescription());
				loadImages(images,currentImageIndex);
			}
		});
		holder.code.setText("编号：" + item.getProductCode());
		holder.outDescription.setText(item.getDescription());
		
		StringBuilder attrstr = new StringBuilder(item.getBrandName()/* + ":"*/);
		Map<String, String> attrs = item.getAttrs();
		Iterator<String> iterator = attrs.keySet().iterator();
		/*while(iterator.hasNext()){
			String key = iterator.next();
			attrstr.append(key + "/");
			attrstr.append(attrs.get(key) + "  ");
		}*/
		holder.attr.setText(attrstr.toString());
		
		holder.address.setText(item.getAddress());
		holder.image_count.setText(item.getImageCount() + "张");
		holder.price.setText("￥" + item.getSellPrice());
		BaseApplication.imageLoader.displayImage(item.getImageUrl(), holder.image, BaseApplication.options);
		try {
			holder.concernToggle.setChecked(checkedMap.get(position));
			holder.expandToggle.setChecked(expandMap.get(position) == View.GONE ? true : false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.dangkou.setText("档口号：" + item.getAddress());
		holder.date.setText(item.getReleaseDate());
		holder.afterSales.setText(item.getAftermarket());
		if(!holder.concernToggle.isChecked()) {
			holder.concernToggle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					checkedMap.put(position, ((CheckBox)v).isChecked());
					ChangePriceHelper.collectAndChangePrice(context, new String[]{item.getCode()}, new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
						}
					}, item.getSellPrice());
					holder.concernToggle.setChecked(!((CheckBox)v).isChecked());
				}
			});
		} else {
			holder.concernToggle.setClickable(false);
		}
		
		holder.expandToggle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(expandMap.get(position) == View.VISIBLE){
					expandMap.put(position, View.GONE);
				} else {
					expandMap.put(position, View.VISIBLE);
				}
				holder.detail.setVisibility(expandMap.get(position));
			}
		});
		if(!TextUtils.isEmpty(item.getWeixinId())){
			holder.mesage_weixin.setText(Html.fromHtml("<a href='#'>"+item.getWeixinId()+"</a>"));
			holder.mesage_weixin.append("(点击复制)");
			holder.mesage_weixin.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ClipboardManager cmb = (ClipboardManager) context.getSystemService(((Activity)context).CLIPBOARD_SERVICE);
					cmb.setText(item.getDescription());
					Toast.makeText(context, "微信号已被复制！", Toast.LENGTH_LONG)
					.show();
					
				}
			});
		}
		
		if(!TextUtils.isEmpty(item.getQq())){
			holder.mesage_qq.setText(item.getQq());
		}
		
		if(!TextUtils.isEmpty(item.getTel())){
			holder.mesage_phone.setText(item.getTel());
		}
		
		if(!TextUtils.isEmpty(item.getEmail())){
			holder.mesage_email.setText(item.getEmail());
		}
		return convertView;
	}
	final class ViewHolder {

		public ImageView change;
		public TextView mesage_email;
		public TextView mesage_phone;
		public TextView mesage_qq;
		public TextView mesage_weixin;
		public TextView attr;
		public TextView address;
		public TextView outDescription;
		public TextView message;
		public View detail;
		public CheckBox expandToggle;
		public TextView afterSales;
		public TextView date;
		TextView dangkou;
		TextView image_count;
		ImageView image;
		TextView price;
		TextView description;
		CheckBox concernToggle;
		TextView code;
		ImageView share;

	}

	public void setOnProductCencernChangeListener(
			OnProductConcernChangeListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}

	private synchronized void openWeixin(String[]images, int index){
		setCurrentImageIndex(++ index);
		if (currentImageIndex == images.length) {
			DiscCacheAware discCache = BaseApplication.imageLoader
					.getDiscCache();
			ArrayList<Uri> imageArray = new ArrayList<Uri>();
			for (String key : images) {
				File file = discCache.get(key);
				imageArray.add(Uri.fromFile(file));
				Log.i(TAG, file.toString());
			}
			Intent intent = new Intent();
			ComponentName localComponentName = new ComponentName(
					"com.tencent.mm",
					"com.tencent.mm.ui.tools.ShareToTimeLineUI");
			intent.setComponent(localComponentName);
			intent.setAction("android.intent.action.SEND_MULTIPLE");
			intent.setType("image/*");
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageArray);
			try {
				((Activity) context).startActivityForResult(intent, 0x333);
				Toast.makeText(context, "请直接长按文本框粘贴产品描述！", Toast.LENGTH_LONG)
						.show();
			} catch (Exception e) {
				// TODO Auto-generated catch block

			}
			CustomLoadingDialog.dismissDialog();
		} else {
			loadImages(images, currentImageIndex);
		}
	}
	
	
	public void loadImages(final String[] images, final int index){
		BaseApplication.imageLoader.loadImage(images[index], BaseApplication.options,new ImageLoadingListener(){

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				openWeixin(images, index);
			}
			

			@Override
			public void onLoadingComplete(String imageUri,
					View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				openWeixin(images, index);
			}

			@Override
			public void onLoadingCancelled(String imageUri,
					View view) {
				// TODO Auto-generated method stub
				
			}});
	}
}
