package com.daixiaobao.filter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Toast;

import com.daixiaobao.R;
import com.daixiaobao.R.id;
import com.daixiaobao.R.layout;
import com.wookii.tools.comm.DateTool;

public class DateTimePickerActivity extends Activity {
	public static final int responseCode = 3;
	private static final String TAG = "DataTimeActivity";
	private DatePicker datePicker;
	private Button okButton, resetButton;
	private int year = 0;
	private int month= 0;
	private int day= 0;
	private String getDateTime;

	private static final int MONTH = 1;
	boolean bool = true;
	void init() {
		datePicker.init(year, month - MONTH, day, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int month,
					int day) {
				DateTimePickerActivity.this.year = year;
				DateTimePickerActivity.this.month = month + MONTH;
				DateTimePickerActivity.this.day = day;
				bool = true;
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.date_time);
		Intent intent = this.getIntent();
		getDateTime = intent.getStringExtra("dateTime");
		datePicker = (DatePicker) findViewById(R.id.date);
		okButton = (Button) findViewById(R.id.data_time_close);
		resetButton = (Button) findViewById(R.id.data_time_reset);
		if (getDateTime != null) {
			getDataTime();
		} else {
			showDataTime();
		}
		init();
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(bool){
					Intent data = new Intent();
					data.putExtra("dataTime", year + "-" + month + "-" + day );
					DateTimePickerActivity.this.setResult(responseCode, data);
					DateTimePickerActivity.this.finish();
				}else{
					Toast.makeText(DateTimePickerActivity.this, "格式不正确", Toast.LENGTH_LONG).show();
				}
				
			}
		});
	
		resetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDataTime();
				init();
			}
		});
	}

	private void getDataTime() {
		String[] dateTiem = getDateTime.split(" ");
		String[] date = dateTiem[0].split("-");
		String[] time = dateTiem[1].split(":");
		year = Integer.parseInt(date[0]);
		month = Integer.parseInt(date[1]);
		day = Integer.parseInt(date[2]);
	}

	private void showDataTime() {
		String showDate =DateTool.getCurrentTime("yyyy-MM-dd");
		String[] dateTiem = showDate.split(" ");
		String[] date = dateTiem[0].split("-");
		year = Integer.parseInt(date[0]);
		month = Integer.parseInt(date[1]);
		day = Integer.parseInt(date[2]);
	}
}
