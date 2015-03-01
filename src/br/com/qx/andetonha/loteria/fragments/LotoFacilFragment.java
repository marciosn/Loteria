package br.com.qx.andetonha.loteria.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
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

public class LotoFacilFragment extends Fragment{
	
	private Context context;
	public static final String TAG = "GERADOR NUMEROS";
	public static final String URL_G1 = "http://g1.globo.com/loterias/lotofacil.html";
	private ProgressDialog pDialog;
	private RequestQueue rq;
	
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
	
	private Button btn_verResultadosLotoFacil;
	
	public LotoFacilFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_loto_facil, container, false);
		
		context = getActivity();
		rq = Volley.newRequestQueue(context);
		
		resultado_concurso_TV = (TextView) view.findViewById(R.id.resultado_concurso_LotoFacil);
		numero_concurso_TV = (TextView) view.findViewById(R.id.numero_concurso_LotoFacil);
		
		ganhadores_acertos15_TV = (TextView) view.findViewById(R.id.ganhadores_acertos15_lotofacil);
		ganhadores_acertos14_TV = (TextView) view.findViewById(R.id.ganhadores_acertos14_lotofacil);
		ganhadores_acertos13_TV = (TextView) view.findViewById(R.id.ganhadores_acertos13_lotofacil);
		ganhadores_acertos12_TV = (TextView) view.findViewById(R.id.ganhadores_acertos12_lotofacil);
		ganhadores_acertos11_TV = (TextView) view.findViewById(R.id.ganhadores_acertos11_lotofacil);
		
		rateio_acertos15_TV = (TextView) view.findViewById(R.id.rateio_acertos15_lotofacil);
		rateio_acertos14_TV = (TextView) view.findViewById(R.id.rateio_acertos14_lotofacil);
		rateio_acertos13_TV = (TextView) view.findViewById(R.id.rateio_acertos13_lotofacil);
		rateio_acertos12_TV = (TextView) view.findViewById(R.id.rateio_acertos12_lotofacil);
		rateio_acertos11_TV = (TextView) view.findViewById(R.id.rateio_acertos11_lotofacil);
		
		acumulou_TV = (TextView) view.findViewById(R.id.acumulou_lotofacil);
		
		btn_verResultadosLotoFacil = (Button) view.findViewById(R.id.VerResultadoLotoFacil);
		
		btn_verResultadosLotoFacil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getResultados(v);
			}
		});
		
		
		return view;
	}
	
	public void getResultados(View view){
		
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Buscando...");
		pDialog.show();
		
		StringRequest request = new StringRequest(Request.Method.GET, URL_G1,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						hidePDialog();
						
						Document doc = Jsoup.parse(response);
						
						numero_concurso_TV.setText(doc.getElementsByClass("numero-concurso").text() +" - " + doc.getElementsByClass("data-concurso").text());
						resultado_concurso_TV.setText(doc.getElementsByClass("resultado-concurso").text());
						
						String valor_acumulado = doc.getElementsByClass("valor-acumulado").text();
						if(valor_acumulado.equalsIgnoreCase("R$ 0,00")){
							acumulou_TV.setText("");
						}else{
							acumulou_TV.setText("VALOR ACUMULADO: "+doc.getElementsByClass("valor-acumulado").text());
						}
						
						Element table = doc.select("table").get(0);
					    Elements rows = table.select("tr");
					    int i;
					    for (i = 0; i < rows.size(); i++) {
					        Element row = rows.get(i);
					        Elements cols = row.select("td");
					        
					        if(i == 0){
					        	ganhadores_acertos15_TV.setText(cols.get(1).text());
				        		rateio_acertos15_TV.setText(cols.get(2).text());
				        	}
				        	if(i == 1){
				        		ganhadores_acertos14_TV.setText(cols.get(1).text());
				        		rateio_acertos14_TV.setText(cols.get(2).text());
				        	}
				        	if(i == 2){
				        			ganhadores_acertos13_TV.setText(cols.get(1).text());
					        		rateio_acertos13_TV.setText(cols.get(2).text());
				        	}
				        	if(i == 3){
			        			ganhadores_acertos12_TV.setText(cols.get(1).text());
				        		rateio_acertos12_TV.setText(cols.get(2).text());
				        	}
				        	if(i == 4){
			        			ganhadores_acertos11_TV.setText(cols.get(1).text());
				        		rateio_acertos11_TV.setText(cols.get(2).text());
				        	}
					   }
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, error.toString());
						Toast.makeText(context,error.toString(), Toast.LENGTH_LONG).show();
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
