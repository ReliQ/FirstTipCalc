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

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.reliqartz.firsttipcalc.R;
import com.reliqartz.firsttipcalc.interfaces.FinalBillChangeListener;
import com.reliqartz.firsttipcalc.interfaces.SplitRatioChangeListener;
import com.reliqartz.firsttipcalc.utils.FontApplicator;
import com.reliqartz.firsttipcalc.utils.FontLibrary;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, 
		OnSharedPreferenceChangeListener, FinalBillChangeListener, SplitRatioChangeListener,
		FontApplicator.Fonty {
	private static final String TAG = "FirstTip/Main";
	
	private static final String KEYBOARD_PREF_KEY = "pref_keyboard";
	private static final String CURRENCY_PREF_KEY = "pref_currency";
	private static final String BASE_TIP_PREF_KEY = "pref_base_tip";
	private static final String SPLIT_INCLUSIVE_PREF_KEY = "pref_split_inclusive";
	
	// Preferences
	public static boolean sStartWithKeyboard = false;
	public static boolean sSplitInclusive = true;
	public static int sBaseTip = 5;
	public static String sCurrencySymbol = "$";
	
	private CalcFragment mCalculator;
	private SplitterFragment mSplitter;
	
	// Font
	private FontLibrary mFonts;
	private FontApplicator mFontApplicator;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loadPreferences();
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// initialize fragments
		if (savedInstanceState == null) {
			mCalculator = new CalcFragment();
			mSplitter = new SplitterFragment();
		} else {
			mCalculator = (CalcFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, CalcFragment.TAG);
			mSplitter = (SplitterFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, SplitterFragment.TAG);
		}

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the application.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		// setup fonts
		applyFonts();
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		getSupportFragmentManager().putFragment(outState, CalcFragment.TAG, mCalculator);
		getSupportFragmentManager().putFragment(outState, SplitterFragment.TAG, mSplitter);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Intent i;
		Log.d(TAG, "Menu item selected: "+ item.getItemId());
		int itemId = item.getItemId();
		if (itemId == R.id.action_refresh) {
			i = getIntent();
			finish();
			//i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(i);
			overridePendingTransition (0,0);
			return true;
		} else if (itemId == R.id.action_settings) {
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	/* (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		loadPreferences();
	}
	
	/* (non-Javadoc)
	 * @see com.reliqartz.firsttipcalc.interfaces.FinalBillChangeListener#onFinalBillChanged(double)
	 */
	@Override
	public void onFinalBillChanged(double finalBill) {
		mSplitter.onFinalBillChanged(finalBill);
	}
	
	/* (non-Javadoc)
	 * @see com.reliqartz.firsttipcalc.interfaces.SplitRatioChangeListener#onSplitRatioChanged(java.lang.String)
	 */
	@Override
	public void onSplitRatioChanged(String ratio) {
		mSplitter.onSplitRatioChanged(ratio);		
	}
	
	/* (non-Javadoc)
	 * @see com.reliqartz.firsttipcalc.utils.FontApplicator.Fonty#applyFonts()
	 */
	@Override
	public void applyFonts() {
		// Setup Fonts
		mFonts = new FontLibrary(this);
		// Apply two fonts to ActionBar, one for title & one for other TextViews
		mFontApplicator = new FontApplicator(getApplicationContext(),
				FontLibrary.ROBOTO_LIGHT).applyFont(getWindow().getDecorView());
		final int actionBarTitleId = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
		((TextView) getWindow().findViewById(actionBarTitleId))
				.setTypeface(mFonts.getFont(FontLibrary.ROBOTO));
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			// Get fragment to show
			switch (position) {
				case 0:
					return mCalculator;
				default:
					return mSplitter;
			}
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			}
			return null;
		}
	}
	
	/**
	 * @return the FontApplicator.
	 */
	FontApplicator getFontApplicator() {
		return mFontApplicator;
	}
	
	/**
	 * Load user preferences;
	 */
	private void loadPreferences() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sStartWithKeyboard = settings.getBoolean(KEYBOARD_PREF_KEY, sStartWithKeyboard);
		sSplitInclusive = settings.getBoolean(SPLIT_INCLUSIVE_PREF_KEY, sSplitInclusive);
		sCurrencySymbol = settings.getString(CURRENCY_PREF_KEY, sCurrencySymbol);
		sBaseTip = Integer.parseInt(settings.getString(BASE_TIP_PREF_KEY, sBaseTip + "").replaceAll("[\\D]",""));
		settings.registerOnSharedPreferenceChangeListener(this);
	}

}
