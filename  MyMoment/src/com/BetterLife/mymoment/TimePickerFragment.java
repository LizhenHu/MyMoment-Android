package com.BetterLife.mymoment;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME = 
			"com.BetterLife.mymoment.time";
	private Date mDate;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		mDate = (Date)getArguments().getSerializable(EXTRA_TIME);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int mins = calendar.get(Calendar.MINUTE);
		
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_time, null);
		
		TimePicker timepicker = (TimePicker)v.findViewById(R.id.dialog_timePicker);
		timepicker.setCurrentHour(hours);
		timepicker.setCurrentMinute(mins);
		timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
			@Override
			public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(mDate);
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				
				mDate = calendar.getTime();
				getArguments().putSerializable(EXTRA_TIME, mDate);
			}

			
		});
		
		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.time_picker_title)		
				.setPositiveButton(android.R.string.ok, 
						new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){
							sendResult(Activity.RESULT_OK);
						}
				})
				.create();
	}
	
	public static TimePickerFragment newInstance(Date mDate){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, mDate);
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private void sendResult(int resultCode){
		if (getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_TIME,mDate);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
}
