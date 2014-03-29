package com.reliqartz.firsttipcalc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. 
 * Use the {@link SplitterFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class SplitterFragment extends Fragment {
	private static final String TAG = "FirstTip/Splitter";
	
	private static final String ARG_PARAM1 = "param1";

	// private String mParam1;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
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
		/*if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
		}*/
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_splitter, container, false);
	}

}
