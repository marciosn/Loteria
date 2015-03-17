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

public class TimeManiaFragment extends Fragment {

	private Context context;
	public static final String TAG = TimeManiaFragment.class.getName();
	public static final String URL_G1 = "http://g1.globo.com/loterias/timemania.html";
	public static final String TOAST_MESSAGE = "Não foi possível buscar os resultados!";
	private ProgressDialog pDialog;
	private RequestQueue rq;

	private TextView numero_concurso_TV;
	private TextView resultado_concurso_TV;
	private TextView resultado_timeDoCoracao_TV;

	private TextView ganhadores_acertos7_TV;
	private TextView ganhadores_acertos6_TV;
	private TextView ganhadores_acertos5_TV;
	private TextView ganhadores_acertos4_TV;
	private TextView ganhadores_acertos3_TV;
	private TextView ganhadores_timeDoCoracao_TV;

	private TextView rateio_acertos7_TV;
	private TextView rateio_acertos6_TV;
	private TextView rateio_acertos5_TV;
	private TextView rateio_acertos4_TV;
	private TextView rateio_acertos3_TV;
	private TextView rateio_timeDoCoracao_TV;
	
	private RelativeLayout relativeLayout;
	private LinearLayout linearLayout;
	private TextView acumulou_TV;

	private ImageButton btn_verResultadosTimeMania;
	private Utils utils;
	public TimeManiaFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_time_mania, container,
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
						relativeLayout.setVisibility(View.VISIBLE);
						hidePDialog();
						if(linearLayout.getVisibility() == View.VISIBLE){
							linearLayout.setVisibility(View.GONE);
						}

						try {
							Document doc = Jsoup.parse(response);
							
							numero_concurso_TV.setText(doc.getElementsByClass(
									"numero-concurso").text()
									+ " - "
									+ doc.getElementsByClass("data-concurso")
											.text());
							resultado_concurso_TV.setText(doc.getElementsByClass(
									"resultado-concurso").text());
							resultado_timeDoCoracao_TV
									.setText(doc.getElementsByClass(
											"resultado-time-do-coracao").text());

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
									ganhadores_acertos7_TV.setText(cols.get(1)
											.text());
									rateio_acertos7_TV.setText(cols.get(2).text());
								}
								if (i == 1) {
									ganhadores_acertos6_TV.setText(cols.get(1)
											.text());
									rateio_acertos6_TV.setText(cols.get(2).text());
								}
								if (i == 2) {
									ganhadores_acertos5_TV.setText(cols.get(1)
											.text());
									rateio_acertos5_TV.setText(cols.get(2).text());
								}
								if (i == 3) {
									ganhadores_acertos4_TV.setText(cols.get(1)
											.text());
									rateio_acertos4_TV.setText(cols.get(2).text());
								}
								if (i == 4) {
									ganhadores_acertos3_TV.setText(cols.get(1)
											.text());
									rateio_acertos3_TV.setText(cols.get(2).text());
								}
								if (i == 5) {
									ganhadores_timeDoCoracao_TV.setText(cols.get(1)
											.text());
									rateio_timeDoCoracao_TV.setText(cols.get(2)
											.text());
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
		
		relativeLayout = (RelativeLayout) view.findViewById(R.id.Layout_timemania);
		linearLayout = (LinearLayout) view.findViewById(R.id.error_timemania);
		
		resultado_concurso_TV = (TextView) view
				.findViewById(R.id.resultado_concurso_timemania);
		numero_concurso_TV = (TextView) view
				.findViewById(R.id.numero_concurso_timemania);
		resultado_timeDoCoracao_TV = (TextView) view
				.findViewById(R.id.resultado_time_do_coracao);

		ganhadores_acertos7_TV = (TextView) view
				.findViewById(R.id.ganhadores_7_acertos);
		ganhadores_acertos6_TV = (TextView) view
				.findViewById(R.id.ganhadores_6_acertos);
		ganhadores_acertos5_TV = (TextView) view
				.findViewById(R.id.ganhadores_5_acertos);
		ganhadores_acertos4_TV = (TextView) view
				.findViewById(R.id.ganhadores_4_acertos);
		ganhadores_acertos3_TV = (TextView) view
				.findViewById(R.id.ganhadores_3_acertos);
		ganhadores_timeDoCoracao_TV = (TextView) view
				.findViewById(R.id.ganhadores_time_do_coracao);

		rateio_acertos7_TV = (TextView) view
				.findViewById(R.id.rateio_7_acertos);
		rateio_acertos6_TV = (TextView) view
				.findViewById(R.id.rateio_6_acertos);
		rateio_acertos5_TV = (TextView) view
				.findViewById(R.id.rateio_5_acertos);
		rateio_acertos4_TV = (TextView) view
				.findViewById(R.id.rateio_4_acertos);
		rateio_acertos3_TV = (TextView) view
				.findViewById(R.id.rateio_3_acertos);
		rateio_timeDoCoracao_TV = (TextView) view
				.findViewById(R.id.rateio_time_do_coracao);


		acumulou_TV = (TextView) view.findViewById(R.id.acumulou_timemania);

		btn_verResultadosTimeMania = (ImageButton) view
				.findViewById(R.id.VerResultadoTimeMania);
		
		linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doIfOnline();
				if(linearLayout.getVisibility() == View.VISIBLE){
					linearLayout.setVisibility(View.GONE);
				}
			}
		});

		btn_verResultadosTimeMania.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doIfOnline();
				if(linearLayout.getVisibility() == View.VISIBLE){
					linearLayout.setVisibility(View.GONE);
				}
			}
		});

	}

}
