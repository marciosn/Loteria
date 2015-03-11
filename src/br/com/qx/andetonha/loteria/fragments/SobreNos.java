package br.com.qx.andetonha.loteria.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.qx.andetonha.loteria.R;

public class SobreNos extends Fragment{
	
	public SobreNos() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sobre_nos, container, false);
		
		TextView andeTonha = (TextView) view.findViewById(R.id.andetonha);
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Something in the way.ttf");
		andeTonha.setTypeface(font);		
		return view;
	}

}
