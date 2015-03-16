package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.android.volley.RetryPolicy;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LotoFacilFragment extends Fragment {

	private Context context;
	public static final String TAG = LotoFacilFragment.class.getSimpleName();
	public static final String URL_G1 = "http://g1.globo.com/loterias/lotofacil.html";
	public static final String TOAST_MESSAGE = "Não foi possível buscar os resultados!";
	private ProgressDialog pDialog;
	private RequestQueue rq;
	
	private RelativeLayout relativeLayout1;
	private RelativeLayout relativeLayout2;
	private LinearLayout linearLayout;

	private TextView numero_concurso_TV;
	private TextView resultado_concurso_TV;
	private TextView ganhadores_acertos15_TV;
	private TextView ganhadores_acertos14_TV;
	private TextView ganhadores_acertos13_TV;
	private TextView ganhadores_acertos12_TV;
	private TextView ganhadores_acertos11_TV;

	private TextView rateio_acertos15_TV;
	private TextView rateio_acertos14_TV;
	private TextView rateio_acertos13_TV;
	private TextView rateio_acertos12_TV;
	private TextView rateio_acertos11_TV;

	private TextView acumulou_TV;
	private Utils utils;

	private ImageButton btn_verResultadosLotoFacil;

	public LotoFacilFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_loto_facil, container,
				false);

		context = getActivity();
		rq = Volley.newRequestQueue(context);
		utils = new Utils(getActivity());
		try {
			utils.setStyleActionBar(getResources(), getActivity());
			carregarTextView(view);
			relativeLayout1.setVisibility(View.GONE);
			relativeLayout2.setVisibility(View.GONE);
			btn_verResultadosLotoFacil.setVisibility(View.GONE);
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
						relativeLayout1.setVisibility(View.VISIBLE);
						relativeLayout2.setVisibility(View.VISIBLE);
						btn_verResultadosLotoFacil.setVisibility(View.VISIBLE);
						hidePDialog();
						
						try {
							Document doc = Jsoup.parse(response);
							
							numero_concurso_TV.setText(doc.getElementsByClass(
									"numero-concurso").text()
									+ " - "
									+ doc.getElementsByClass("data-concurso")
											.text());
							resultado_concurso_TV.setText(doc.getElementsByClass(
									"resultado-concurso").text());

							String valor_acumulado = doc.getElementsByClass(
									"valor-acumulado").text();
							if (valor_acumulado.equalsIgnoreCase("R$ 0,00")) {
								acumulou_TV.setText("");
							} else {
								acumulou_TV.setText("VALOR ACUMULADO: "
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
									ganhadores_acertos15_TV.setText(cols.get(1)
											.text());
									rateio_acertos15_TV.setText(cols.get(2).text());
								}
								if (i == 1) {
									ganhadores_acertos14_TV.setText(cols.get(1)
											.text());
									rateio_acertos14_TV.setText(cols.get(2).text());
								}
								if (i == 2) {
									ganhadores_acertos13_TV.setText(cols.get(1)
											.text());
									rateio_acertos13_TV.setText(cols.get(2).text());
								}
								if (i == 3) {
									ganhadores_acertos12_TV.setText(cols.get(1)
											.text());
									rateio_acertos12_TV.setText(cols.get(2).text());
								}
								if (i == 4) {
									ganhadores_acertos11_TV.setText(cols.get(1)
											.text());
									rateio_acertos11_TV.setText(cols.get(2).text());
								}
							}
						} catch (Exception e) {
							hidePDialog();
							linearLayout.setVisibility(View.VISIBLE);
							Log.e(TAG, e.toString());
							Toast.makeText(context, TOAST_MESSAGE, Toast.LENGTH_LONG).show();
						} catch (OutOfMemoryError e) {
							hidePDialog();
							linearLayout.setVisibility(View.VISIBLE);
							Log.e(TAG, e.toString());
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
		
		relativeLayout1 = (RelativeLayout) view.findViewById(R.id.layout_lotofacil1);
		relativeLayout2 = (RelativeLayout) view.findViewById(R.id.layout_lotofacil);
		linearLayout = (LinearLayout) view.findViewById(R.id.error_lotofacil);
		
		resultado_concurso_TV = (TextView) view
				.findViewById(R.id.resultado_concurso_LotoFacil);
		numero_concurso_TV = (TextView) view
				.findViewById(R.id.numero_concurso_LotoFacil);

		ganhadores_acertos15_TV = (TextView) view
				.findViewById(R.id.ganhadores_acertos15_lotofacil);
		ganhadores_acertos14_TV = (TextView) view
				.findViewById(R.id.ganhadores_acertos14_lotofacil);
		ganhadores_acertos13_TV = (TextView) view
				.findViewById(R.id.ganhadores_acertos13_lotofacil);
		ganhadores_acertos12_TV = (TextView) view
				.findViewById(R.id.ganhadores_acertos12_lotofacil);
		ganhadores_acertos11_TV = (TextView) view
				.findViewById(R.id.ganhadores_acertos11_lotofacil);

		rateio_acertos15_TV = (TextView) view
				.findViewById(R.id.rateio_acertos15_lotofacil);
		rateio_acertos14_TV = (TextView) view
				.findViewById(R.id.rateio_acertos14_lotofacil);
		rateio_acertos13_TV = (TextView) view
				.findViewById(R.id.rateio_acertos13_lotofacil);
		rateio_acertos12_TV = (TextView) view
				.findViewById(R.id.rateio_acertos12_lotofacil);
		rateio_acertos11_TV = (TextView) view
				.findViewById(R.id.rateio_acertos11_lotofacil);

		acumulou_TV = (TextView) view.findViewById(R.id.acumulou_lotofacil);

		btn_verResultadosLotoFacil = (ImageButton) view.findViewById(R.id.VerResultadoLotoFacil);
		
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doIfOnline();
			}
		});

		btn_verResultadosLotoFacil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doIfOnline();
			}
		});

	}
}
