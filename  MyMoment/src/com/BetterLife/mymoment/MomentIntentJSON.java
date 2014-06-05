package com.BetterLife.mymoment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class MomentIntentJSON {
	private Context mContext;
	private String mFilename;
	
	public MomentIntentJSON(Context context, String filename){
		mContext = context;
		mFilename = filename;
	}
	
	public void saveMoments(ArrayList<Moment> mMoments)
		throws JSONException, IOException{
		JSONArray array = new JSONArray();
		for(Moment m: mMoments){
			//call public JSONArray put (Object value)
			array.put(m.toJSON());
		}
		Writer writer = null;
		
		try{
			//Open a private file associated with this Context's application
			//package for writing. Creates the file if it doesn't already exist.
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			
			//using out as the target stream to write converted characters to.
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public ArrayList<Moment> loadMoments() 
			throws IOException, JSONException{
		ArrayList<Moment> moments = new ArrayList<Moment>();
		//Wraps an existing Reader and buffers the input
		BufferedReader reader = null;
		try{
			//Open a private file associated with this Context's application package for reading.
			InputStream input = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(input));
			StringBuilder jsonString = new StringBuilder();
			while(reader.readLine() != null){
				jsonString.append(reader.readLine());
			}
			
			//parse the JSON
			JSONArray array = (JSONArray) new JSONTokener(
					jsonString.toString()).nextValue();
			
			//build the arrays of moments from JSONObjects
			for(int i = 0; i <array.length(); i++){
				moments.add(new Moment(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
			
		} finally {
			if (reader != null)
				reader.close();
		}
		return moments;
	}
}
