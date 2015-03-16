package br.com.qx.andetonha.loteria.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import br.com.qx.andetonha.loteria.R;
import br.com.qx.andetonha.loteria.utils.Utils;

public class GerarApostasFragment extends Fragment {
	private TextView numerosTV;
	private ImageButton btn_gerarApostas;
	private Context context;
	private Utils utils;
	private int tamanhoAposta = 0;

	public GerarApostasFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gerar_apostas,container, false);
		context = getActivity();
		utils = new Utils(getActivity());
		try {
			utils.setStyleActionBar(getResources(), getActivity());
			numerosTV = (TextView) view.findViewById(R.id.numeros_da_aposta);
			btn_gerarApostas = (ImageButton) view.findViewById(R.id.btn_gerarApostas);

			btn_gerarApostas.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					numerosTV.setText(palpite());
				}
			});
			TextView site_mega_sena = (TextView) view.findViewById(R.id.tv_site_mega_sena);
			site_mega_sena.setMovementMethod(LinkMovementMethod.getInstance());
			/**/
			
			final TextView tv_valorBP = (TextView) view
					.findViewById(R.id.tv_valorBP);
			
			final TextView tv_betValue = (TextView) view
					.findViewById(R.id.tv_valorAposta);

			SeekBar sk_valorAposta = (SeekBar) view.findViewById(R.id.bp_valorAposta);
			sk_valorAposta.setMax(9);
			sk_valorAposta.setProgress(0);
			tamanhoAposta = 6;
			tv_valorBP.setText("Quantidade de nº jogados: 6");
			tv_betValue.setText("Valor da aposta aprox :" + "\n" + "R$2,50");
			sk_valorAposta.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					tv_valorBP.setText("Quantidade de nº jogados "
							+ String.valueOf(progress + 6));
					switch (progress) {
					case 0:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + "R$2,50");
						tamanhoAposta = 6;
						break;
					case 1:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + " R$17,50");
						tamanhoAposta = 7;
						break;
					case 2:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + " R$70,00");
						tamanhoAposta = 8;
						break;
					case 3:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + " R$210,00");
						tamanhoAposta = 9;
						break;
					case 4:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + " R$525,00");
						tamanhoAposta = 10;
						break;
					case 5:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + " R$1.155,00");
						tamanhoAposta = 11;
						break;
					case 6:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + " R$2.310,00");
						tamanhoAposta = 12;
						break;
					case 7:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + "R$4.290,00");
						tamanhoAposta = 13;
						break;
					case 8:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + "R$7.507,50");
						tamanhoAposta = 14;
						break;
					case 9:
						tv_betValue.setText("Valor da aposta aprox. :" + "\n" + "R$12.512,50");
						tamanhoAposta = 15;
						break;
					}

				}
			});
		} catch (Exception e) {
			Log.e("Gerador de números", "onCreateView "+e.toString());
			Toast.makeText(context, "Ops!, tivemos problemas técnicos!", Toast.LENGTH_LONG).show();
		}
		return view;
	}

	public String palpite() {
		String palpiteDaSena = "";
		List<Integer> list = geraNumerosOrdenados(tamanhoAposta);
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				palpiteDaSena += String.valueOf(list.get(i));
			} else {
				palpiteDaSena += String.valueOf(list.get(i)) + " - ";
			}
		}
		return palpiteDaSena;
	}

	public List<Integer> geraNumerosOrdenados(int tamanhoAposta) {
		List<Integer> lista = new ArrayList<Integer>();
		for (int i = 0; i < tamanhoAposta; i++) {
			int sena = new Random().nextInt(60) + 1;
			if (!lista.contains(sena))
				lista.add(sena);
			else
				lista.add(new Random().nextInt(60) + 1);
		}
		Collections.sort(lista);
		return lista;
	}
}
