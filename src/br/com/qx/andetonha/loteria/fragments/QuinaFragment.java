package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class QuinaFragment extends Fragment {
	private Context context;
	public static final String TAG = QuinaFragment.class.getName();
	public static final String URL_G1 = "http://g1.globo.com/loterias/quina.html";
	private ProgressDialog pDialog;
	private RequestQueue rq;

	private TextView numero_concurso_TV;
	private TextView resultado_concurso_TV;
	private TextView ganhadores_terno_TV;
	private TextView ganhadores_quina_TV;
	private TextView ganhadores_quadra_TV;
	private TextView rateio_terno_TV;
	private TextView rateio_quina_TV;
	private TextView rateio_quadra_TV;
	private TextView acumulado_TV;

	private Button btn_verResultadosSena;

	public QuinaFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_quina, container, false);

		context = getActivity();
		rq = Volley.newRequestQueue(context);

		resultado_concurso_TV = (TextView) view
				.findViewById(R.id.resultado_concurso_Quina);
		numero_concurso_TV = (TextView) view
				.findViewById(R.id.numero_concurso_Quina);
		ganhadores_quina_TV = (TextView) view
				.findViewById(R.id.ganhadores_quina_Quina);
		ganhadores_quadra_TV = (TextView) view
				.findViewById(R.id.ganhadores_quadra_Quina);
		ganhadores_terno_TV = (TextView) view
				.findViewById(R.id.ganhadores_terno_Quina);

		rateio_quina_TV = (TextView) view.findViewById(R.id.rateio_quina_Quina);
		rateio_quadra_TV = (TextView) view
				.findViewById(R.id.rateio_quadra_Quina);
		rateio_terno_TV = (TextView) view.findViewById(R.id.rateio_terno_Quina);
		acumulado_TV = (TextView) view.findViewById(R.id.acumulado_quina);

		btn_verResultadosSena = (Button) view
				.findViewById(R.id.VerResultadoQuina);

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
								ganhadores_quina_TV.setText(cols.get(1).text());
								rateio_quina_TV.setText(cols.get(2).text());
							}
							if (i == 1) {
								ganhadores_quadra_TV
										.setText(cols.get(1).text());
								rateio_quadra_TV.setText(cols.get(2).text());
							} else if (i == 2) {
								ganhadores_terno_TV.setText(cols.get(1).text());
								rateio_terno_TV.setText(cols.get(2).text());
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
