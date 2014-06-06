package com.BetterLife.mymoment;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

public class Moment {
	private static final String JSON_TITLE = "title";
	private static final String JSON_ID = "id";
	private static final String JSON_DATE = "date";
	private static final String JSON_DETAIL ="detail";
	private static final String JSON_PHOTO = "photo";
	
	private String mTitle;
	private UUID mId;
	private Date mDate;
	private String mDetail;
	private Photo mPhoto;


	
	public Moment(){
		mId = UUID.randomUUID();
		mDate = new Date();

	}
	
	//create a constructor that accepts a JSONObject
	public Moment(JSONObject json) throws JSONException{
		mId = UUID.fromString(json.getString(JSON_ID));
		mDate = new Date(json.getLong(JSON_DATE));
		if(json.has(JSON_TITLE)){
			mTitle = json.getString(JSON_TITLE);
		}
		if(json.has(JSON_DETAIL))
			mDetail = json.getString(JSON_DETAIL);
		if(json.has(JSON_PHOTO))
			mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_TITLE, mTitle.toString());
		json.put(JSON_ID, mId);
		json.put(JSON_DATE, mDate.toString());
		json.put(JSON_DETAIL, mDetail);
		if (mPhoto != null)
			json.put(JSON_PHOTO, mPhoto.toJSON());
		return json;
	}
	public String toString(){
		return mTitle;
	}

	public Photo getPhoto() {
		return mPhoto;
	}

	public void setPhoto(Photo photo) {
		mPhoto = photo;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}

	public void setId(UUID id) {
		mId = id;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public String getDetail() {
		return mDetail;
	}

	public void setDetail(String detail) {
		mDetail = detail;
	}


	
	
}
