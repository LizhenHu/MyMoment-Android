package com.BetterLife.mymoment;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class MomentLab {
	private static final String FILENAME = "moment.json";
	private static final String TAG = "MomentLab";
	
	private static MomentLab sMomentLab;
	private Context mAppContext;
	private ArrayList<Moment> mMoments;
	private MomentIntentJSON mSerializer;
	
	private MomentLab(Context appContext){
		mAppContext = appContext;
		try{
			mMoments = mSerializer.loadMoments(); 
		} catch (Exception e){
			mMoments = new ArrayList<Moment>();
			Log.e(TAG, "Error loading crimes: ", e);
		}
		mSerializer = new MomentIntentJSON(mAppContext,FILENAME);
	}
	
	public static MomentLab get(Context c){
		if(sMomentLab == null)
			sMomentLab = new MomentLab(c.getApplicationContext());
		return sMomentLab;
	}
	
	public ArrayList<Moment> getMoments(){
		return mMoments;
	}
	
	public Moment getMoment(UUID id){
		for(Moment m: mMoments){
			if (m.getId().equals(id))
				return m;
		}
		return null;
		
	}
	
	public void addMoment(Moment m){
		mMoments.add(m);
	}
	
	public void deleteMoment(Moment m){
		mMoments.remove(m);
	}
	public boolean saveMoments(){
		try{
			mSerializer.saveMoments(mMoments);
			Log.i(TAG, "moments saved ");
			return true;
		} catch (Exception e){
			Log.e(TAG, "Error saving moments: " + e);
			return false;
		}
	}
}
