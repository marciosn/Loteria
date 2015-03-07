package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import br.com.qx.andetonha.loteria.R;
import br.com.qx.andetonha.loteria.utils.Utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProximosSorteiosFragment extends Fragment{
	public static final String TAG = ProximosSorteiosFragment.class.getSimpleName();
	public static final String URL_G1 = "http://g1.globo.com/loterias/index.html";
	private ProgressDialog pDialog;
	private RequestQueue rq;
	
	private Context context;
	
	private TextView proximo_concurso_megasena;
	private TextView proximo_concurso_duplasena;
	private TextView proximo_concurso_lotofacil;
	private TextView proximo_concurso_quina;
	private TextView proximo_concurso_timemania;
	
	public ProximosSorteiosFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_proximos_sorteios, container, false);
		context = getActivity();
		rq = Volley.newRequestQueue(context);
		
		try {
			doIfOnline();
			carregarTextView(view);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			Toast.makeText(context, "Não foi possível carregar os dados!", Toast.LENGTH_LONG).show();
		}
		
		return view;
	}
	
	public void getResultados() {

		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Buscando...");
		pDialog.show();

		StringRequest request = new StringRequest(Request.Method.GET, URL_G1,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						hidePDialog();
						Toast.makeText(context, "Última Atualizão: "+new Utils().getDate(), Toast.LENGTH_LONG).show();
						Document doc = Jsoup.parse(response);

						try {
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
							Log.e(TAG, "getResultados "+e.toString());
							Toast.makeText(context, "Não foi possível carregar os dados!", Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, error.toString());
						Toast.makeText(context, "A busca falhou. Tente novamente!", Toast.LENGTH_SHORT).show();
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
		proximo_concurso_megasena = (TextView) view.findViewById(R.id.proximo_concurso_megasena);
		proximo_concurso_duplasena = (TextView) view.findViewById(R.id.proximo_concurso_duplasena);
		proximo_concurso_lotofacil = (TextView) view.findViewById(R.id.proximo_concurso_lotofacil);
		proximo_concurso_quina = (TextView) view.findViewById(R.id.proximo_concurso_quina);
		proximo_concurso_timemania = (TextView) view.findViewById(R.id.proximo_concurso_timemania);
	}
}
