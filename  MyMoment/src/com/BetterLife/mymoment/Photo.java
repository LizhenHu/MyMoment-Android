package com.BetterLife.mymoment;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
	private static final String JSON_PICTURENAME = "pictureName";
	
	private String mPictureName;
	public Photo(String pictureName){
		mPictureName = pictureName;
	}
	
	public Photo(JSONObject json) throws JSONException{
		mPictureName = json.getString(JSON_PICTURENAME);
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_PICTURENAME, mPictureName);
		return json;
	}

	public String getPictureName() {
		return mPictureName;
	}
	
	
}
