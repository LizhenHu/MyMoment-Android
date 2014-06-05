package com.BetterLife.mymoment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BigImageFragment extends DialogFragment {
	public static final String EXTRA_IMAGE_PATH =
			"com.BetterLife.mymoment.imager_path";
	
	private ImageView mImageView;
	
	public View onCreateView(LayoutInflater inflater,
			ViewGroup parent, Bundle saveInstanceState){
		mImageView = new ImageView(getActivity());
		String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
		BitmapDrawable image = PictureScaleAndClean.getScaledDrawable(getActivity(), path);
		
		mImageView.setImageDrawable(image);
		return mImageView;
	}
	
	public void onDestroyView(){
		super.onDestroyView();
		PictureScaleAndClean.cleanImageView(mImageView);
	}
	
	public static BigImageFragment newInstance(String path){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_IMAGE_PATH, path);
		
		BigImageFragment fragment = new BigImageFragment();
		fragment.setArguments(args);
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		
		return fragment;
	}
}
