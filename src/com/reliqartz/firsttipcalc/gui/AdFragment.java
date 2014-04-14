/**
 * Copyright 2014 ReliQ Artz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reliqartz.firsttipcalc.gui;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.reliqartz.firsttipcalc.FirstTipApplication;

/**
 * An abstract fragment with one default ad unit.
 * @author Patrick Reid
 */
public abstract class AdFragment extends Fragment {
	private static final String TAG = "FirstTip/AdFragment";
	private static final String PRIMARY_AD_UNIT_ID = "";
	protected AdView mAdView;  
	/* (non-Javadoc) 
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		mAdView = new AdView(getActivity());
		mAdView.setAdSize(AdSize.BANNER);
		mAdView.setAdUnitId(PRIMARY_AD_UNIT_ID);
		mAdView.setVisibility(View.GONE);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
	      mAdView.resume();
	    }
		requestAd();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		if (mAdView != null) {
	      mAdView.pause();
	    }
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}
	
	/**
	 * Initialize a banner ad inside a view.
	 * @param viewId The id of the view in which the ad should be placed.
	 */
	protected void initAd(int viewId) {
		((LinearLayout) getView().findViewById(viewId))
				.addView(mAdView);
		mAdView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				mAdView.setVisibility(View.VISIBLE);
			}
			@Override
			public void onAdFailedToLoad(int errorCode) {
				mAdView.setVisibility(View.GONE);
				Log.w(TAG, "Ad failed to load: " + errorCode);
			}
		});
	}
	
	/**
	 * Request an ad.
	 */
	protected void requestAd() {
		if(FirstTipApplication.sShowAds) {
			mAdView.loadAd(new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.build());
		} else {
			Log.v(TAG, "Ads are off.");
		}
	}
}
