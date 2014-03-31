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

package com.reliqartz.firsttipcalc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.reliqartz.firsttipcalc.interfaces.FinalBillChangeListener;
import com.reliqartz.firsttipcalc.interfaces.SplitRatioChangeListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. 
 * Use the {@link SplitterFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class SplitterFragment extends Fragment implements FinalBillChangeListener,
		SplitRatioChangeListener, OnCheckedChangeListener {
	public static final String TAG = "FirstTip/Splitter";
	
	private static final String ARG_PARAM1 = "param1";
	private static final String FINAL_BILL = "TOTAL_BILL";
	
	private double mFinalBill = 0.0;
	
	private TextView mBillTextView;
	private Spinner mSplitForSpinner;
	private RadioGroup mSplitRadioGroup;
	private RadioButton mSplitEvenRadioButton, mSplitRatioRadioButton;
	private TextView mSplitInfoTextView, mSplitOnTextView;
	private LinearLayout mSplitEvenResultLayout;
	private ListView mSplitRatioResultsListView;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SplitterFragment.
	 */
	public static SplitterFragment newInstance(String param1) {
		SplitterFragment fragment = new SplitterFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		fragment.setArguments(args);
		
		return fragment;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null){
			mFinalBill = savedInstanceState.getDouble(FINAL_BILL, mFinalBill);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_splitter, container, false);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		this.init();
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putDouble(FINAL_BILL, mFinalBill);
	}

	@Override
	public void onFinalBillChanged(double finalBill) {
		Log.v(TAG, "Final bill recieved as: " + finalBill);
		
		mFinalBill = finalBill;
		if(mBillTextView != null){
			mBillTextView.setText(MainActivity.sCurrencySymbol + String.format("%.02f", mFinalBill));
		}else{
			Log.v(TAG, "Bill view not initialized. No update.");
		}
	}

	@Override
	public void onSplitRatioChanged(double ratio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Log.v(TAG, "RadioGroup onCheckedChanged.");
	} 
	
	/**
	 * Initialize controls
	 */
	private void init() {
		Log.i(TAG, "getView(): " +getView());
		
		mBillTextView = (TextView) getView().findViewById(R.id.billTextView);
		mSplitForSpinner = (Spinner) getView().findViewById(R.id.splitForSpinner);
		mSplitRadioGroup = (RadioGroup) getView().findViewById(R.id.splitForRadioGroup);
		mSplitEvenRadioButton = (RadioButton) getView().findViewById(R.id.splitEvenRadioButton);
		mSplitRatioRadioButton = (RadioButton) getView().findViewById(R.id.splitRatioRadioButton);
		mSplitInfoTextView = (TextView) getView().findViewById(R.id.splitInfoTextView);
		mSplitOnTextView = (TextView) getView().findViewById(R.id.splitOnTextView);
		mSplitEvenResultLayout = (LinearLayout) getView().findViewById(R.id.splitEvenResultLayout);
		mSplitRatioResultsListView = (ListView) getView().findViewById(R.id.splitRatioResultsListView);
		
		mBillTextView.setText(MainActivity.sCurrencySymbol + String.format("%.02f", mFinalBill));
		mSplitRadioGroup.clearCheck();
		mSplitRadioGroup.setOnCheckedChangeListener(this);
	}
}
