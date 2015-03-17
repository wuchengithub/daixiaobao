package com.example.testpic;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.daixiaobao.R;
import com.daixiaobao.proxy.ProxyProductFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ChooiceActivity  extends SherlockFragmentActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(com.daixiaobao.R.layout.choocie_activity);
		FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
		FragmentTransaction replace = beginTransaction.replace(R.id.chooice_content, ProxyProductFragment.newInstance(-2,""));
		replace.commit();
		
	}
}
