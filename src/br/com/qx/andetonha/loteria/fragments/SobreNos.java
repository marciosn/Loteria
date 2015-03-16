package br.com.qx.andetonha.loteria.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.qx.andetonha.loteria.R;
import br.com.qx.andetonha.loteria.utils.Utils;

public class SobreNos extends Fragment{
	private Utils utils;
	
	public SobreNos() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sobre_nos, container, false);
		
		TextView andeTonha = (TextView) view.findViewById(R.id.andetonha);
		utils = new Utils(getActivity());
		try {
			utils.setStyleActionBar(getResources(), getActivity());
			Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Something in the way.ttf");
			andeTonha.setTypeface(font);
		} catch (Exception e) {
			Log.e("SOBRE", e.toString());
		}
		return view;
	}

}
