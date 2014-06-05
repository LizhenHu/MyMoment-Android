package com.BetterLife.mymoment;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class MomentViewPagerActivity extends FragmentActivity 
		implements MomentFragment.Callbacks{
	
	//ViewPager is a fragment container, so it has to have an ID for fragment manager
	private ViewPager mViewPager;
	
	private ArrayList<Moment> mMoments;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//ViewPager class is only available in the support library
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mMoments = MomentLab.get(this).getMoments();
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(fm){
			@Override
			public int getCount(){
				return mMoments.size();
			}
			
			@Override
			public Fragment getItem(int pos){
				Moment m = mMoments.get(pos);
				return MomentFragment.newInstance(m.getId());
			}
		};
		
		mViewPager.setAdapter(adapter);
		
		//get the moment if from the intent extra data
		UUID mId = (UUID)getIntent()
				.getSerializableExtra(MomentFragment.EXTRA_MOMENT_ID);
		// find the current index
		for(int i = 0; i < mMoments.size(); i++){
			if(mMoments.get(i).getId().equals(mId)){
				mViewPager.setCurrentItem(i);
				break;
			}
				
		}
		
		//call this listener to change the action bar / title bar 
		//as the title of the moment
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
			 @Override
		     public void onPageSelected(int position) {
				 Moment moment = mMoments.get(position);
				 if(moment.getTitle() != null)
					 setTitle(moment.getTitle());
			 }
			 
			 @Override
		     public void onPageScrollStateChanged(int state) {
		     }
			 
			 @Override
		     public void onPageScrolled(int position, 
		    		 float positionOffset, int positionOffsetPixels) {
		        }
		});
		
	}
	
	public void onMomentUpdated(Moment moment){
		
	}
}
