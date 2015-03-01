package br.com.qx.andetonha.loteria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import br.com.qx.andetonha.loteria.fragments.TelaInicial;

public class Splash extends ActionBarActivity {
	private long ms = 0;
	private long splashTime = 3000;
	private boolean splashActive = true;
	private boolean paused=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
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
