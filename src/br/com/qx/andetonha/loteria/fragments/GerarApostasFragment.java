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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.qx.andetonha.loteria.R;

public class GerarApostasFragment extends Fragment {
	private TextView numerosTV;
	private Button btn_gerarApostas;
	private EditText tamanhoJogada;
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
		tamanhoJogada = (EditText) view.findViewById(R.id.tamanhoJogado);
		
		btn_gerarApostas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				numerosTV.setText(palpite());
			}
		});
		
		return view;
	}
	
	public String palpite(){
		valida();
		String palpiteDaSena = "";	
		if(valida()){
			List<Integer> list = geraNumerosOrdenados(tamanhoAposta);
			for(int i = 0 ; i < list.size() ; i++){
				palpiteDaSena += String.valueOf(list.get(i)) + " ";
			}
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
	
	public boolean valida(){
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
	}
}
