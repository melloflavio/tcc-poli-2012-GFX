package com.poli.gfx.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class MaxTodayDatePickerDialog  extends DatePickerDialog{

	private int maxMonth;
	private int maxDay;
	private int maxYear;
	
	private Calendar mCalendar;
	private SimpleDateFormat mFormatter;
	
	public MaxTodayDatePickerDialog(Context context,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		
		
		
		mCalendar = Calendar.getInstance();
		maxYear = mCalendar.get(Calendar.YEAR);
		maxMonth = mCalendar.get(Calendar.MONTH);
		maxDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		
		mFormatter = new SimpleDateFormat("MMMM d, yyyy");
		
		mCalendar.set(year, monthOfYear, dayOfMonth);
		
		setTitle(mFormatter.format(mCalendar.getTime()));
	}
	
	public MaxTodayDatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);

		mCalendar = Calendar.getInstance();
		maxYear = mCalendar.get(Calendar.YEAR);
		maxMonth = mCalendar.get(Calendar.MONTH);
		maxDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		
		mFormatter = new SimpleDateFormat("MMMM d, yyyy");
		
		mCalendar.set(year, monthOfYear, dayOfMonth);
		
		setTitle(mFormatter.format(mCalendar.getTime()));
	}
	
	@Override
	public void onDateChanged(DatePicker view, int year,int month, int day){
		boolean afterMaxDate = false;
		
		if(year > maxYear){
			afterMaxDate = true;
		}
		else if(year == maxYear){
			if (month > maxMonth){
				afterMaxDate = true;
			}
			else if(month == maxMonth){
				if (day > maxDay){
					afterMaxDate = true;
				}
			}
			
		}
		
		
		if (afterMaxDate){
			year = maxYear;
			month = maxMonth;
			day = maxDay;
			
			view.updateDate(year,  month,  day);
		}
		
		//display in view title
		mCalendar.set(year, month, day);
	    setTitle(mFormatter.format(mCalendar.getTime()));
		
	}


}

