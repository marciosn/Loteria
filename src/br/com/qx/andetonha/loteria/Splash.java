package br.com.qx.andetonha.loteria;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import br.com.qx.andetonha.loteria.fragments.TelaInicial;

public class Splash extends ActionBarActivity {
	private long ms = 0;
	private long splashTime = 2500;
	private boolean splashActive = true;
	private boolean paused=false;
	private TextView at;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		try {
			at = (TextView) findViewById(R.id.at);
			Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Something in the way.ttf");
			at.setTypeface(font);
		} catch (Exception e) {
			Log.e("SPLASH", e.toString());
		}
		
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
	}

}
