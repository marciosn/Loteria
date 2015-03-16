package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class ProximosSorteiosFragment extends Fragment{
	public static final String TAG = ProximosSorteiosFragment.class.getSimpleName();
	public static final String URL_G1 = "http://g1.globo.com/loterias/index.html";
	public static final String TOAST_MESSAGE = "Não foi possível buscar os resultados!";
	private ProgressDialog pDialog;
	private RequestQueue rq;
	
	private RelativeLayout relativeLayout;
	private LinearLayout linearLayout;
	private Context context;
	
	private TextView proximo_concurso_megasena;
	private TextView proximo_concurso_duplasena;
	private TextView proximo_concurso_lotofacil;
	private TextView proximo_concurso_quina;
	private TextView proximo_concurso_timemania;
	private Utils utils;
	
	public ProximosSorteiosFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_proximos_sorteios, container, false);
		context = getActivity();
		rq = Volley.newRequestQueue(context);
		utils = new Utils(getActivity());
		try {
			utils.setStyleActionBar(getResources(), getActivity());
			carregarTextView(view);
			relativeLayout.setVisibility(View.GONE);
			doIfOnline();
		} catch (Exception e) {
			hidePDialog();
			linearLayout.setVisibility(View.VISIBLE);
			Log.e(TAG, e.toString());
			Toast.makeText(context, TOAST_MESSAGE, Toast.LENGTH_LONG).show();
		}
		return view;
	}
	
	public void getResultados() {

		pDialog = new ProgressDialog(context);
		pDialog.setCancelable(false);
		pDialog.setMessage("Buscando...");
		pDialog.show();

		StringRequest request = new StringRequest(Request.Method.GET, URL_G1,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						hidePDialog();
						relativeLayout.setVisibility(View.VISIBLE);
						
						try {
							Document doc = Jsoup.parse(response);

							
							proximo_concurso_megasena.setText(doc.getElementsByClass("wrapper-proximo-concurso").get(5).text() +"\n"+
									doc.getElementsByClass("valor-premio").get(5).text());
							proximo_concurso_duplasena.setText(doc.getElementsByClass("wrapper-proximo-concurso").get(0).text() +"\n"+
									doc.getElementsByClass("valor-premio").get(0).text());
							proximo_concurso_lotofacil.setText(doc.getElementsByClass("wrapper-proximo-concurso").get(2).text()  +"\n"+
									doc.getElementsByClass("valor-premio").get(2).text());
							proximo_concurso_quina.setText(doc.getElementsByClass("wrapper-proximo-concurso").get(6).text() +"\n"+
									doc.getElementsByClass("valor-premio").get(6).text());
							proximo_concurso_timemania.setText(doc.getElementsByClass("wrapper-proximo-concurso").get(7).text() +"\n"+
									doc.getElementsByClass("valor-premio").get(7).text());
						} catch (Exception e) {
							hidePDialog();
							linearLayout.setVisibility(View.VISIBLE);
							Log.e(TAG, "getResultados "+e.toString());
							Toast.makeText(context, TOAST_MESSAGE, Toast.LENGTH_LONG).show();
						} catch (OutOfMemoryError e) {
							hidePDialog();
							linearLayout.setVisibility(View.VISIBLE);
							Log.e(TAG, "getResultados "+e.toString());
							Toast.makeText(context, TOAST_MESSAGE, Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hidePDialog();
						linearLayout.setVisibility(View.VISIBLE);
						Log.d(TAG, error.toString());
						Toast.makeText(context, TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
					}
				});
		int timeout = 30000;
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
		relativeLayout = (RelativeLayout) view.findViewById(R.id.proximos_sorteios_layout);
		linearLayout = (LinearLayout) view.findViewById(R.id.error_proximos_sorteios);
		
		proximo_concurso_megasena = (TextView) view.findViewById(R.id.proximo_concurso_megasena);
		proximo_concurso_duplasena = (TextView) view.findViewById(R.id.proximo_concurso_duplasena);
		proximo_concurso_lotofacil = (TextView) view.findViewById(R.id.proximo_concurso_lotofacil);
		proximo_concurso_quina = (TextView) view.findViewById(R.id.proximo_concurso_quina);
		proximo_concurso_timemania = (TextView) view.findViewById(R.id.proximo_concurso_timemania);
		
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doIfOnline();
			}
		});
	}
}
