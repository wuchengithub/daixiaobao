package com.daixiaobao.widget;




import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;

import com.daixiaobao.R;

/**
 * 
 * @author iteye whyhappy01
 *
 */
public class FlowIndicator extends View {
	private int count;
	private float space, radius;
	private int point_normal_color, point_seleted_color;
	public static boolean DEBUG = true;
	private Context context;
	private int seleted = 0;

	// background seleted normal

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x123:
				if(count > 1){
					next();
				}
				break;
			default:
				break;
			}
		};
	};
	private OnSelectionChangeListener onSelectionChangeListener;
	private boolean isRotation;
	private Timer timer;
	private TextSwitcher adGameNameSwitcher;
	private String textString;
	private ArrayList<String> textList;
	public FlowIndicator(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.FlowIndicator);

		count = a.getInteger(R.styleable.FlowIndicator_count, 4);
		space = a.getDimension(R.styleable.FlowIndicator_space, 9);
		radius = a.getDimension(R.styleable.FlowIndicator_point_radius, 9);

		point_normal_color = a.getColor(
				R.styleable.FlowIndicator_point_normal_color, 0x000000);
		point_seleted_color = a.getColor(
				R.styleable.FlowIndicator_point_seleted_color, 0xffff07);

		int sum = attrs.getAttributeCount();
		if (DEBUG) {
			String str = "";
			for (int i = 0; i < sum; i++) {
				String name = attrs.getAttributeName(i);
				String value = attrs.getAttributeValue(i);
				str += "attr_name :" + name + ": " + value + "\n";
			}
		}
		a.recycle();
	}

	public void setRotation(boolean isRotation){
		this.isRotation = isRotation;
	}
	public void startRotation(){
		if(!isRotation) return;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0x123);
			}
		}, 1500, 5000);
	}
	
	public void cancelRotation(){
		try {
			if(timer != null) {
				timer.cancel();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSeletion(int index) {
		this.seleted = index;
		invalidate();
	}

	public void setCount(int count) {
		this.count = count;
		invalidate();
	}
	
	public void nextText(int positon) {
		try {
			adGameNameSwitcher.setText(textList.get(positon));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void next() {
		if (seleted < count - 1)
			seleted++;
		else
			seleted = 0;
		if(onSelectionChangeListener != null) {
			onSelectionChangeListener.onSelection(seleted);
		}
		invalidate();
	}

	public void previous() {
		if (seleted > 0)
			seleted--;
		else
			seleted = count - 1;
		if(onSelectionChangeListener != null) {
			onSelectionChangeListener.onSelection(seleted);
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		/*Display dm = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 

		int systemWidth = dm.getWidth();*/
	 

		float width = (getWidth() - ((radius * 2 * count) + (space * (count - 1)))) / 2.f;
		//float width = (systemWidth - ((radius * 2 * count) + (space * (count - 1)))) / 2.f;

		for (int i = 0; i < count; i++) {
			if (i == seleted)
				paint.setColor(point_seleted_color);
			else
				paint.setColor(point_normal_color);
			/*canvas.drawCircle(width*1.8f + getPaddingLeft() + radius +i
					* (space + radius + radius), getHeight()/ 2, radius, paint);*/
			canvas.drawCircle(width + getPaddingLeft() + radius +i
					* (space + radius + radius), getHeight()/ 2, radius, paint);

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (getPaddingLeft() + getPaddingRight()
					+ (count * 2 * radius) + (count - 1) * radius + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (2 * radius + getPaddingTop() + getPaddingBottom() + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	public interface OnSelectionChangeListener{
		public void onSelection(int index);
	}

	public void setOnSelectionChangeListener(
			OnSelectionChangeListener onSelectionChangeListener) {
		// TODO Auto-generated method stub
		this.onSelectionChangeListener = onSelectionChangeListener;
	}

	public void setTextSwitcher(TextSwitcher adGameNameSwitcher) {
		// TODO Auto-generated method stub
		this.adGameNameSwitcher = adGameNameSwitcher;
	}

	public void addText(String string) {
		// TODO Auto-generated method stub
		if(textList == null){
			textList = new ArrayList<String>();
			//adGameNameSwitcher.setCurrentText(string);
		}
		textList.add(string);
	}
	
	
}
