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

import java.util.ArrayList;

import com.reliqartz.firsttipcalc.beta.R;
import com.reliqartz.firsttipcalc.utils.FontLibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter for ratios.
 * @author Patrick Reid
 */
public class RatioShareAdapter extends BaseAdapter {
	
	private ArrayList<Share> mShares; 
	private Context mContext;
	private int mFinalRatioSplit;
	private int[] mRatios;
	private double mFinalBill;
	
	RatioShareAdapter(Context context, int[] ratios, double finalBill) {
		mContext = context;
		mRatios = ratios;
		mFinalBill = finalBill;
		mShares = new ArrayList<Share>();
		for (int i=0; i < mRatios.length; i++) {
			mFinalRatioSplit += mRatios[i];
		}
		for (int ratio: mRatios) {
			mShares.add(new Share(ratio, mFinalBill/mFinalRatioSplit*ratio));
		}
	}	
	
	@Override
	public int getCount() {
		return mShares.size();
	}

	@Override
	public Object getItem(int position) {
		return mShares.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_item_share, parent, false);
			holder = new ViewHolder(row);
			row.setTag(holder);
			//Log.v(TAG, "creating new row..");
		} else {
			holder = (ViewHolder) row.getTag();
			//Log.v(TAG, "recycling row...");
		}
		holder.shareWeight.setText("Share " + mShares.get(position).weight
				+ ": ");
		holder.shareValue.setText(String.format("%s%.2f",
				MainActivity.sCurrencySymbol, mShares.get(position).value));
		return row;
	}
	
	/**
	 * A single ratio share.
	 * @author Patrick Reid
	 */
	private class Share {
		int weight;
		double value;
		Share(int weight, double value) {
			this.weight = weight;
			this.value = value;
		}
	}
	
	/**
	 * View holder for single row.
	 * @author Patrick Reid
	 */
	class ViewHolder {
		TextView shareWeight, shareValue;
		ViewHolder(View v) {
			shareWeight = (TextView) v.findViewById(R.id.shareTextView);
			shareWeight.setTypeface(FontLibrary.getFont(FontLibrary.ROBOTO_LIGHT, mContext));
			shareValue = (TextView) v.findViewById(R.id.valueTextView);
		}
	}
}