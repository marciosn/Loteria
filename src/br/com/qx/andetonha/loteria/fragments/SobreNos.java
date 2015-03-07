package br.com.qx.andetonha.loteria.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.qx.andetonha.loteria.R;

public class SobreNos extends Fragment{
	
	public SobreNos() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sobre_nos, container, false);
		return view;
	}

}
