package com.BetterLife.mymoment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

public class PictureScaleAndClean {
	
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity a, String path){
		
		//obtain the two dimensions of window display 
		Display display = a.getWindowManager().getDefaultDisplay();
		float displayWidth = display.getWidth();
		float displayHeight = display.getHeight();
		
		//obtain the two dimensions of the original picture
		BitmapFactory.Options options = new BitmapFactory.Options();
		//set inJustDecodeBounds to true, the decoder will return null (no bitmap)
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		//calculate the picture's proper size for the app
		int sampleSize = 1;
		if (srcHeight > displayHeight || srcWidth > displayWidth){
			if (srcHeight > srcWidth)
				sampleSize = Math.round(srcWidth / displayWidth);
			else
				sampleSize = Math.round(srcHeight / displayHeight);
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = sampleSize;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return new BitmapDrawable(a.getResources(), bitmap);
	}
	
	public static void cleanImageView(ImageView imageView){
		if (!(imageView.getDrawable() instanceof BitmapDrawable))
			return;
		//clean up the view's image for the sake of memory
		BitmapDrawable bitmap = (BitmapDrawable)imageView.getDrawable();
		bitmap.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
	
	
}
