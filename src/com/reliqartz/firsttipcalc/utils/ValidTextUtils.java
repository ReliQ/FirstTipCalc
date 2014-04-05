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

package com.reliqartz.firsttipcalc.utils;

import android.util.Log;

/**
 * Set of utilities for text validation.
 * @author Patrick Reid
 */
public class ValidTextUtils {
	public static final String TAG = "ValidTextUtils";
	
	/**
	 * Check whether a ratio is valid.
	 * @param 	ratio	The string containing the proposed ratio.
	 * @param 	split	The amount of shares the ratio should be split in. 
	 * 			Pass less than 2 to avoid checking shares. 
	 * @return 	Whether ratio is valid for required shares.
	 */
	public static boolean validRatio(String ratio, int split) {
		boolean result =  false;
		if (ratio.matches("^[0-9]:[[0-9]:]*[0-9]$")) {
			Log.v(TAG, "Checking ratio " + ratio + " results follow...");
			Log.i(TAG, "RATIO PATTERN MATCHED");
			if (split > 1) {
				String[] ratios = ratio.split(":");
				if (ratios.length == split) {
					Log.i(TAG, "RATIO IS VALID, hoorah!");
					result = true;
				} else {
					Log.w(TAG, "INVALID AMOUNT OF SHARES, RATIO INVALID");
				}
			} else {
				result = true;
			}
		} else {
			Log.w(TAG, "INVALID RATIO");
		}
		return result;
	}
}
