package br.com.qx.andetonha.loteria.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
	private Context context;
	
	public Utils(Context context) {
		this.context = context;
	}
	
	public Utils() {
	}
	
	public boolean isOnline() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public String getDate(){
		String ultima_atualizacao = new SimpleDateFormat("HH:mm:ss").format(new Date());
		return ultima_atualizacao;
	}
}
