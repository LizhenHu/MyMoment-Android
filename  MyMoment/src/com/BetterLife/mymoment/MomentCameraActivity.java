package com.BetterLife.mymoment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class MomentCameraActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return new MomentCameraFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		
		
		//hide window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//hide status bar 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//onCreate has to call after above methods
		super.onCreate(savedInstanceState);
	}

/*
	@Override
	public void onCreate(Bundle savedInstanceState){
		//hide the window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//hide the status bar and other os-level chrome
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}*/
}
