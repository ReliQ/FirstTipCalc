package com.reliqartz.firsttipcalc;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

public class TipCalcFragment extends Fragment implements OnSharedPreferenceChangeListener {
	
	private double billBeforeTip;
	private double tipAmount;
	private double finalTipAmount;
	private double finalBill;
	
	private int[] checklistValues = new int[15];
	
	//Preferences
	private static boolean startWithKeyboard = false;
	
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
	
	Spinner problemsSpinner;
	
	Button startChronometerButton;
	Button pauseChronometerButton;
	Button resetChronometerButton;
	
	/* Optional Chrono
	Chronometer timeWaitingChronometer;
	long secondsYouWaited = 0;
	TextView timeWaitingTextView;
	*/
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.tip_calc_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		loadPreferences();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		initControls();
		setUpCheckBoxes();
		//setButtonOnClickListeners();
		addChangeListenerToRadios();
		addItemSelectedListenerToSpinner();	
		
	}
	
	public void onResume(){
		super.onResume();
		getStatics();
		if(!startWithKeyboard)
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	private void getStatics(){
		billBeforeTip = ((CrazyTipCalc) getActivity()).billBeforeTip;
		tipAmount = ((CrazyTipCalc) getActivity()).tipAmount;
		finalTipAmount = ((CrazyTipCalc) getActivity()).finalTipAmount;
		finalBill = ((CrazyTipCalc) getActivity()).finalBill;
	}
	
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
				
		problemsSpinner = (Spinner) getView().findViewById(R.id.problemsSpinner);
		
		/* Optional Chrono
		startChronometerButton = (Button) findViewById(R.id.startChronometerButton);
		pauseChronometerButton = (Button) findViewById(R.id.pauseChronometerButton);
		resetChronometerButton = (Button) findViewById(R.id.resetChronometerButton);
		timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);
		timeWaitingTextView = (TextView) findViewById(R.id.timeWaitingTextView);
		*/
	}
	
	private void loadPreferences(){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		startWithKeyboard = settings.getBoolean("pref_keyboard", false);
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
				billBeforeTip = Double.parseDouble(arg0.toString());
			}catch(NumberFormatException e){
				billBeforeTip = 0.0;
			}
			updateTipAndFinalBill();
			
		}
		
	};
	
	private OnSeekBarChangeListener tipSeekBarChangeListener= new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			
			tipAmount = (tipSeekBar.getProgress()) * .01; 
			tipAmountET.setText(String.format("%.02f", tipAmount));
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
	
	

	private void setUpCheckBoxes(){
		
		friendlyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[0] = (friendlyCheckBox.isChecked())?4:0;
				updateTipAndFinalBill();
			}
		});
		
		specialsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[1] = (specialsCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		opinionCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[2] = (opinionCheckBox.isChecked())?2:0;
				updateTipAndFinalBill();
			}
		});
		
		courtesyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[3] = (courtesyCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		foodCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[4] = (foodCheckBox.isChecked())?3:0;
				updateTipAndFinalBill();
			}
		});
		
		drinksCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[5] = (drinksCheckBox.isChecked())?2:0;
				updateTipAndFinalBill();
			}
		});
		
		attentiveCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[6] = (attentiveCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		judgementCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[7] = (judgementCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
		
		groomedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				checklistValues[8] = (groomedCheckBox.isChecked())?1:0;
				updateTipAndFinalBill();
			}
		});
	}
	
	private void addChangeListenerToRadios(){
		availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				checklistValues[9] = (availableBadRadio.isChecked())?-2:0;
				checklistValues[10] = (availableOkRadio.isChecked())?1:0;
				checklistValues[11] = (availableGoodRadio.isChecked())?3:0;
				updateTipAndFinalBill();
			}

		});
	}
	
	private void addItemSelectedListenerToSpinner(){
		problemsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				checklistValues[12] = (problemsSpinner.getSelectedItem()).equals("Bad")?-1:0;
				checklistValues[13] = (problemsSpinner.getSelectedItem()).equals("OK")?1:0;
				checklistValues[14] = (problemsSpinner.getSelectedItem()).equals("Good")?2:0;
				updateTipAndFinalBill();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
	/*
	 * Check auto area and update
	 * */
	private void setFinalTip(){
		int checklistTotal = 0;
		
		for(int item : checklistValues)
			checklistTotal += item;
			
		finalTipAmountET.setText(String.format("%.02f", (tipSeekBar.getProgress()) * .01 + checklistTotal*.01));
	}
	
	private void updateTipAndFinalBill(){
		
		setFinalTip();
		
		finalTipAmount = Double.parseDouble(finalTipAmountET.getText().toString());
		finalBill = billBeforeTip + (finalTipAmount * billBeforeTip);
		
		finalTipValueET.setText("$" + String.format("%.02f", (finalTipAmount*billBeforeTip)));
		finalBillET.setText("$" + String.format("%.02f", finalBill));
	}
	
	
	/* 
	 * Chronometer Stuff:
	 * */
	/*
	private void setButtonOnClickListeners(){
		startChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				int stoppedMilliseconds = 0;
				
				String chronoText = timeWaitingChronometer.getText().toString();
				String array[] = chronoText.split(":");
				
				if(array.length == 2){
					stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
				}else if(array.length == 3){
					stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
				}
				
				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
				
				secondsYouWaited = Long.parseLong(array[1]);
				
				updateTipBasedOnTimeWaited(secondsYouWaited);
				
				timeWaitingChronometer.start();
			}
			
		});
		
		pauseChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				timeWaitingChronometer.stop();	
			}
			
		});
		
		resetChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
				secondsYouWaited = 0;
			}
			
		});
	}
	
	private void updateTipBasedOnTimeWaited(long secondsYouWaited){
		checklistValues[9] = (secondsYouWaited > 10)?-2:2;
		
		setTipFromWaitressChecklist();
		updateTipAndFinalBill();
	}
	
	*/

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		loadPreferences();
	}

}
