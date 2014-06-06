package com.BetterLife.mymoment;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MomentFragment extends Fragment {
	private static final String TAG = "MomentFragment";
	private Moment mMoment;
	private EditText mTextTitle;
	private Button mButtonDate;
	private Button mButtonTime;
	private EditText mTextDetail;
	private ImageView mImageViewPhoto;
	private Callbacks mCallbacks;
	
	public static final String EXTRA_MOMENT_ID = 
			"com.BetterLife.mymoment.moment_id";
	private static final String DIALOG_DATE = "date";
	private static final String DIALOG_TIME = "time";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_PHOTO = 1;
	private static final int REQUEST_TIME = 2;
	private static final String DIALOG_IMAGE = "image";
	
	//*** any hosting activity of this fragment has to implements this interface
	public interface Callbacks{
		void onMomentUpdated(Moment moment);
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		UUID mId = (UUID)getArguments().getSerializable(EXTRA_MOMENT_ID);
		mMoment = MomentLab.get(getActivity()).getMoment(mId);
		
		setRetainInstance(true);
		
	}
	

	
	@TargetApi(11)
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_moment, container, false);
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			//if (NavUtils.getParentActivityName(getActivity()) !=  null){
				android.app.ActionBar actionBar = getActivity().getActionBar();
				actionBar.setDisplayHomeAsUpEnabled(true);
			//}
		}
		
		mTextTitle = (EditText)v.findViewById(R.id.title_moment);
		mTextTitle.setText(mMoment.getTitle());
		mTextTitle.addTextChangedListener(new TextWatcher(){
			
			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2,
					int arg3) {
				mMoment.setTitle(c.toString());
				mCallbacks.onMomentUpdated(mMoment);
				getActivity().setTitle(mMoment.getTitle());
			}

			@Override
			public void afterTextChanged(Editable arg0) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

		});

		
		mTextDetail = (EditText)v.findViewById(R.id.detail_moment);
		mTextDetail.setText(mMoment.getDetail());
		mTextDetail.addTextChangedListener(new TextWatcher(){
			
			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2,
					int arg3) {
				mMoment.setDetail(c.toString());
				mCallbacks.onMomentUpdated(mMoment);
			}

			@Override
			public void afterTextChanged(Editable arg0) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

		});
		
		mButtonDate = (Button)v.findViewById(R.id.button_date);
		updateDate(mMoment.getDate());
		mButtonDate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialogDate = DatePickerFragment.
						newInstance(mMoment.getDate());
				dialogDate.setTargetFragment(MomentFragment.this,
						REQUEST_DATE);
				dialogDate.show(fm, DIALOG_DATE);
			}
		});
		
		mButtonTime = (Button)v.findViewById(R.id.button_time);
		updateTimeDisplay(mMoment.getDate());
		mButtonTime.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				FragmentManager fm = getActivity().getSupportFragmentManager();
				TimePickerFragment dialogTime =TimePickerFragment.newInstance(mMoment.getDate());
				dialogTime.setTargetFragment(MomentFragment.this, REQUEST_TIME);
				dialogTime.show(fm, DIALOG_TIME);
			}
		});
		
		
		mImageViewPhoto = (ImageView)v.findViewById(R.id.imageView_moment);
		mImageViewPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Photo picture = mMoment.getPhoto();
				if (picture == null) return;
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity()
						.getFileStreamPath(picture.getPictureName())
						.getAbsolutePath();
				BigImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
		});
		
		return v;
	}
	
	@TargetApi(9)
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem item = menu.findItem(R.id.menu_item_camera);
		//check if the camera is available, if not then disable mButtonPhoto
		PackageManager pm = getActivity().getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
						pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
						(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD &&
						Camera.getNumberOfCameras() > 0);
		if (!hasCamera){
			item.setEnabled(false);
			item.getIcon().setAlpha(130);
			
		}
		else {
			item.setEnabled(true);
			item.getIcon().setAlpha(255);	
		}
		
	}
	private void updateDate(Date mDate){	
		String date = dateDetailAsInt(mDate);
		mButtonDate.setText(date);
	}
	
	public static String dateDetailAsInt(Date mDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		 
		String[] dayOfWeekStr = new String[]{"Sun","Mon", "Tue", "Wed" ,
			"Thur","Fri","Sat"};
		String date =dayOfWeekStr[dayOfWeek] + " "+ month + "-" + day +" " + year;
		return date;
	}
	
	public static MomentFragment newInstance(UUID mId){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_MOMENT_ID, mId);
		
		MomentFragment fragment = new MomentFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent i){
		if(resultCode != Activity.RESULT_OK) return;
		
		switch(requestCode){
			case REQUEST_DATE:
				Date date = (Date)i.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
				mMoment.setDate(date);
				mCallbacks.onMomentUpdated(mMoment);
				updateDate(mMoment.getDate());
				return;
			case REQUEST_TIME:
				Date time = (Date)i.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
				mMoment.setDate(time);
				mCallbacks.onMomentUpdated(mMoment);
				updateTimeDisplay(mMoment.getDate());
			case REQUEST_PHOTO:
				String pictureName = i.getStringExtra(MomentCameraFragment
						.EXTRA_PHOTONAME);
				if (pictureName != null){
					Photo photo = new Photo(pictureName);
					mMoment.setPhoto(photo);
					mCallbacks.onMomentUpdated(mMoment);
					showPhoto();
				}
				return;
		}
	}
	private void updateTimeDisplay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int mins = c.get(Calendar.MINUTE);
		mButtonTime.setText(new StringBuilder().append(pad(hours))
				.append(":").append(pad(mins)));
	}
	
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) !=  null){
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		case R.id.menu_item_share_moment:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, getMomentShareContent());
			intent.putExtra(Intent.EXTRA_SUBJECT, 
					getString(R.string.moment_share_subject));
			intent = Intent.createChooser(intent,
					getString(R.string.moment_share));
			startActivity(intent);
			return true;
		case R.id.menu_item_camera:
			Intent i = new Intent(getActivity(), MomentCameraActivity.class);
			startActivityForResult(i, REQUEST_PHOTO);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void onPause(){
		super.onPause();
		MomentLab.get(getActivity()).saveMoments();
	}
	
	//has the photo ready once this fragment becomes visible
	public void onStart(){
		super.onStart();
		showPhoto();
	}
	
	private void showPhoto(){
		Photo picture = mMoment.getPhoto();
		BitmapDrawable bitmap = null;
		if (picture != null){
			String path = getActivity()
					.getFileStreamPath(picture.getPictureName())
					.getAbsolutePath();
			bitmap = PictureScaleAndClean.getScaledDrawable(getActivity(), path);
		}
		mImageViewPhoto.setImageDrawable(bitmap);
		
	}
	
	public void onStop(){
		super.onStop();
		PictureScaleAndClean.cleanImageView(mImageViewPhoto);
	}
	
	private String getMomentShareContent(){
		String content = null;
		String dateStr = dateDetailAsInt(mMoment.getDate());
		
		
		String title = "";
		if (mMoment.getTitle() != null)
			title =  mMoment.getTitle();
		String detail = "";
		if (mMoment.getDetail() != null)
			detail = mMoment.getDetail();
		content = title + "\n" + dateStr+ "\n" +detail;
		return content;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_moment_detail, menu);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		mCallbacks = null;
	}
}
