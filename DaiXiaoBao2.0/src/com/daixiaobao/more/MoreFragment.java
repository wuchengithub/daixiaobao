package com.daixiaobao.more;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daixiaobao.CommonUtil;
import com.daixiaobao.R;
import com.daixiaobao.other.VersionHelper;
import com.daixiaobao.widget.CustomLoadingDialog;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.wookii.WookiiSDKManager;
import com.wookii.utils.LoginMessageDataUtils;

/**
 * 代理商
 * 
 * @author Administrator
 * 
 */
public class MoreFragment extends Fragment {

	private static final int ABOUT = 0;
	private static final int CANCELLATION = 1;
	private static final int CHECK_VERSION = 2;
	private static final int HELP = 3;
	private static final int MODIFY_PASSWORD = 4;
	private static final int OPINION = 5;
	private Button about;
	private Button cancellation;
	private Button checkVersion;
	private Button help;
	private Button modifyPassword;
	private Button opinion;
	private ImageView dimensionalCode;
	private Activity context;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onAttach(Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_more, container, false);
		about = (Button) view.findViewById(R.id.more_about);
		about.setOnClickListener(new MyButtonCilckListener(ABOUT));
		cancellation = (Button) view.findViewById(R.id.more_cancellation);
		cancellation
				.setOnClickListener(new MyButtonCilckListener(CANCELLATION));
		checkVersion = (Button) view.findViewById(R.id.more_check_version);
		checkVersion.setText("当前版本（" + 
		VersionHelper.getLocalVersions(getActivity()).split(VersionHelper.REGULAR_EXPRESSION)[1] + ")");
		checkVersion
				.setOnClickListener(new MyButtonCilckListener(CHECK_VERSION));
		help = (Button) view.findViewById(R.id.more_help);
		help.setOnClickListener(new MyButtonCilckListener(HELP));
		modifyPassword = (Button) view.findViewById(R.id.more_modify_password);
		modifyPassword.setOnClickListener(new MyButtonCilckListener(
				MODIFY_PASSWORD));
		opinion = (Button) view.findViewById(R.id.more_opinion);
		opinion.setOnClickListener(new MyButtonCilckListener(OPINION));
		dimensionalCode = (ImageView) view
				.findViewById(R.id.more_two_dimensional_code);
		Button b = (Button) view.findViewById(R.id.more_login);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonUtil.openLoginView(getActivity(),
						CommonUtil.LOGIN_REQUEST_CODE);
			}
		});
		return view;
	}

	class MyButtonCilckListener implements View.OnClickListener {

		private int what;

		public MyButtonCilckListener(int what) {
			this.what = what;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (what) {
			case ABOUT:
				Intent about = new Intent("android.intent.action.about");
				getActivity().startActivity(about);
				break;
			case CANCELLATION:
				cancellation();
				break;
			case CHECK_VERSION:
				CustomLoadingDialog.showProgress(context, "", "正在检查更新", false, true);
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				    @Override
				    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
				        switch (updateStatus) {
				        case UpdateStatus.Yes: // has update
				            UmengUpdateAgent.showUpdateDialog(context, updateInfo);
				            break;
				        case UpdateStatus.No: // has no update
				            Toast.makeText(context, "没有更新", Toast.LENGTH_SHORT).show();
				            break;
				        case UpdateStatus.NoneWifi: // none wifi
				            Toast.makeText(context, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
				            break;
				        case UpdateStatus.Timeout: // time out
				            Toast.makeText(context, "超时", Toast.LENGTH_SHORT).show();
				            break;
				        }
				        CustomLoadingDialog.dismissDialog();
				    }

				});
				UmengUpdateAgent.forceUpdate(context);
				break;
			case HELP:

				break;
			case MODIFY_PASSWORD:
				WookiiSDKManager.openModifyPasswordView(getActivity(), WookiiSDKManager.MODIFY_PASSWORD_REQUEST_CODE);
				break;
			case OPINION:
				FeedbackAgent agent = new FeedbackAgent(getActivity());
			    agent.startFeedbackActivity();
				break;
			default:
				break;
			}
		}

	}

	public void cancellation() {
		// TODO Auto-generated method stub
		LoginMessageDataUtils.insertToken(getActivity(), null);
		CommonUtil.openLoginView(getActivity(), CommonUtil.LOGIN_REQUEST_CODE);
	}
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}
}
