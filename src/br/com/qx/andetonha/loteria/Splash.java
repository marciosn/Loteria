package br.com.qx.andetonha.loteria;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.com.qx.andetonha.loteria.utils.Utils;

public class Splash extends ActionBarActivity {
	private long ms = 0;
	private long splashTime = 1500;
	private boolean splashActive = true;
	private boolean paused=false;
	private TextView at;
	private Utils utils;
	private ProgressBar progressBar;
	private ImageButton imageButton;
	private TextView problema;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		utils = new Utils(Splash.this);
		
		try {
			at = (TextView) findViewById(R.id.at);
			Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Something in the way.ttf");
			at.setTypeface(font);
			progressBar = (ProgressBar) findViewById(R.id.progressBar1);
			imageButton = (ImageButton) findViewById(R.id.logoSplash);
			problema = (TextView) findViewById(R.id.problemaInternt);
		
		} catch (Exception e) {
			Log.e("SPLASH", e.toString());
			if(progressBar.getVisibility() == View.VISIBLE){
				progressBar.setVisibility(View.GONE);
			}
			if(problema.getVisibility() == View.GONE){
				problema.setVisibility(View.VISIBLE);
			}
		}
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(Splash.this, "Tentando.....", Toast.LENGTH_SHORT).show();
				startSplash();
			}
		});
		
		startSplash();
	}

	public void startSplash(){
		if(utils.isOnline()){
			Thread mythread = new Thread() {
				public void run() {
					try {
						while (splashActive && ms < splashTime) {
							if(!paused)
								ms=ms+100;
							sleep(100);
						}
					} catch(Exception e) {}
					finally {
						Intent intent = new Intent(Splash.this, Home.class);
						startActivity(intent);
						finish();
					}
				}
			};
			mythread.start();
		}else{
			if(progressBar.getVisibility() == View.VISIBLE){
				progressBar.setVisibility(View.GONE);
			}
			if(problema.getVisibility() == View.GONE){
				problema.setVisibility(View.VISIBLE);
			}
			Toast.makeText(Splash.this, "Sem conexão com a internet.", Toast.LENGTH_LONG).show();
		}
	}
}
