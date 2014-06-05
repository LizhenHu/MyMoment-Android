package com.BetterLife.mymoment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MomentCameraFragment extends Fragment {
	private static final String TAG = "MomentCameraFragment";
	
	public static final String EXTRA_PHOTONAME =
			"com.BetterLife.mymoment.photo_pictureName";
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private Button mTakePictureButton;
	private View mProgressContainer;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_moment_camera, parent, false);
		
		mTakePictureButton = (Button)v.findViewById(
				R.id.moment_camera_takePictureButton);
		mTakePictureButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if (mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, mPictureCallback);
				}
			}
		});
		mSurfaceView = (SurfaceView)v.findViewById(
				R.id.moment_camera_surfaceView);
		//get the SurfaceHolder providing access and control over this 
		//SurfaceView's underlying surface.
		SurfaceHolder holder = mSurfaceView.getHolder();
		//the following deprecated method is for Camera preview to work on pre-3.0 devices
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		holder.addCallback(new SurfaceHolder.Callback(){
			public void surfaceCreated(SurfaceHolder holder){
				try {
					if (mCamera != null)
						mCamera.setPreviewDisplay(holder);
				} catch (IOException exception) {
					Log.e (TAG, "Error setting up preview display",
							exception);
				}
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format,
					int w, int h){
				if(mCamera == null) return;
				
				Camera.Parameters parameters = mCamera.getParameters();
				Size s = getLargestSupportSize(
						parameters.getSupportedPreviewSizes(), w,h);
				parameters.setPreviewSize(s.width, s.height);
				s = getLargestSupportSize(parameters.getSupportedPictureSizes(), w,h);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try{
					mCamera.startPreview();
				} catch (Exception e){
					Log.e(TAG, "Failed to start preview",e);
					mCamera.release();
					mCamera = null;
				}			
			}
			
			public void surfaceDestroyed(SurfaceHolder holder){
				if (mCamera != null)
					mCamera.stopPreview();
			}
		});
		
		mProgressContainer = v.findViewById(R.id.camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
		return v;
	}
	
	
	public Size getLargestSupportSize(List<Size> sizes, int w, int h){
		Size largestSize = sizes.get(0);
		int largestArea = largestSize.width * largestSize.height;
		for(Size s: sizes){
			int area = s.width * s.height;
			if(largestArea < area){
				largestArea = area;
				largestSize = s;
			}
		}
		return largestSize;	
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onResume(){
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
			//create a new camera object
			mCamera = Camera.open(0);
		} else {
			mCamera = Camera.open();
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if (mCamera != null){
			mCamera.release();
			mCamera = null;
		}
	}
	
	// signal the moment of actual image capture
	private Camera.ShutterCallback mShutterCallback = 
			new Camera.ShutterCallback(){
		public void onShutter(){
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	
	private Camera.PictureCallback mPictureCallback = 
			new Camera.PictureCallback(){
		public void onPictureTaken(byte[] data, Camera camera){
			String pictureName = UUID.randomUUID().toString() + ".jpg";
			FileOutputStream output = null;
			boolean success = true;
			try{
				output = getActivity().openFileOutput(pictureName, Context.MODE_PRIVATE);
				output.write(data);
			} catch (Exception e ) {
				Log.e(TAG, "Fail to save the picture " + pictureName, e);
			} finally {
				try {
					if (output != null)
						output.close();
				} catch (Exception e) {
					Log.e (TAG, "Fail to close file " + pictureName, e);
					success = false;
				}
			}
			
			if (success){
				Intent intent = new Intent();
				intent.putExtra(EXTRA_PHOTONAME, pictureName);
				getActivity().setResult(Activity.RESULT_OK, intent);
			} else {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
		}
	};
	
}
