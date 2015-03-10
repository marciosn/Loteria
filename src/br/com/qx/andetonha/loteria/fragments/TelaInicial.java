package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.qx.andetonha.loteria.R;
import br.com.qx.andetonha.loteria.utils.Utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class TelaInicial extends Fragment{
	
	public static final String MEGA_SENA = "Mega Sena";
	public static final String QUINA = "Quina";
	public static final String LOTO_FACIL = "Loto Fácil";
	public static final String TIMEMANIA= "Timemania";
	public static final String DUPLA_SENA = "Dupla Sena";
	public static final String TAG = TelaInicial.class.getSimpleName();
	public static final String URL_G1 = "http://g1.globo.com/loterias/index.html";
	private ProgressDialog pDialog;
	private RequestQueue rq;
	private Context context;
	
	private RelativeLayout relativeLayout;
	
	private TextView premio_megasena;
	private TextView premio_quina;
	private TextView premio_lotofacil;
	private TextView premio_timemania;
	private TextView premio_duplasena;
	
	private TextView concurso_megasena;
	private TextView concurso_quina;
	private TextView concurso_lotofacil;
	private TextView concurso_timemania;
	private TextView concurso_duplasena;
	
	private RelativeLayout btn_megasena;
	private RelativeLayout btn_quina;
	private RelativeLayout btn_lotofacil;
	private RelativeLayout btn_duplasena;
	private RelativeLayout btn_timemania;
	
	private FragmentManager fragmentManager;
	private Fragment fragment;
	private ActionBar actionBar;
	
	public TelaInicial(FragmentManager fragmentManager, ActionBar actionBar) {
		this.fragmentManager = fragmentManager;
		this.actionBar = actionBar;
	}
	
	public TelaInicial() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tela_inicial, container, false);
		
		context = getActivity();
		rq = Volley.newRequestQueue(context);
		
		try {
			carregarTextView(view);
			relativeLayout.setVisibility(View.GONE);
			doIfOnline();
		} catch (Exception e) {
			Log.e(TAG, "onCreateView "+e.toString());
			Toast.makeText(context, " onCreateView Não foi possível carregar os dados!", Toast.LENGTH_LONG).show();
		}
		return view;
	}

	@SuppressLint("DefaultLocale")
	public void getResultados() {

		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Buscando...");
		pDialog.show();

		StringRequest request = new StringRequest(Request.Method.GET, URL_G1,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						relativeLayout.setVisibility(View.VISIBLE);
						hidePDialog();
						Document doc = Jsoup.parse(response);
						
						try {
							premio_megasena.setText(doc.getElementsByClass("label-premio").get(0).text().toUpperCase());
							premio_duplasena.setText(doc.getElementsByClass("coluna-ganhadores-concurso").get(0).text().toUpperCase());
							premio_lotofacil.setText(doc.getElementsByClass("coluna-ganhadores-concurso").get(3).text().toUpperCase());
							premio_quina.setText(doc.getElementsByClass("coluna-ganhadores-concurso").get(6).text().toUpperCase());
							premio_timemania.setText(doc.getElementsByClass("coluna-ganhadores-concurso").get(7).text().toUpperCase());
							
							
							concurso_megasena.setText(doc.getElementsByClass("loteria-numero").text() + " . " + 
									doc.getElementsByClass("loteria-data").text());
							concurso_duplasena.setText(doc.getElementsByClass("coluna-dados-concurso").get(0).text());
							concurso_lotofacil.setText(doc.getElementsByClass("coluna-dados-concurso").get(3).text());
							concurso_quina.setText(doc.getElementsByClass("coluna-dados-concurso").get(6).text());
							concurso_timemania.setText(doc.getElementsByClass("coluna-dados-concurso").get(7).text());
							
						} catch (Exception e) {
							Log.e(TAG, e.toString());
							Toast.makeText(context, "Não foi possível carregar os dados!", Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						relativeLayout.setVisibility(View.GONE);
						Log.d(TAG, error.toString());
						Toast.makeText(context, "A busca falhou. Tente novamente!", Toast.LENGTH_LONG).show();
					}
				});
		int timeout = 10000;
		RetryPolicy policy = new DefaultRetryPolicy(timeout,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		request.setRetryPolicy(policy);
		request.setTag(TAG);
		rq.add(request);

	}
	
	public void doIfOnline(){
		if (new Utils(context).isOnline()) {
			getResultados();
		} else {
			Toast.makeText(context, "Sem conexão com a internet.", Toast.LENGTH_LONG).show();
		}
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
		
	}
	
	public void carregarTextView(View view){
		
		relativeLayout = (RelativeLayout) view.findViewById(R.id.tela_inicial_layout);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		fragment = null;
		
		premio_megasena = (TextView) view.findViewById(R.id.premio_megasenaLabel);
		premio_quina = (TextView) view.findViewById(R.id.premio_quinaLabel);
		premio_lotofacil = (TextView) view.findViewById(R.id.premio_lotofacilLabel);
		premio_timemania = (TextView) view.findViewById(R.id.premio_timemaniaLabel);
		premio_duplasena = (TextView) view.findViewById(R.id.premio_duplasenaLabel);
		
		concurso_megasena = (TextView) view.findViewById(R.id.concurso_megasena_new);
		concurso_quina = (TextView) view.findViewById(R.id.concurso_quina);
		concurso_lotofacil = (TextView) view.findViewById(R.id.concurso_lotofacil);
		concurso_timemania = (TextView) view.findViewById(R.id.concurso_timemania);
		concurso_duplasena = (TextView) view.findViewById(R.id.concurso_duplasena);
		
		btn_megasena = (RelativeLayout) view.findViewById(R.id.Layout_megasena);
		btn_quina = (RelativeLayout) view.findViewById(R.id.Layout_quina);
		btn_duplasena = (RelativeLayout) view.findViewById(R.id.Layout_duplasena);
		btn_lotofacil = (RelativeLayout) view.findViewById(R.id.Layout_lotofacil);
		btn_timemania = (RelativeLayout) view.findViewById(R.id.Layout_timemania);
		
		btn_megasena.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFragment(new MegaSenaFragment());
				actionBar.setTitle(MEGA_SENA);
			}
		});
		
		btn_quina.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFragment(new QuinaFragment());
				actionBar.setTitle(QUINA);
			}
		});
		
		btn_duplasena.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFragment(new DuplaSenaFragment());
				actionBar.setTitle(DUPLA_SENA);
			}
		});
		
		btn_lotofacil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFragment(new LotoFacilFragment());
				actionBar.setTitle(LOTO_FACIL);
			}
		});
		btn_timemania.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFragment(new TimeManiaFragment());
				actionBar.setTitle(TIMEMANIA);
			}
		});
	}
	
	public void setFragment(Fragment fragment){
		try {
			fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
		} catch (Exception e) {
			Toast.makeText(context, "Ops!, Temos um problema técnico!", Toast.LENGTH_LONG).show();
		}
	}
}
