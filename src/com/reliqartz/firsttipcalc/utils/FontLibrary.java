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

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Utility for font management.
 * 
 * @author Patrick Reid
 */
public class FontLibrary {
	
	public static final String OPEN_SANS = "fonts/open_sans/open_sans_regular.ttf";
	public static final String OPEN_SANS_LIGHT = "fonts/open_sans/open_sans_light.ttf";
	public static final String ROBOTO = "fonts/roboto/roboto_regular.ttf";
	public static final String ROBOTO_LIGHT = "fonts/roboto/roboto_light.ttf";
	public static final String ROBOTO_LIGHT_ITALIC = "fonts/roboto/roboto_light_italic.ttf";
	
	private Activity activity;
	
	/**
	 * @param Activity
	 */
	public FontLibrary(Activity activity) {
		this.activity = activity;
	}
	
	/**
	 * @param font String name of font.
	 * @return Usable instance of requested font. 
	 */
	public Typeface getFont(String font){
		return (Typeface) Typeface.createFromAsset(this.activity.getAssets(), font);
	}
	
	/**
	 * @param font
	 * @param context
	 * @return Usable instance of requested font. 
	 */
	public static Typeface getFont(String font, Context context){
		return (Typeface) Typeface.createFromAsset(context.getAssets(), font);
	}
	
}
