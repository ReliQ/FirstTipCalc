/**
 * Copyright (c) 2014 Michal Dabski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
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

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.reliqartz.firsttipcalc.utils.ViewTraverser.ForeachAction;

/**
 * Applies selected font to the view and all views
 * @author Michal
 *
 */
public class FontApplicator {
	final private Typeface font;
	
	public FontApplicator(Typeface font) {
		this.font = font;
	}
	
	/**
	 * @param context
	 * @param fontName	The full path to the font, as defined in font library.
	 */
	public FontApplicator(Context context, String fontName) {
		this(Typeface.createFromAsset(context.getAssets(), fontName));
	}
	
	public FontApplicator(AssetManager assets, String assetFontName) {
		this.font = Typeface.createFromAsset(assets, assetFontName);
	}
	
	/**
	 * Applies font to the view and/or its children
	 * @param root
	 * @return FontApplicator
	 */
	public FontApplicator applyFont(View root) {
		if (root == null) return this;
		new ViewTraverser(root).traverse(new ForeachAction<View>()
		{
			
			@Override
			public void onElement(View element)
			{
				if (element instanceof TextView)
				{
					((TextView) element).setTypeface(font);
				}
			}
		});
		
		return this;
	}
	
	
	/**
	 * Implies font settings.
	 * @author Patrick Reid
	 */
	public interface Fonty {
		public void applyFonts();
	}

}
