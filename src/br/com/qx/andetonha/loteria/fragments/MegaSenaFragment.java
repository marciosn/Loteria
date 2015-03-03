package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.com.qx.andetonha.loteria.R;

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

	public static final String TAG = "GERADOR NUMEROS";
	public static final String URL_G1 = "http://g1.globo.com/loterias/megasena.html";
	private ProgressDialog pDialog;
	private RequestQueue rq;

	private TextView numero_concurso_TV;
	private TextView resultado_concurso_TV;
	private TextView ganhadores_sena_TV;
	private TextView ganhadores_quina_TV;
	private TextView ganhadores_quadra_TV;
	private TextView rateio_sena_TV;
	private TextView rateio_quina_TV;
	private TextView rateio_quadra_TV;
	private TextView sena_TV;
	private TextView quina_TV;
	private TextView quadra_TV;
	private TextView acumulado_TV;

	private Button btn_verResultadosSena;

	private Context context;

	public MegaSenaFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_megasena, container,
				false);

		context = getActivity();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rq = Volley.newRequestQueue(context);

		resultado_concurso_TV = (TextView) view
				.findViewById(R.id.resultado_concurso);
		numero_concurso_TV = (TextView) view.findViewById(R.id.numero_concurso);
		ganhadores_sena_TV = (TextView) view.findViewById(R.id.ganhadores_sena);
		ganhadores_quina_TV = (TextView) view
				.findViewById(R.id.ganhadores_quina);
		ganhadores_quadra_TV = (TextView) view
				.findViewById(R.id.ganhadores_quadra);
		rateio_sena_TV = (TextView) view.findViewById(R.id.rateio_sena);
		rateio_quina_TV = (TextView) view.findViewById(R.id.rateio_quina);
		rateio_quadra_TV = (TextView) view.findViewById(R.id.rateio_quadra);
		sena_TV = (TextView) view.findViewById(R.id.sena);
		quina_TV = (TextView) view.findViewById(R.id.quina);
		quadra_TV = (TextView) view.findViewById(R.id.quadra);
		acumulado_TV = (TextView) view.findViewById(R.id.acumulado);

		btn_verResultadosSena = (Button) view
				.findViewById(R.id.VerResultadoSena);

		btn_verResultadosSena.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOnline()) {
					getResultados(v);
				} else {
					Toast.makeText(context, "Sem conexão com a internet.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		return view;
	}

	public boolean isOnline() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public void getResultados(View view) {

		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Buscando...");
		pDialog.show();

		StringRequest request = new StringRequest(Request.Method.GET, URL_G1,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						hidePDialog();

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
								sena_TV.setText(cols.get(0).text());
								ganhadores_sena_TV.setText(cols.get(1).text());
								rateio_sena_TV.setText(cols.get(2).text());
							}
							if (i == 1) {
								quina_TV.setText(cols.get(0).text());
								ganhadores_quina_TV.setText(cols.get(1).text());
								rateio_quina_TV.setText(cols.get(2).text());
							} else if (i == 2) {
								quadra_TV.setText(cols.get(0).text());
								ganhadores_quadra_TV
										.setText(cols.get(1).text());
								rateio_quadra_TV.setText(cols.get(2).text());
							}
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, error.toString());
						Toast.makeText(context, error.toString(),
								Toast.LENGTH_LONG).show();
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

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
}
