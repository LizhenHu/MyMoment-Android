package com.BetterLife.mymoment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MomentListActivity extends SingleFragmentActivity
	implements MomentListFragment.Callbacks,
	MomentFragment.Callbacks{

	@Override
	protected Fragment createFragment() {
		return new MomentListFragment();
	}
	
	@Override 
	protected int getLayoutResId(){
		return R.layout.activity_masterdetail;
	}
	
	@Override
	public void onMomentSelected(Moment moment){
		if (findViewById(R.id.detailFragmentContainer) == null){
			//start an instance of MomentViewPagerActivity
			Intent intent = new Intent(this, MomentViewPagerActivity.class);
			intent.putExtra(MomentFragment.EXTRA_MOMENT_ID, moment.getId());
			startActivity(intent);			
		} else {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			Fragment oldFragment = fm.findFragmentById(R.id.detailFragmentContainer);
			if(oldFragment != null){
				ft.remove(oldFragment);
			}
			
			Fragment newFragment = MomentFragment.newInstance(moment.getId());
			ft.add(R.id.detailFragmentContainer, newFragment).commit();
		}
	}
	
	@Override
	public void onMomentUpdated(Moment moment){
		FragmentManager fm = getSupportFragmentManager();
		MomentListFragment fragment = (MomentListFragment)
				fm.findFragmentById(R.id.fragmentContainer);
		fragment.updateUI();
	}
}
