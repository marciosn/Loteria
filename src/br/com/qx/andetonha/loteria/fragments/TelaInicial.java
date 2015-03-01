package br.com.qx.andetonha.loteria.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.qx.andetonha.loteria.R;

public class TelaInicial extends Fragment{
	
	public TelaInicial() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tela_inicial, container, false);
		return view;
	}

	
}
