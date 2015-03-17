package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.qx.andetonha.loteria.R;
import br.com.qx.andetonha.loteria.utils.Utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DuplaSenaFragment extends Fragment {
	private Context context;
	public static final String TAG = QuinaFragment.class.getName();
	public static final String URL_G1 = "http://g1.globo.com/loterias/duplasena.html";
	public static final String TOAST_MESSAGE = "Não foi possível buscar os resultados!";
	private ProgressDialog pDialog;
	private RequestQueue rq;
	
	private RelativeLayout relativeLayout;
	private LinearLayout linearLayout;

	private TextView numero_concurso_TV;
	private TextView resultado_concurso_TV;

	private TextView ganhadores_sena1_TV;
	private TextView ganhadores_quina1_TV;
	private TextView ganhadores_quadra1_TV;
	private TextView ganhadores_sena2_TV;
	private TextView ganhadores_quina2_TV;
	private TextView ganhadores_quadra2_TV;

	private TextView rateio_sena1_TV;
	private TextView rateio_quina1_TV;
	private TextView rateio_quadra1_TV;

	private TextView rateio_sena2_TV;
	private TextView rateio_quina2_TV;
	private TextView rateio_quadra2_TV;

	private TextView acumulado_TV;

	private ImageButton btn_verResultadosDuplaSena;
	private Utils utils;

	public DuplaSenaFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dupla_sena, container,
				false);

		context = getActivity();
		rq = Volley.newRequestQueue(context);
		utils = new Utils(getActivity());
		
		try {
			utils.setStyleActionBar(getResources(), getActivity());
			carregarTextView(view);
			relativeLayout.setVisibility(View.GONE);
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
						relativeLayout.setVisibility(View.VISIBLE);
						if(linearLayout.getVisibility() == View.VISIBLE){
							linearLayout.setVisibility(View.GONE);
						}
						hidePDialog();

						try {
							
							Document doc = Jsoup.parse(response);
							
							numero_concurso_TV.setText(doc.getElementsByClass(
									"numero-concurso").text()
									+ " - "
									+ doc.getElementsByClass("data-concurso")
											.text());
							resultado_concurso_TV.setText("1º Sorteio - "
									+ doc.getElementsByClass("resultado-concurso")
											.get(0).text()
									+ "\n"
									+ "2º Sorteio - "
									+ doc.getElementsByClass("resultado-concurso")
											.get(1).text());

							String valor_acumulado = doc
									.getElementsByClass("valor-acumulado").get(0)
									.text();
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
									ganhadores_sena1_TV.setText(cols.get(1).text());
									rateio_sena1_TV.setText(cols.get(2).text());
								}
								if (i == 1) {
									ganhadores_quina1_TV
											.setText(cols.get(1).text());
									rateio_quina1_TV.setText(cols.get(2).text());
								} else if (i == 2) {
									ganhadores_quadra1_TV.setText(cols.get(1)
											.text());
									rateio_quadra1_TV.setText(cols.get(2).text());
								}
							}

							Element table2 = doc.select("table").get(1);
							Elements rows2 = table2.select("tr");
							int j;
							for (j = 0; j < rows2.size(); j++) {
								Element row = rows2.get(j);
								Elements cols = row.select("td");

								if (j == 0) {
									ganhadores_sena2_TV.setText(cols.get(1).text());
									rateio_sena2_TV.setText(cols.get(2).text());
								}
								if (j == 1) {
									ganhadores_quina2_TV
											.setText(cols.get(1).text());
									rateio_quina2_TV.setText(cols.get(2).text());
								} else if (j == 2) {
									ganhadores_quadra2_TV.setText(cols.get(1)
											.text());
									rateio_quadra2_TV.setText(cols.get(2).text());
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
		
		relativeLayout = (RelativeLayout) view.findViewById(R.id.Layout_duplasena);
		linearLayout = (LinearLayout) view.findViewById(R.id.errorDuplaSena);

		resultado_concurso_TV = (TextView) view.findViewById(R.id.resultadoConcursoDuplaSena);
		numero_concurso_TV = (TextView) view.findViewById(R.id.numero_concursoDuplaSena);

		ganhadores_sena1_TV = (TextView) view.findViewById(R.id.ganhadores_sena_duplasena);
		ganhadores_quina1_TV = (TextView) view.findViewById(R.id.ganhadores_quina_duplasena);
		ganhadores_quadra1_TV = (TextView) view.findViewById(R.id.ganhadores_quadra_duplasena);

		ganhadores_sena2_TV = (TextView) view.findViewById(R.id.ganhadores_sena2_duplasena);
		ganhadores_quina2_TV = (TextView) view.findViewById(R.id.ganhadores_quina2_duplasena);
		ganhadores_quadra2_TV = (TextView) view.findViewById(R.id.ganhadores_quadra2_duplasena);

		rateio_sena1_TV = (TextView) view.findViewById(R.id.rateio_sena_duplasena);
		rateio_quina1_TV = (TextView) view.findViewById(R.id.rateio_quina_duplasena);
		rateio_quadra1_TV = (TextView) view.findViewById(R.id.rateio_quadra_duplasena);
		
		rateio_sena2_TV = (TextView) view.findViewById(R.id.rateio_sena2_duplasena);
		rateio_quina2_TV = (TextView) view.findViewById(R.id.rateio_quina2_duplasena);
		rateio_quadra2_TV = (TextView) view.findViewById(R.id.rateio_quadra2_duplasena);

		acumulado_TV = (TextView) view.findViewById(R.id.acumulado_duplasena);	

		btn_verResultadosDuplaSena = (ImageButton) view.findViewById(R.id.VerResultadoDuplaSena);
		
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(linearLayout.getVisibility() == View.VISIBLE){
					linearLayout.setVisibility(View.GONE);
				}
				doIfOnline();
			}
		});

		btn_verResultadosDuplaSena.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(linearLayout.getVisibility() == View.VISIBLE){
					linearLayout.setVisibility(View.GONE);
				}
				doIfOnline();
			}
		});
	}

}
