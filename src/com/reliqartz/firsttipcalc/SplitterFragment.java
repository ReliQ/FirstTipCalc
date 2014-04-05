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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reliqartz.firsttipcalc.interfaces.FinalBillChangeListener;
import com.reliqartz.firsttipcalc.interfaces.SplitRatioChangeListener;

/**
 * Bill splitter. 
 * @author Patrick Reid
 */
public class SplitterFragment extends Fragment implements FinalBillChangeListener,
		SplitRatioChangeListener, OnCheckedChangeListener, OnItemSelectedListener {
	public static final String TAG = "FirstTip/Splitter";
	
	private static final String ARG_PARAM1 = "param1";
	private static final String FINAL_BILL = "TOTAL_BILL";
	
	private double mFinalBill = 0.0;
	private boolean mSplitEven = true;
	private int mSplitFor = 2;
	private String mSplitInfo = new String();
	private String mSplitRatio = new String();
	
	private TextView mBillTextView;
	private Spinner mSplitForSpinner;
	private RadioGroup mSplitRadioGroup;
	private RadioButton mSplitEvenRadioButton, mSplitRatioRadioButton;
	private TextView mSplitInfoTextView, mSplitRatioTextView, mSplitEvenResultTextView;
	private LinearLayout mSplitEvenResultLayout;
	private ListView mSplitRatioResultsListView;

	/**
	 * Factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
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
		
		mSplitInfo = getString(R.string.split_info_text_view);
		if (savedInstanceState != null) {
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
		init();
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putDouble(FINAL_BILL, mFinalBill);
	}

	/* (non-Javadoc)
	 * @see com.reliqartz.firsttipcalc.interfaces.FinalBillChangeListener#onFinalBillChanged(double)
	 */
	@Override
	public void onFinalBillChanged(double finalBill) {
		Log.v(TAG, "Final bill recieved as: " + finalBill);
		mFinalBill = finalBill;
		if (mBillTextView != null) {
			mBillTextView.setText(MainActivity.sCurrencySymbol + String.format("%.02f", mFinalBill));
			if (mSplitEvenRadioButton.isChecked()) {
				doSplit();
			}
		} else {
			Log.v(TAG, "Bill view not initialized. No update.");
		}
	}

	/* (non-Javadoc)
	 * @see com.reliqartz.firsttipcalc.interfaces.SplitRatioChangeListener#onSplitRatioChanged(double)
	 */
	@Override
	public void onSplitRatioChanged(String ratio) {
		Log.i(TAG, "Split ratio recieved: " + ratio);
		
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == mSplitRatioRadioButton.getId()) {
			mSplitEven = false;
		} else {
			mSplitEven = true;
		}
		Log.v(TAG, "Split changed: " + checkedId + " :checked. Split even: " + mSplitEven);
		doSplit();
	} 

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mSplitFor = Integer.parseInt(mSplitForSpinner.getSelectedItem().toString().replaceAll("[\\D]",""));
		Log.v(TAG, "Splitting for: " + mSplitFor);
		if (mSplitEvenRadioButton.isChecked() || mSplitEvenRadioButton.isChecked()) {
			doSplit();
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing...
	} 
	
	/**
	 * Initialize views and set listeners.
	 */
	private void init() {
		mBillTextView = (TextView) getView().findViewById(R.id.billTextView);
		mSplitForSpinner = (Spinner) getView().findViewById(R.id.splitForSpinner);
		mSplitRadioGroup = (RadioGroup) getView().findViewById(R.id.splitForRadioGroup);
		mSplitEvenRadioButton = (RadioButton) getView().findViewById(R.id.splitEvenRadioButton);
		mSplitRatioRadioButton = (RadioButton) getView().findViewById(R.id.splitRatioRadioButton);
		mSplitInfoTextView = (TextView) getView().findViewById(R.id.splitInfoTextView);
		mSplitRatioTextView = (TextView) getView().findViewById(R.id.splitRatioTextView);
		mSplitEvenResultTextView = (TextView) getView().findViewById(R.id.evenSplitAmountTextView);
		mSplitEvenResultLayout = (LinearLayout) getView().findViewById(R.id.splitEvenResultLayout);
		mSplitRatioResultsListView = (ListView) getView().findViewById(R.id.splitRatioResultsListView);
		
		mBillTextView.setText(MainActivity.sCurrencySymbol + String.format("%.02f", mFinalBill));
		mSplitRadioGroup.clearCheck();
		mSplitRadioGroup.setOnCheckedChangeListener(this);
		mSplitForSpinner.setOnItemSelectedListener(this);
		
		updateSplitInfoRow();
	}
	
	/**
	 * Perform bill split.
	 */
	private void doSplit() {
		Log.v(TAG, "Attempting to do split...");
		if (mSplitEven) {
			Log.v(TAG, "Splitting even.:.");
			mSplitEvenResultLayout.setVisibility(View.VISIBLE);
			mSplitRatioResultsListView.setVisibility(View.GONE);
			mSplitInfo = getString(R.string.split_on_text_view);
			mSplitRatio = getString(R.string.split_ratio_text_view);
			mSplitEvenResultTextView.setText(MainActivity.sCurrencySymbol 
					+ String.format("%.02f", mFinalBill/mSplitFor));
		} else {
			if (mFinalBill == 0.0) {
				mSplitRadioGroup.clearCheck();
				Toast.makeText(getActivity(),
						getString(R.string.ratio_no_bill_warning),
						Toast.LENGTH_SHORT).show();
			} else {
				Log.v(TAG, "Splitting by ratio.:..");
				mSplitRatioResultsListView.setVisibility(View.VISIBLE);
				mSplitEvenResultLayout.setVisibility(View.GONE);
				RatioDialog.newInstance(mFinalBill, mSplitFor)
					.show(getFragmentManager(), RatioDialog.TAG);

				//Toast.makeText(getActivity(), "Uneven split selected.", Toast.LENGTH_SHORT).show();
			}
		}
		updateSplitInfoRow();
	}
	
	/**
	 * Change split information texts.
	 */
	private void updateSplitInfoRow() {
		mSplitInfoTextView.setText(mSplitInfo);
		mSplitRatioTextView.setText(mSplitRatio);
	}
}
