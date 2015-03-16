package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class MegaSenaFragment extends Fragment {

	public static final String TAG = MegaSenaFragment.class.getSimpleName();
	public static final String URL_G1 = "http://g1.globo.com/loterias/megasena.html";
	public static final String TOAST_MESSAGE = "Não foi possível buscar os resultados!";
	private ProgressDialog pDialog;
	private RequestQueue rq;
	
	private RelativeLayout relativeLayout1;
	private RelativeLayout relativeLayout2;
	private LinearLayout error;

	private TextView numero_concurso_TV;
	private TextView resultado_concurso_TV;
	private TextView ganhadores_sena_TV;
	private TextView ganhadores_quina_TV;
	private TextView ganhadores_quadra_TV;
	private TextView rateio_sena_TV;
	private TextView rateio_quina_TV;
	private TextView rateio_quadra_TV;
	private TextView acumulado_TV;
	
	private ImageButton btn_verResultadosSena;

	private Context context;
	private ActionBar actionBar;
	private Utils utils;

	public MegaSenaFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_megasena, container,
				false);

		context = getActivity();
		rq = Volley.newRequestQueue(context);
		utils = new Utils(getActivity());
		
		try {
			utils.setStyleActionBar(getResources(), getActivity());
			carregarTextView(view);
			relativeLayout1.setVisibility(View.GONE);
			relativeLayout2.setVisibility(View.GONE);
			btn_verResultadosSena.setVisibility(View.GONE);
			doIfOnline();
		} catch (Exception e) {
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
						relativeLayout1.setVisibility(View.VISIBLE);
						relativeLayout2.setVisibility(View.VISIBLE);
						btn_verResultadosSena.setVisibility(View.VISIBLE);
						
						hidePDialog();
						
						try {							
							Document doc = Jsoup.parse(response);
							
							numero_concurso_TV.setText(doc.getElementsByClass(
									"numero-concurso").text()
									+ " - "
									+ doc.getElementsByClass("data-concurso")
											.text());
							resultado_concurso_TV.setText(doc.getElementsByClass("resultado-concurso").text());

							String valor_acumulado = doc.getElementsByClass(
									"valor-acumulado").text();
							if (valor_acumulado.equalsIgnoreCase("R$ 0,00")) {
								acumulado_TV.setText("");
							} else {
								acumulado_TV.setText("VALOR ACUMULADO: "
										+ doc.getElementsByClass("valor-acumulado")
												.text());
							}

							Element table = doc.select("table").get(0);
							Elements rows = table.select("tr");
							int i;
							for (i = 0; i < rows.size(); i++) {
								Element row = rows.get(i);
								Elements cols = row.select("td");

								if (i == 0) {
									//sena_TV.setText(cols.get(0).text());
									ganhadores_sena_TV.setText(cols.get(1).text());
									rateio_sena_TV.setText(cols.get(2).text());
								}
								if (i == 1) {
									//quina_TV.setText(cols.get(0).text());
									ganhadores_quina_TV.setText(cols.get(1).text());
									rateio_quina_TV.setText(cols.get(2).text());
								} else if (i == 2) {
									//quadra_TV.setText(cols.get(0).text());
									ganhadores_quadra_TV
											.setText(cols.get(1).text());
									rateio_quadra_TV.setText(cols.get(2).text());
								}
							}
						} catch (Exception e) {
							hidePDialog();
							error.setVisibility(View.VISIBLE);
							Log.e(TAG, e.toString());
							Toast.makeText(context, TOAST_MESSAGE, Toast.LENGTH_LONG).show();
						} catch (OutOfMemoryError e) {
							hidePDialog();
							error.setVisibility(View.VISIBLE);
							Log.e(TAG, e.toString());
							Toast.makeText(context, TOAST_MESSAGE, Toast.LENGTH_LONG).show();
						}
						

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						hidePDialog();
						error.setVisibility(View.VISIBLE);
						Log.e(TAG, volleyError.toString());
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
		
		relativeLayout1 = (RelativeLayout) view.findViewById(R.id.Layout_megasena);
		relativeLayout2 = (RelativeLayout) view.findViewById(R.id.Layout_Megasena2);
		error = (LinearLayout) view.findViewById(R.id.error_megasena);
		
		resultado_concurso_TV = (TextView) view.findViewById(R.id.resultado_concurso_megasena);
		numero_concurso_TV = (TextView) view.findViewById(R.id.numero_concurso);
		ganhadores_sena_TV = (TextView) view.findViewById(R.id.ganhadores_sena);
		ganhadores_quina_TV = (TextView) view
				.findViewById(R.id.ganhadores_quina);
		ganhadores_quadra_TV = (TextView) view
				.findViewById(R.id.ganhadores_quadra);
		rateio_sena_TV = (TextView) view.findViewById(R.id.rateio_sena);
		rateio_quina_TV = (TextView) view.findViewById(R.id.rateio_quina);
		rateio_quadra_TV = (TextView) view.findViewById(R.id.rateio_quadra);
		acumulado_TV = (TextView) view.findViewById(R.id.acumulado);

		btn_verResultadosSena = (ImageButton) view.findViewById(R.id.VerResultadoSena);

		btn_verResultadosSena.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doIfOnline();
			}
		});
		
		error.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doIfOnline();
			}
		});

	}
}
