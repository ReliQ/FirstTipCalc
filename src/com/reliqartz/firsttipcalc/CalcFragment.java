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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

public class CalcFragment extends Fragment implements OnSharedPreferenceChangeListener {
	
	private static final String TOTAL_BILL = "TOTAL_BILL";
	private static final String CURRENT_TIP = "CURRENT_TIP";
	private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";
	
	private double mBillBeforeTip;
	private double mTipAmount;
	private double mFinalTipAmount;
	private double mFinalBill;
	
	private int[] mChecklistValues = new int[15];
	
	//Preferences
	private static boolean sStartWithKeyboard = false;
	
	EditText billBeforeTipET;
	EditText tipAmountET;
	EditText finalTipAmountET;
	EditText finalTipValueET;
	EditText finalBillET;
	SeekBar tipSeekBar;
	
	CheckBox friendlyCheckBox;
	CheckBox specialsCheckBox;
	CheckBox opinionCheckBox;
	
	CheckBox courtesyCheckBox;
	CheckBox foodCheckBox;
	CheckBox drinksCheckBox;
	
	CheckBox attentiveCheckBox;
	CheckBox judgementCheckBox;
	CheckBox groomedCheckBox;
	
	RadioGroup availableRadioGroup;
	RadioButton availableBadRadio;
	RadioButton availableOkRadio;
	RadioButton availableGoodRadio;
	
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
			mBillBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
			mTipAmount = savedInstanceState.getDouble(CURRENT_TIP);
			mFinalBill = savedInstanceState.getDouble(TOTAL_BILL);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.calc_fragment, container, false);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		loadPreferences();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		initControls();
		setUpCheckBoxes();
		//setButtonOnClickListeners();
		addChangeListenerToRadios();
		
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	public void onResume(){
		super.onResume();
		if(!sStartWithKeyboard)
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		
		outState.putDouble(BILL_WITHOUT_TIP, mBillBeforeTip);
		outState.putDouble(CURRENT_TIP, mTipAmount);
		outState.putDouble(TOTAL_BILL, mFinalBill);
	}
	
	
	/**
	 * Setup controllers. 
	 */
	private void initControls(){
		
		billBeforeTipET = (EditText) getView().findViewById(R.id.billEditText);
		tipAmountET = (EditText) getView().findViewById(R.id.tipEditText);
		finalTipAmountET = (EditText) getView().findViewById(R.id.finalTipPercentEditText);
		finalTipValueET = (EditText) getView().findViewById(R.id.finalTipEditText);
		finalBillET = (EditText) getView().findViewById(R.id.finalBillEditText);
		tipSeekBar = (SeekBar) getView().findViewById(R.id.tipSeekBar);
		tipSeekBar.setProgress(15);
		
		billBeforeTipET.addTextChangedListener(billBeforeTipListener);
		tipSeekBar.setOnSeekBarChangeListener(tipSeekBarChangeListener);
		
		friendlyCheckBox = (CheckBox) getView().findViewById(R.id.friendlyCheckBox);
		specialsCheckBox = (CheckBox) getView().findViewById(R.id.specialsCheckBox);
		opinionCheckBox = (CheckBox) getView().findViewById(R.id.opinionCheckBox);
		courtesyCheckBox = (CheckBox) getView().findViewById(R.id.courtesyCheckBox);
		foodCheckBox = (CheckBox) getView().findViewById(R.id.foodCheckBox);
		drinksCheckBox = (CheckBox) getView().findViewById(R.id.drinksCheckBox);		
		attentiveCheckBox = (CheckBox) getView().findViewById(R.id.attentiveCheckBox);
		judgementCheckBox = (CheckBox) getView().findViewById(R.id.judgementCheckBox);
		groomedCheckBox = (CheckBox) getView().findViewById(R.id.groomedCheckBox);
		
		availableRadioGroup = (RadioGroup) getView().findViewById(R.id.availableRadioGroup);
		availableBadRadio = (RadioButton) getView().findViewById(R.id.availableBadRadio);
		availableOkRadio = (RadioButton) getView().findViewById(R.id.availableOkRadio);
		availableGoodRadio = (RadioButton) getView().findViewById(R.id.availableGoodRadio);
		
	}
	
	private void loadPreferences(){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		sStartWithKeyboard = settings.getBoolean("pref_keyboard", false);
		settings.registerOnSharedPreferenceChangeListener(this);
	}
	
	private TextWatcher billBeforeTipListener = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			
			try{
				mBillBeforeTip = Double.parseDouble(arg0.toString());
			}catch(NumberFormatException e){
				mBillBeforeTip = 0.0;
			}
			updateTipAndFinalBill();
			
		}
		
	};
	
	private OnSeekBarChangeListener tipSeekBarChangeListener= new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			
			mTipAmount = (tipSeekBar.getProgress()) * .01; 
			tipAmountET.setText(String.format("%.02f", mTipAmount));
			updateTipAndFinalBill();
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	

	/**
	 * Set checkbox values.
	 */
	private void setUpCheckBoxes(){
		
		friendlyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[0] = (friendlyCheckBox.isChecked())?4:0;
				updateTipAndFinalBill();
			}
		});
		
		specialsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[1] = (specialsCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		opinionCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[2] = (opinionCheckBox.isChecked())?2:0;
				updateTipAndFinalBill();
			}
		});
		
		courtesyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[3] = (courtesyCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		foodCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[4] = (foodCheckBox.isChecked())?3:0;
				updateTipAndFinalBill();
			}
		});
		
		drinksCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[5] = (drinksCheckBox.isChecked())?2:0;
				updateTipAndFinalBill();
			}
		});
		
		attentiveCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[6] = (attentiveCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		judgementCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[7] = (judgementCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		groomedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				mChecklistValues[8] = (groomedCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
	}
	
	private void addChangeListenerToRadios(){
		availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				mChecklistValues[9] = (availableBadRadio.isChecked())?-2:0;
				mChecklistValues[10] = (availableOkRadio.isChecked())?1:0;
				mChecklistValues[11] = (availableGoodRadio.isChecked())?3:0;
				updateTipAndFinalBill();
			}

		});
	}
	
	/**
	 * Check auto area and update.
	 */
	private void setFinalTip(){
		int checklistTotal = 0;
		for(int item : mChecklistValues)
			checklistTotal += item;
		finalTipAmountET.setText(String.format("%.02f", (tipSeekBar.getProgress()) * .01 + checklistTotal*.01));
	}
	
	private void updateTipAndFinalBill(){
		setFinalTip();
		mFinalTipAmount = Double.parseDouble(finalTipAmountET.getText().toString());
		mFinalBill = mBillBeforeTip + (mFinalTipAmount * mBillBeforeTip);
		finalTipValueET.setText("$" + String.format("%.02f", (mFinalTipAmount*mBillBeforeTip)));
		finalBillET.setText("$" + String.format("%.02f", mFinalBill));
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		loadPreferences();
	}

}
