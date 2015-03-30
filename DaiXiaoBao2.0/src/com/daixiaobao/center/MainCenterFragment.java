package com.daixiaobao.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daixiaobao.BaseApplication;
import com.daixiaobao.MainActivity;
import com.daixiaobao.R;
import com.daixiaobao.center.ResponseCenterMain.Detail;
import com.daixiaobao.protocol.MyBaseProtocol;
import com.daixiaobao.widget.CircleImageView;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.wookii.protocollManager.ProtocolManager;
import com.wookii.utils.DeviceTool;
import com.wookii.utils.LoginMessageDataUtils;

/**
 * 
 * @author wood
 * 
 */
public class MainCenterFragment extends Fragment {

	private static final String TAG = "MainCenterFragment";
	private FragmentActivity context;
	private Handler handler = new Handler (){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ProtocolManager.NOTIFICATION:
				if(msg.obj == null)break;
				ResponseCenterMain obj = (ResponseCenterMain)msg.obj;
				int code = obj.getErrorCode();
				if(code == ProtocolManager.ERROR_CODE_ZORE){
					Detail detail = obj.getDetail();
					//name
					TextView nameTX = (TextView)view.findViewById(R.id.center_main_name);
					nameTX.setText(detail.getUserLoginId());
					//iamge
					/*CircleImageView image = (CircleImageView)view.findViewById(R.id.center_main_image);
					BaseApplication.imageLoader.displayImage(detail.getImageUrl(), image);*/
					//proxyLevel
					//TextView proxyLevel = (TextView)view.findViewById(R.id.center_main_proxylevel);
					//proxyLevel.setText(detail.getValid());
					//myProxy
					//TextView myProxy = (TextView)view.findViewById(R.id.center_main_my_product_count);
					//myProxy.setText(detail.getProductLimit());
					//spreadCount
					//TextView spread = (TextView)view.findViewById(R.id.center_main_spread_count);
					//spread.setText(detail.getFriendsLimit());
				} else {
					
				}
				break;

			default:
				break;
			}
		};
	};
	private View view;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		////Log.i(TAG, "onActivityCreated");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		////Log.i(TAG, "onActivityResult");
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		////Log.i(TAG, "onAttach");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		////Log.i(TAG, "onCreate");
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.context = getActivity();
		////Log.i(TAG, "onCreateView");
		view = inflater.inflate(R.layout.center_main_fragment_content,
				container, false);
		
		
		
		//initFunctionList(view);
		
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setOnOpenListener(new OnOpenListener() {
			
			@Override
			public void onOpen() {
				// TODO Auto-generated method stub
				getDataFromServer();
			}
		});
		return view;
	}
	
	private SlidingMenu getSlidingMenu(){
		return ((MainActivity)getActivity()).getSlidingMenu();
	}

	private void getDataFromServer() {
		MyBaseProtocol protocol = new CenterMainProtocol();
		protocol.invoke(new RequestCenterMain(LoginMessageDataUtils.getToken(context),
				LoginMessageDataUtils.getUID(context), 
				DeviceTool.getUniqueId(context)), 
				handler);
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		////Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		////Log.i(TAG, "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		////Log.i(TAG, "onDetach");
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		////Log.i(TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		////Log.i(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		////Log.i(TAG, "onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		////Log.i(TAG, "onStop");
		super.onStop();
	}

}
