package com.BetterLife.mymoment;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MomentListFragment extends ListFragment {
	ArrayList<Moment> mMoments;
	private static final String TAG = "MF";
	private Callbacks mCallbacks;
	
	
	//****any hosting activity of this fragment has to implement 
	//this interface
	public interface Callbacks{
		void onMomentSelected(Moment moment);
	}
	
	@Override 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		//call Fragment method getActivity() return the hosting activity
		//use Activity.setTitle() to change what displayed on the actionbar/titlebar
		getActivity().setTitle(R.string.title_moments);
		
		mMoments = MomentLab.get(getActivity()).getMoments();
		
		MomentAdapter adapter = new MomentAdapter(mMoments);
		
		//set the adapter of the implicit ListView
		setListAdapter(adapter);
		setRetainInstance(true);
	}
	
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, 
			Bundle savedInstanceState){
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			//use floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);
		} else {
			//use contextual acting bar on Honeycomb and higher
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener(){

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.moment_list_item, menu);
					return true;
				}
				@Override
				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					switch(item.getItemId()){
						case R.id.menu_item_delete_moment:
							MomentAdapter adapter = (MomentAdapter)getListAdapter();
							MomentLab momentLab = MomentLab.get(getActivity());
							for (int i = adapter.getCount() - 1; i >= 0; i--){
								if (getListView().isItemChecked(i))
									momentLab.deleteMoment(adapter.getItem(i));
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
						default: 
							return false;	
					}	
				}

			

				@Override
				public void onDestroyActionMode(ActionMode arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode arg0,
						int arg1, long arg2, boolean arg3) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
		return v;
	}
	
	
	public void onListItemClick(ListView l, View v, int position,
			long id){
		
		//call getListAdapter() method to return the the list view's adapter
		Moment m = ((MomentAdapter)getListAdapter()).getItem(position);
		Log.i(TAG, m.getTitle() +  " was clicked");
		
		mCallbacks.onMomentSelected(m);
		
	}
	
	private class MomentAdapter extends ArrayAdapter<Moment>{
		
		public MomentAdapter(ArrayList<Moment> moments){
			super(getActivity(), 0 , moments);
		}
	
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
		
			//If there is no given view, inflate one
			if(convertView == null){
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_moment, null);
			}
			
			//connect view with this moment
			Moment m = getItem(position);
			
			TextView textViewTitle = (TextView)convertView
					.findViewById(R.id.titleTextView_moment_list_item);
			textViewTitle.setText(m.getTitle());
			
			TextView textViewDate = (TextView)convertView
					.findViewById(R.id.dateTextView_moment_list_item);
			textViewDate.setText(MomentFragment.dateDetailAsInt(m.getDate()));
			
			
			TextView textViewDetail = (TextView)convertView
					.findViewById(R.id.detailTextView_moment_list_item);
			textViewDetail.setText(m.getDetail());
			
			return convertView;
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		((MomentAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_moment_list, menu);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo){
		getActivity().getMenuInflater().inflate(R.menu.moment_list_item, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		 case R.id.new_moment_menu_item:
			 Moment moment = new Moment();
			 MomentLab.get(getActivity()).addMoment(moment);
			 ((MomentAdapter)getListAdapter()).notifyDataSetChanged();
			 mCallbacks.onMomentSelected(moment);
			 return true;
		default:
			return super.onOptionsItemSelected(item);		
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)
				item.getMenuInfo();
		int position = info.position;
		MomentAdapter adapter = (MomentAdapter)getListAdapter();
		Moment moment = adapter.getItem(position);
		
		switch(item.getItemId()){
			case R.id.menu_item_delete_moment:
				MomentLab.get(getActivity()).deleteMoment(moment);
				adapter.notifyDataSetChanged();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
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
	
	public void updateUI(){
		((MomentAdapter)getListAdapter()).notifyDataSetChanged();
	}
}
