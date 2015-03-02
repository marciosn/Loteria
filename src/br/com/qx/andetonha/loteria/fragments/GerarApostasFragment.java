package br.com.qx.andetonha.loteria.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import br.com.qx.andetonha.loteria.R;

public class GerarApostasFragment extends Fragment {
	private TextView numerosTV;
	private Button btn_gerarApostas;
	private int tamanhoJogada;
	private Context context;
	
	private int tamanhoAposta = 0;
	
	public GerarApostasFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gerar_apostas, container, false);
		
		context = getActivity();
		
		numerosTV = (TextView) view.findViewById(R.id.numeros_da_aposta);
		btn_gerarApostas = (Button) view.findViewById(R.id.btn_gerarApostas);
		
		btn_gerarApostas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				numerosTV.setText(palpite());
			}
		});
		final TextView tv_skValue = (TextView)view.findViewById(R.id.tv_skValue);
		final TextView tv_betValue = (TextView)view.findViewById(R.id.tv_betValue);
		
		SeekBar sk_betValue = (SeekBar)view.findViewById(R.id.sk_betValue);
		sk_betValue.setMax(9);
		sk_betValue.setProgress(0);
		tamanhoAposta = 6;
		tv_skValue.setText("Quantidade de nº jogados: 6");
		tv_betValue.setText("Valor da aposta aprox. : R$2,50");
		sk_betValue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
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
				tv_skValue.setText("Quantidade de nº jogados " + String.valueOf(progress + 6));
				switch(progress){
				case 0:
					tv_betValue.setText("Valor da aposta aprox. : R$2,50");
					tamanhoAposta = 6;
					break;
				case 1:
					tv_betValue.setText("Valor da aposta aprox. : R$17,50");
					tamanhoAposta = 7;
					break;
				case 2:
					tv_betValue.setText("Valor da aposta aprox. : R$70,00");
					tamanhoAposta = 8;
					break;
				case 3:
					tv_betValue.setText("Valor da aposta aprox. : R$210,00");
					tamanhoAposta = 9;
					break;
				case 4:
					tv_betValue.setText("Valor da aposta aprox. : R$525,00");
					tamanhoAposta = 10;
					break;
				case 5:
					tv_betValue.setText("Valor da aposta aprox. : R$1.155,00");
					tamanhoAposta = 11;
					break;
				case 6:
					tv_betValue.setText("Valor da aposta aprox. : R$2.310,00");
					tamanhoAposta = 12;
					break;
				case 7:
					tv_betValue.setText("Valor da aposta aprox. : R$4.290,00");
					tamanhoAposta = 13;
					break;
				case 8:
					tv_betValue.setText("Valor da aposta aprox. : R$7.507,50");
					tamanhoAposta = 14;
					break;
				case 9:
					tv_betValue.setText("Valor da aposta aprox. : R$12.512,50");
					tamanhoAposta = 15;
					break;
				}
				
			}
		});
		
		return view;
	}
	
	public String palpite(){
		String palpiteDaSena = "";		
			List<Integer> list = geraNumerosOrdenados(tamanhoAposta);
			for(int i = 0 ; i < list.size() ; i++){
				palpiteDaSena += String.valueOf(list.get(i)) + " ";
			}
		return palpiteDaSena;
	}
		
	public List<Integer> geraNumerosOrdenados(int tamanhoAposta){	
		boolean troca = true;
		int aux;
		List<Integer> lista = new ArrayList<Integer>();
		for(int i = 0 ; i < tamanhoAposta ; i++){
			int sena = new Random().nextInt(60) + 1;
			
			if(!lista.contains(sena))
				lista.add(sena);
			else
			lista.add(new Random().nextInt(60) + 1);
		}
		while(troca){
			troca = false;
			for(int j = 0 ; j < lista.size()-1 ; j++){
				if(lista.get(j) > lista.get(j + 1)){
					aux = lista.get(j);
					lista.set(j, lista.get(j + 1));
					lista.set(j + 1, aux);
					troca = true;
				}
			}
		}
		
		return lista;
	}
	
	/*public boolean valida(){
		boolean apostaValida = false;
		String aposta = tamanhoJogada.getText().toString();
		if(aposta == null || aposta.equals("")){
			Toast.makeText(context, "Não pode ser vazio", Toast.LENGTH_SHORT).show();
		}else{
			tamanhoAposta = Integer.valueOf(aposta);
			if(tamanhoAposta >= 5 && tamanhoAposta <= 15){
				apostaValida = true;
			}else{
				tamanhoJogada.setText("");
				Toast.makeText(context, "Jogada deve ser entre 5 e 15", Toast.LENGTH_SHORT).show();
			}
		}
		return apostaValida;
	}*/
}
