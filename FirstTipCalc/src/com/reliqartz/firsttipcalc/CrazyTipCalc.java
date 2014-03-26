package com.reliqartz.firsttipcalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class CrazyTipCalc extends FragmentActivity {
	private static final String TAG = "CrazyTipCalc";
	
	private static final String TOTAL_BILL = "TOTAL_BILL";
	private static final String CURRENT_TIP = "CURRENT_TIP";
	private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";
	
	public double billBeforeTip;
	public double tipAmount, finalTipAmount;
	public double finalBill;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
		tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
		finalBill = savedInstanceState.getDouble(TOTAL_BILL);
	}
	
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		
		outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
		outState.putDouble(CURRENT_TIP, tipAmount);
		outState.putDouble(TOTAL_BILL, finalBill);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crazy_tip_calc, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent i; // 
		Log.d(TAG, "Manu item selected: "+ item.getItemId());
		
		switch(item.getItemId()){
			case R.id.action_refresh:
				i = getIntent(); finish();
				//i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(i);
				overridePendingTransition (0,0);
				return true;
			case R.id.action_settings:
				i = new Intent(this, Preferences.class);
				startActivity(i);
				return true;
			default:
		        return super.onOptionsItemSelected(item);
		}
	}
	
}
