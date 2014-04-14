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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reliqartz.firsttipcalc.R;
import com.reliqartz.firsttipcalc.interfaces.SplitRatioChangeListener;
import com.reliqartz.firsttipcalc.utils.ValidTextUtils;

/**
 * Ratio dialog.
 * @author Patrick Reid
 */
public class RatioDialog extends DialogFragment {
	public static final String TAG = "FirstTip/RatioDialog";
	
	private static final String ARG_BILL = "__BILL__";
	private static final String ARG_SPLIT_FOR = "__SPLIT__";
	
	private boolean mAllowClose = false; // close with any ratio
	private int mSplitFor = 2;
	private double mBill = 0.0;
	private SplitRatioChangeListener mActivity;
	private View mView;
	private EditText mRatioEditText;
	
	/**
	 * Factory method to create a new instance of this fragment using
	 * the provided parameters.
	 */
	public static RatioDialog newInstance(double bill, int splitFor) {
		RatioDialog fragment = new RatioDialog();
		Bundle args = new Bundle();
		args.putDouble(ARG_BILL, bill);
		args.putInt(ARG_SPLIT_FOR, splitFor);
		fragment.setArguments(args);
		return fragment;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSplitFor = getArguments().getInt(ARG_SPLIT_FOR, mSplitFor);
		mBill = getArguments().getDouble(ARG_BILL, mBill);
		Log.i(TAG, "Expecting ratio of " + mBill + " split for " + mSplitFor);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mActivity = (SplitRatioChangeListener) activity;
		} catch(ClassCastException e) {
			Log.e(TAG, "Calling activity must implement SplitRatioChangeListener!!");
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_ratio_dialog, null);
		mRatioEditText = (EditText) mView.findViewById(R.id.ratioEditText);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
		builder.setTitle(R.string.title_ratio_dialog);
		builder.setView(mView);
		((TextView) mView.findViewById(R.id.ratioHintTextView))
			.setText(getString(R.string.ratio_hint_text, mSplitFor));
		builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.v(TAG, "Ratio cancelled.");
			}
		});
		builder.setPositiveButton(R.string.title_section2, null);
		return builder.create();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		
		// prevent canceling
		setCancelable(false);
		
		// setup TextWatcher for input
		mRatioEditText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// do nothing
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// do nothing
			}
			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				// allow only 0-9 and :
				if (str.length() > 0) {
					int length = str.length();
					// if last character is not allowed
					if ( ! str.substring(length-1, length).matches("[:0-9]")) {
				        str = str.substring(0, length-1);
				        s.clear(); 		// clear editable 
				        s.append(str); 	// append acceptable string
				    }
				}
			}
			
		});
		
		// get the shown dialog and override the positive buttons OnClickListener
		AlertDialog dialog = (AlertDialog) getDialog();
		if (dialog != null) {
			dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String ratio = mRatioEditText.getText().toString();
					if (ValidTextUtils.validRatio(ratio, mSplitFor) || mAllowClose) {
						mActivity.onSplitRatioChanged(ratio);
						dismiss();
					} else {
						Toast.makeText(getActivity(), 
								getString(R.string.ratio_invalid, mSplitFor), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}	
	}
	
}
