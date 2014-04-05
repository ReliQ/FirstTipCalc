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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.reliqartz.firsttipcalc.interfaces.FinalBillChangeListener;

/**
 * Tip calculator.
 * @author Patrick Reid
 */
public class CalcFragment extends Fragment {
	public static final String TAG = "FirstTip/Calc";

	private static final int PROGRESS_MAX = 100;
	private static final String TOTAL_BILL = "TOTAL_BILL";
	private static final String CURRENT_TIP = "CURRENT_TIP";
	private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";
	private static final String SEEKBAR_TIP = "SEEKBAR_TIP";
	
	private double mBillBeforeTip;
	private double mTipAmount;
	private double mFinalTipAmount = 0.0;
	private double mFinalBill = 0.0;
	private int mSeekBarTip = 0;
	
	private int[] mChecklistValues = new int[15];
	private String mCurrency;
	
	private EditText mBillBeforeTipET;
	private EditText mTipAmountET;
	private EditText mFinalTipAmountET;
	private EditText mFinalTipValueET;
	private EditText mFinalBillET;
	private SeekBar mTipSeekBar;
	
	private CheckBox mFriendlyCheckBox;
	private CheckBox mSpecialsCheckBox;
	private CheckBox mOpinionCheckBox;
	private CheckBox mCourtesyCheckBox;
	private CheckBox mFoodCheckBox;
	private CheckBox mDrinksCheckBox;
	private CheckBox mAttentiveCheckBox;
	private CheckBox mJudgementCheckBox;
	private CheckBox mGroomedCheckBox;
	
	private RadioGroup mAvailableRadioGroup;
	private RadioButton mAvailableBadRadio;
	private RadioButton mAvailableOkRadio;
	private RadioButton mAvailableGoodRadio;
	
	private TextView mCurrencyTextView;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mBillBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
			mTipAmount = savedInstanceState.getDouble(CURRENT_TIP);
			mFinalBill = savedInstanceState.getDouble(TOTAL_BILL);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (savedInstanceState != null) {
			mSeekBarTip = savedInstanceState.getInt(SEEKBAR_TIP, mSeekBarTip);
		} else {
			mSeekBarTip = MainActivity.sBaseTip;
		}
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_calc, container, false);
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
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	public void onResume() {
		super.onResume();
		if (!MainActivity.sStartWithKeyboard)
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putDouble(BILL_WITHOUT_TIP, mBillBeforeTip);
		outState.putDouble(CURRENT_TIP, mTipAmount);
		outState.putDouble(TOTAL_BILL, mFinalBill);
		outState.putInt(SEEKBAR_TIP, mTipSeekBar.getProgress());
	}
	
	
	/**
	 * Setup controllers. 
	 */
	private void init() {
		Log.v(TAG, "starting tip: " + mSeekBarTip);
		
		mCurrency = MainActivity.sCurrencySymbol;
		
		mBillBeforeTipET = (EditText) getView().findViewById(R.id.billEditText);
		mTipAmountET = (EditText) getView().findViewById(R.id.tipEditText);
		mFinalTipAmountET = (EditText) getView().findViewById(R.id.finalTipPercentEditText);
		mFinalTipValueET = (EditText) getView().findViewById(R.id.finalTipEditText);
		mFinalBillET = (EditText) getView().findViewById(R.id.finalBillEditText);
		mTipSeekBar = (SeekBar) getView().findViewById(R.id.tipSeekBar);
		
		mBillBeforeTipET.addTextChangedListener(billBeforeTipListener);
		mTipSeekBar.setOnSeekBarChangeListener(tipSeekBarChangeListener);
		
		mFriendlyCheckBox = (CheckBox) getView().findViewById(R.id.friendlyCheckBox);
		mSpecialsCheckBox = (CheckBox) getView().findViewById(R.id.specialsCheckBox);
		mOpinionCheckBox = (CheckBox) getView().findViewById(R.id.opinionCheckBox);
		mCourtesyCheckBox = (CheckBox) getView().findViewById(R.id.courtesyCheckBox);
		mFoodCheckBox = (CheckBox) getView().findViewById(R.id.foodCheckBox);
		mDrinksCheckBox = (CheckBox) getView().findViewById(R.id.drinksCheckBox);		
		mAttentiveCheckBox = (CheckBox) getView().findViewById(R.id.attentiveCheckBox);
		mJudgementCheckBox = (CheckBox) getView().findViewById(R.id.judgementCheckBox);
		mGroomedCheckBox = (CheckBox) getView().findViewById(R.id.groomedCheckBox);
		
		mAvailableRadioGroup = (RadioGroup) getView().findViewById(R.id.availableRadioGroup);
		mAvailableBadRadio = (RadioButton) getView().findViewById(R.id.availableBadRadio);
		mAvailableOkRadio = (RadioButton) getView().findViewById(R.id.availableOkRadio);
		mAvailableGoodRadio = (RadioButton) getView().findViewById(R.id.availableGoodRadio);
		
		mCurrencyTextView = (TextView) getView().findViewById(R.id.dollarSignTextView);
		
		mCurrencyTextView.setText(mCurrency);
		mFinalBillET.setText(mCurrency + String.format("%.02f", mFinalBill));
		mFinalTipValueET.setText(mCurrency + String.format("%.02f", mFinalTipAmount));
		mTipSeekBar.setMax(PROGRESS_MAX);
		mTipSeekBar.setProgress(mSeekBarTip);
		mTipAmount = (mTipSeekBar.getProgress()) * .01; 
		mTipAmountET.setText(String.format("%.0f", mTipAmount*100) + "%");
		
		setUpCheckBoxes();
		addChangeListenerToRadios();
	}
	
	private TextWatcher billBeforeTipListener = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			// nothing to do here
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// nothing to do here
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			try{
				mBillBeforeTip = Double.parseDouble(s.toString());
			}catch(NumberFormatException e){
				mBillBeforeTip = 0.0;
			}
			updateTipAndFinalBill();
			
		}
		
	};
	
	private OnSeekBarChangeListener tipSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			
			mTipAmount = (mTipSeekBar.getProgress()) * .01; 
			mTipAmountET.setText(String.format("%.0f", mTipAmount*100) + "%");
			updateTipAndFinalBill();
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// nothing to do here
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// nothing to do here
		}
		
	};
	
	/**
	 * Set checkbox values.
	 */
	private void setUpCheckBoxes(){
		
		mFriendlyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[0] = (mFriendlyCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		mSpecialsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[1] = (mSpecialsCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		mOpinionCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[2] = (mOpinionCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		mCourtesyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[3] = (mCourtesyCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		mFoodCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[4] = (mFoodCheckBox.isChecked())?2:0;
				updateTipAndFinalBill();
			}
		});
		
		mDrinksCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[5] = (mDrinksCheckBox.isChecked())?2:0;
				updateTipAndFinalBill();
			}
		});
		
		mAttentiveCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[6] = (mAttentiveCheckBox.isChecked())?2:0;
				updateTipAndFinalBill();
			}
		});
		
		mJudgementCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[7] = (mJudgementCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		mGroomedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[8] = (mGroomedCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
	}
	
	private void addChangeListenerToRadios() {
		mAvailableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mChecklistValues[9] = (mAvailableBadRadio.isChecked())?-2:0;
				mChecklistValues[10] = (mAvailableOkRadio.isChecked())?1:0;
				mChecklistValues[11] = (mAvailableGoodRadio.isChecked())?3:0;
				updateTipAndFinalBill();
			}

		});
	}
	
	/**
	 * Check auto area and update.
	 */
	private double getFinalTip() {
		int checklistTotal = 0;
		for (int item : mChecklistValues) {
			checklistTotal += item;
		}
		return mTipSeekBar.getProgress() * .01 + checklistTotal*.01;
	}
	
	private void updateTipAndFinalBill() {
		Log.v(TAG, "updating tip and final bill...");
		
		mFinalTipAmount = getFinalTip();
		mFinalBill = mBillBeforeTip + (mFinalTipAmount * mBillBeforeTip);
		mFinalTipAmountET.setText(String.format("%.0f", mFinalTipAmount*100) + "%");
		mFinalTipValueET.setText(mCurrency + String.format("%.02f", (mFinalTipAmount*mBillBeforeTip)));
		mFinalBillET.setText(mCurrency + String.format("%.02f", mFinalBill));
		
		// pass final bill to activity
		((FinalBillChangeListener) getActivity()).onFinalBillChanged(mFinalBill);
	}

}
