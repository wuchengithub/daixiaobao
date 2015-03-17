package com.daixiaobao.detailCompany;

import com.daixiaobao.BaseApplication;
import com.daixiaobao.R;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyDetailActivity extends Activity{

	private TextView companyId;
	private TextView description;
	private TextView companyName;
	private ImageView image;
	private String code;

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				ResponseDetailCompany obj = null;
				if(msg.obj == null){
					
				}else {
					obj = (ResponseDetailCompany)msg.obj;
					companyId.setText("ID:" + obj.getCompanyId());
					description.setText(obj.toMessageGroupString());
					companyName.setText(obj.getCompany());
					//BaseApplication.imageLoader.displayImage(obj.get, imageView);
				}
				
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_detail);
		code = getIntent().getStringExtra("code");
		companyId = (TextView)findViewById(R.id.detail_company_id);
		description = (TextView)findViewById(R.id.detail_company_description);
		companyName = (TextView)findViewById(R.id.detail_company_name);
		image = (ImageView)findViewById(R.id.detail_company_pic);
		
		requestDetailData();
	}

	private void requestDetailData() {
		// TODO Auto-generated method stub
		CompanyDetailProtocol protocol = new CompanyDetailProtocol();
		RequestDetailCompany model = new RequestDetailCompany(LoginMessageDataUtils.getToken(this), 
				LoginMessageDataUtils.getUID(this), DeviceTool.getUniqueId(this), 
				code);
		protocol.invoke(model, handler);
	}
	
	
}
