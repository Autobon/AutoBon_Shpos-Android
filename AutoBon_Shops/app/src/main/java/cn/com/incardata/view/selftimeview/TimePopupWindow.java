package cn.com.incardata.view.selftimeview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.com.incardata.autobon_shops.R;
import cn.com.incardata.utils.DateCompute;

/**
 * 时间选择器
 * 
 * @author Sai
 * 
 */
public class TimePopupWindow extends PopupWindow implements OnClickListener {
	public enum Type {
		ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN
	}// 四种选择模式，年月日时分，年月日，时分，月日时分

	private Context context;
	private View rootView; // 总的布局
	WheelTime wheelTime;
	private View btnSubmit, btnCancel;
	private static final String TAG_SUBMIT = "submit";
	private static final String TAG_CANCEL = "cancel";
	private OnTimeSelectListener timeSelectListener;
    private TextView m_CurTime;
	public TimePopupWindow(Context context, Type type,TextView m_CurTime) {
		super(context);
		this.context = context;
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.timepopwindow_anim_style);
        this.m_CurTime=m_CurTime;
		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		rootView = mLayoutInflater.inflate(R.layout.pw_time, null);
		// -----确定和取消按钮
		btnSubmit = rootView.findViewById(R.id.btnSubmit);
		btnSubmit.setTag(TAG_SUBMIT);
		btnCancel = rootView.findViewById(R.id.btnCancel);
		btnCancel.setTag(TAG_CANCEL);
		btnSubmit.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		// ----时间转轮
		final View timepickerview = rootView.findViewById(R.id.timepicker);
		ScreenInfo screenInfo = new ScreenInfo((Activity) context);
		wheelTime = new WheelTime(timepickerview, type);
		wheelTime.screenheight = screenInfo.getHeight();
		
		
		//默认选中当前时间
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());
		
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		String time=this.m_CurTime.getText().toString()+" 00:00";
		String time=this.m_CurTime.getText().toString();
        if(JudgeDate.isDate(time, "yyyy-MM-dd HH:mm")){
             try {
                calendar.setTime(dateFormat.parse(time));
             } catch (ParseException e) {

             }
        }
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		wheelTime.setPicker(year, month, day, hours, minute);
		setCyclic(true);
		setContentView(rootView);

		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				closePopupWindow();
			}
		});
	}

	/**
	 * 设置可以选择的时间范围
	 * 
	 * @param START_YEAR
	 * @param END_YEAR
	 */
	public void setRange(int START_YEAR, int END_YEAR) {
		WheelTime.setSTART_YEAR(START_YEAR);
		WheelTime.setEND_YEAR(END_YEAR);
	}

	/**
	 * 设置选中时间
	 * @param date
	 */
	public void setTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date == null)
			calendar.setTimeInMillis(System.currentTimeMillis());
		else
			calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		wheelTime.setPicker(year, month, day, hours, minute);
	}

	/**
	 * 指定选中的时间，显示选择器
	 * 
	 * @param parent
	 * @param gravity
	 * @param x
	 * @param y
	 * @param date
	 */
	public void showAtLocation(View parent, int gravity, int x, int y, Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date == null)
			calendar.setTimeInMillis(System.currentTimeMillis());
		else
			calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		wheelTime.setPicker(year, month, day, hours, minute);
		update();
		super.showAtLocation(parent, gravity, x, y);
	}
	
	/**
	 * 指定选中的时间，显示选择器
	 * 
	 * @param parent
	 * @param gravity
	 * @param x
	 * @param y
	 */
	public void showAtLocation(View parent, int gravity, int x, int y) {
		update();
		super.showAtLocation(parent, gravity, x, y);
		WindowManager.LayoutParams params=((Activity)this.context).getWindow().getAttributes();
		params.alpha=0.7f;
		((Activity)this.context).getWindow().setAttributes(params);
	}

	/**
	 * 关闭窗口
	 */
	private void closePopupWindow()
	{
		WindowManager.LayoutParams params = ((Activity)this.context).getWindow().getAttributes();
		params.alpha=1f;
		((Activity)this.context).getWindow().setAttributes(params);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wheelTime.setCyclic(cyclic);
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if (tag.equals(TAG_CANCEL)) {
			dismiss();
			return;
		} else {
			if (timeSelectListener != null) {
				try {
					Date date = DateCompute.getStringToDate(wheelTime.getTime());
					timeSelectListener.onTimeSelect(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			dismiss();
			return;
		}
	}


	public interface OnTimeSelectListener {
		void onTimeSelect(Date date);
	}

	public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
		this.timeSelectListener = timeSelectListener;
	}

}
