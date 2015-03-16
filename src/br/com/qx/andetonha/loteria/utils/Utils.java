package br.com.qx.andetonha.loteria.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.TextView;
import br.com.qx.andetonha.loteria.R;

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
	
	public void setStyleActionBar(Resources resources, Activity activity){
		int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView actionBarTitleView = (TextView) activity.getWindow().findViewById(actionBarTitle);  
		Typeface fonte = Typeface.createFromAsset(activity.getAssets(), "fonts/UPBOLTERS.otf");
		
		if(actionBarTitleView != null){
			actionBarTitleView.setTypeface(fonte);
			actionBarTitleView.setTextSize(25);
			actionBarTitleView.setTextColor(resources.getColor(R.color.white));
			actionBarTitleView.setGravity(Gravity.CENTER_HORIZONTAL);
		}
	}
}
