package com.daixiaobao.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.daixiaobao.R;
import com.daixiaobao.categroy.RequestCategroy;
import com.daixiaobao.categroy.ResponseCatagroy;
import com.daixiaobao.categroy.ResponseCatagroy.Group;
import com.daixiaobao.filter.FilterController;
import com.daixiaobao.filter.FilterController.OnFinishFilterListener;
import com.daixiaobao.filter.SearchConfig;
import com.daixiaobao.other.CatagroyHelper;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

/**
 * 可代理产品
 * @author Administrator
 *
 */
public class ProxyProductActivity extends SherlockFragmentActivity {
	
	private int storeId;
	private ActionBar supportActionBar;
	private String businessName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storeId = getIntent().getIntExtra("storeId", -1);
		businessName = getIntent().getStringExtra("businessName");
		// set the Above View
		setContentView(R.layout.activity_proxy_product);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content, ProxyProductFragment.newInstance(storeId, businessName)).commit();
		((TextView)findViewById(R.id.custon_title)).setText(businessName);
		findViewById(R.id.custon_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
