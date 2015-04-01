package com.daixiaobao.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.daixiaobao.R;
import com.daixiaobao.db.DBHelper;
import com.daixiaobao.filter.SearchConfig.SearchAttribute;
import com.daixiaobao.greenrobot.AfterSales;
import com.daixiaobao.greenrobot.Attrb;
import com.daixiaobao.greenrobot.Brand;
import com.daixiaobao.greenrobot.Feature;
import com.daixiaobao.search.AttributeProtocol;
import com.daixiaobao.search.AttributeBean;
import com.daixiaobao.search.AttributeBean.Group;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.daixiaobao.widget.IconButton;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

@SuppressLint("NewApi")
public class FilterController implements OnItemSelectedListener, OnQueryTextListener{

	public interface OnFinishFilterListener {
		public void onFinish(SearchConfig config);
	}
	
	protected static final String TAG = "FilterController";
	private EditText code;
	private EditText beginTime;
	private EditText endTime;
	private LinearLayout rootUI;
	private Activity context;
	private ActionBar actionBar;
	private OnFinishFilterListener listener;
	private String codeStr;
	private PopupWindow mPopupWindow;
	protected AttributeBean data;
	private View popupView;
	protected Spinner brandView;
	private LinearLayout rootLayout;
	private SearchConfig searchConfig;
	private HashMap<String, String> attrMap;
	public static final int FILTER_REQUEST_START = 0x123;
	public static final int FILTER_REQUEST_END = 0x124;
	public IconButton submit;
	public IconButton clear;
	public ArrayList<Spinner> spinnerList = new ArrayList<Spinner>();
	private SearchView searchView;
	protected Spinner afterSalesView;
	public FilterController(View view, SherlockFragment context) {
		// TODO Auto-generated constructor stub
	}

	public FilterController(Activity context, String codeStr) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.codeStr = codeStr;
		this.searchConfig = new SearchConfig();
		searchConfig.setCategoryId(codeStr);
		this.attrMap = new HashMap<String, String>();
		popupView = context.findViewById(R.id.search_popup_window_content);
		searchView = (SearchView)context.findViewById(R.id.product_search);
		int palteId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		View palteView = searchView.findViewById(palteId);
		if (palteView != null) {
			palteView.setBackgroundResource(R.color.WHITE);
		}
		searchView.setQueryHint("输入编号或关键字...");
		searchView.setOnQueryTextListener(this);
		// searchView.setOnSuggestionListener(this);
		searchView.setIconifiedByDefault(false);
		searchView.setSubmitButtonEnabled(true);
		searchView.setFocusable(false);
	        
		rootLayout = (LinearLayout)popupView.findViewById(R.id.search_popup_window_content_root);
		submit = (IconButton)popupView.findViewById(R.id.search_popup_window_content_submit);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchConfig = buildingSearchConfig();
				listener.onFinish(searchConfig);
				
			}
		});
		clear = (IconButton)popupView.findViewById(R.id.search_popup_window_content_clear);
		clear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (Spinner item : spinnerList) {
					item.setSelection(0);
				}
			}
		});
		rootLayout.removeAllViews();
		
	}
	
	public SearchConfig buildingSearchConfig() {
		final Set<String> keySet = attrMap.keySet();
		final Iterator<String> iterator = keySet.iterator();
		SearchAttribute[] array = new SearchAttribute[attrMap.size()];
		int i = 0;
		while (iterator.hasNext()) {
			final String key = iterator.next();
			final SearchAttribute searchAttribute = searchConfig.new SearchAttribute();
			searchAttribute.setFeatureTypeId(key);
			searchAttribute.setFeatureId(attrMap.get(key));
			array[i] = searchAttribute;
			i++;
		}
		searchConfig.setAttribute(array);
		return searchConfig;
	}
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				spinnerList.clear();
				data = (AttributeBean)msg.obj;
				if(data != null && data.getErrorCode() == ProtocolManager.ERROR_CODE_ZORE){
					Group group = data.getData();
					final List<Brand> brands = group.getBrands();
					final List<AfterSales> afterSales = group.getAfterSales();
					//创建属性group的标题
					TextView brandName = new TextView(context);
					brandName.setText("品牌");
					brandName.setTextColor(context.getResources().getColor(R.color.new_shenhui));
					rootLayout.addView(brandName, 0);
					//添加品牌
					brandView = (Spinner)context.getLayoutInflater().inflate(R.layout.layout_spinner, null);
					spinnerList.add(brandView);
					List<Map<String, String>>  brandData = new ArrayList<Map<String, String>>();
					for (Brand brand : brands) {
						Map<String,String> idValueMap = new HashMap<String, String>();
						idValueMap.put("id", brand.getBrandId());
						idValueMap.put("name", brand.getName());
						brandData.add(idValueMap);
					}
					//将可选内容与ArrayAdapter连接起来
					MyAttrAdapter brandAdapter = new MyAttrAdapter(brandData);
					brandView.setAdapter(brandAdapter);
					brandView.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							String id = ((MyAttrAdapter)brandView.getAdapter()).getItemIdForStr(arg2);
							((MyAttrAdapter)brandView.getAdapter()).changeChoiceStyle(arg2);
							searchConfig.setBrandId(id);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
					rootLayout.addView(brandView, 1);
					//添加售后
					//创建属性group的标题
					TextView afterSaleName = new TextView(context);
					afterSaleName.setText("选择售后");
					afterSaleName.setTextColor(context.getResources().getColor(R.color.new_shenhui));
					rootLayout.addView(afterSaleName, 2);
					afterSalesView = (Spinner)context.getLayoutInflater().inflate(R.layout.layout_spinner, null);
					spinnerList.add(afterSalesView);
					List<Map<String, String>>  afterSalesData = new ArrayList<Map<String, String>>();
					for (AfterSales afterSale : afterSales) {
						Map<String,String> idValueMap = new HashMap<String, String>();
						idValueMap.put("id", afterSale.getAfterSalesId());
						idValueMap.put("name", afterSale.getName());
						afterSalesData.add(idValueMap);
					}
					//将可选内容与ArrayAdapter连接起来
					MyAttrAdapter afterSalesAdapter = new MyAttrAdapter(afterSalesData);
					afterSalesView.setAdapter(afterSalesAdapter);
					afterSalesView.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							String id = ((MyAttrAdapter)afterSalesView.getAdapter()).getItemIdForStr(arg2);
							((MyAttrAdapter)afterSalesView.getAdapter()).changeChoiceStyle(arg2);
							searchConfig.setAfterSalesId(id);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
					rootLayout.addView(afterSalesView, 3);
					//添加属性
					final List<Attrb> attributes = group.getAttribute();
					for (Attrb attrb : attributes) {
						final String featureTypeName = attrb.getFeatureTypeName();
						//创建属性group
						final Spinner arrtSpinner = (Spinner)context.getLayoutInflater().inflate(R.layout.layout_spinner, null);
						spinnerList.add(arrtSpinner);
						arrtSpinner.setId(Integer.parseInt(attrb.getFeatureTypeId()));
						arrtSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								String itemIdForStr = ((MyAttrAdapter)arg0.getAdapter()).getItemIdForStr(arg2);
								String key = String.valueOf(arg0.getId());
								attrMap.put(key, itemIdForStr);
								((MyAttrAdapter)arrtSpinner.getAdapter()).changeChoiceStyle(arg2);
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub
								
							}
						});
						rootLayout.addView(arrtSpinner, 4);
						//创建group的child
						List<Map<String, String>>  arrtData = new ArrayList<Map<String, String>>();
						final List<Feature> features = attrb.getFeatures();
						for (Feature feature : features) {
							Map<String,String> idValueMap = new HashMap<String, String>();
							idValueMap.put("id", feature.getFeatureId());
							idValueMap.put("name", feature.getFeatureName());
							arrtData.add(idValueMap);
						}
						//将可选内容与ArrayAdapter连接起来
						MyAttrAdapter attrAdapter = new MyAttrAdapter(arrtData);
						arrtSpinner.setAdapter(attrAdapter);
						TextView name = new TextView(context);
						//创建属性group的标题
						name.setText(featureTypeName);
						name.setTextColor(context.getResources().getColor(R.color.new_shenhui));
						rootLayout.addView(name, 4);
						
					}
				}
				break;

			default:
				break;
			}
			CustomLoadingDialog.dismissDialog();
			//mPopupWindow.showAsDropDown(context.findViewById(R.id.mine_concern_order_toggle), 0, -20);
		};
	};
	private void init() {
		// TODO Auto-generated method stub
		context.getResources().getColor(R.color.text);
		beginTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				context.startActivityForResult(new Intent("android.intent.action.picker"), FILTER_REQUEST_START);
			}
		});
		
		endTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				context.startActivityForResult(new Intent("android.intent.action.picker"), FILTER_REQUEST_END);
			}
		});;
	}

	public void toggle(MenuItem item) {
		// TODO Auto-generated method stub
		if(data == null){
			CustomLoadingDialog.showProgress(context, "", "正在执行", false, true);
			getDataFromDB();
		}  else {
			//mPopupWindow.showAsDropDown(context.findViewById(R.id.mine_concern_order_toggle));
		}
	}

	public void getDataFromDB() {
		// TODO Auto-generated method stub
		//CustomLoadingDialog.showProgress(context, "", "正在执行", false, true);
		new Thread() {
			public void run() {
				AttributeBean allAttribute = DBHelper.getInstance(context).getAllAttribute(codeStr);
				Message msg = Message.obtain();
				msg.what = ProtocolManager.NOTIFICATION;
				msg.obj = allAttribute;
				handler.sendMessage(msg);
			};
		}.start();
		
		
		/*protocol = new AttributeProtocol();
		protocol.invoke(new AttributeBean(LoginMessageDataUtils.getToken(context), LoginMessageDataUtils.getUID(context), 
				DeviceTool.getDeviceId(context), codeStr), handler);*/
	}


	public void setOnFinishFilterListener(
			OnFinishFilterListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}

	public void setCodeStr(String codeStr) {
		// TODO Auto-generated method stub
		this.codeStr = codeStr;
	}
	
	
	class MyAttrAdapter extends BaseAdapter {

		private List<Map<String, String>> data;
		private HashMap<Integer, Boolean> choiceMap = new HashMap<Integer, Boolean>();
		public MyAttrAdapter(List<Map<String,String>> data){
			this.data = data;
		} 
		
		public void changeChoiceStyle(int position) {
			// TODO Auto-generated method stub
			for (Integer key : choiceMap.keySet()) {
				choiceMap.put(key, false);
			}
			choiceMap.put(position, true);
			this.notifyDataSetChanged();
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

		public String getItemIdForStr(int position){
			return data.get(position).get("id");
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = context.getLayoutInflater().inflate(
						R.layout.spinner_content, null);
				holder.name = (TextView) convertView
						.findViewById(R.id.spinner_content_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(data.get(position).get("name"));
			if(choiceMap.get(position) == null || choiceMap.get(position) == false){
				holder.name.setBackgroundColor(0);
				choiceMap.put(position, false);
			} else {
				if(position != 0){
					holder.name.setTextColor(context.getResources().getColor(R.color.new_yese));
				}
			}
			return convertView;
		}
		final class ViewHolder {
			TextView name;
		}
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		SearchConfig searchConfig = new SearchConfig();
		searchConfig.setProductCode(query);
		listener.onFinish(searchConfig);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
	
	protected void openDetailView(String productCode) {
		// TODO Auto-generated method stub
		Intent intent = new Intent("android.intent.action.proxyList");
		SearchConfig config = new SearchConfig();
		config.setProductCode(productCode);
		intent.putExtra("config", config);
		context.startActivity(intent);
	}

	public SearchConfig getConfig() {
		// TODO Auto-generated method stub
		buildingSearchConfig();
		return searchConfig;
	}
}
