package br.com.qx.andetonha.loteria;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import br.com.qx.andetonha.loteria.fragments.DuplaSenaFragment;
import br.com.qx.andetonha.loteria.fragments.GerarApostasFragment;
import br.com.qx.andetonha.loteria.fragments.LotoFacilFragment;
import br.com.qx.andetonha.loteria.fragments.MegaSenaFragment;
import br.com.qx.andetonha.loteria.fragments.ProximosSorteiosFragment;
import br.com.qx.andetonha.loteria.fragments.QuinaFragment;
import br.com.qx.andetonha.loteria.fragments.SobreNos;
import br.com.qx.andetonha.loteria.fragments.TelaInicial;
import br.com.qx.andetonha.loteria.fragments.TimeManiaFragment;

public class Home extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	public static final String LOTERIA = "Loteria";
	public static final String GERAR_PALPITE = "Gerar Palpite";
	public static final String MEGA_SENA = "Mega Sena";
	public static final String QUINA = "Quina";
	public static final String LOTO_FACIL = "Loto Fácil";
	public static final String TIMEMANIA= "Timemania";
	public static final String DUPLA_SENA = "Dupla Sena";
	public static final String PROXIMOS_SORTEIOS = "Próximos Sorteios";
	public static final String SOBRE = "Sobre";
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private ActionBar actionBar;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		fragmentManager = getSupportFragmentManager();
		
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		
		switch (position) {
		case 0:
			fragment = new TelaInicial(fragmentManager, actionBar);
			actionBar.setTitle(LOTERIA);
			break;
		case 1:
			fragment = new GerarApostasFragment();
			actionBar.setTitle(GERAR_PALPITE);
			break;
		case 2:
			fragment = new MegaSenaFragment();
			actionBar.setTitle(MEGA_SENA);
			break;
		case 3:
			fragment = new QuinaFragment();
			actionBar.setTitle(QUINA);
			break;
		case 4:
			fragment = new LotoFacilFragment();
			actionBar.setTitle(LOTO_FACIL);
			break;
		case 5:
			fragment = new DuplaSenaFragment();
			actionBar.setTitle(DUPLA_SENA);
			break;
		case 6:
			fragment = new TimeManiaFragment();
			actionBar.setTitle(TIMEMANIA);
			break;
		case 7:
			fragment = new ProximosSorteiosFragment();
			actionBar.setTitle(PROXIMOS_SORTEIOS);
			break;
		}

		if (fragment != null) {
			fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
		} else {
			Log.e("ERROR FRAGMENT", "Desculpe, ocorreu um problema!");
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 0:
			mTitle = getString(R.string.title_section1);
			break;
		case 1:
			mTitle = getString(R.string.title_section2);
			break;
		case 2:
			mTitle = getString(R.string.title_section3);
			break;
		case 3:
			mTitle = getString(R.string.Row_Quina);
			break;
		case 4:
			mTitle = getString(R.string.Row_Loto_Facil);
			break;
		case 5:
			mTitle = getString(R.string.Row_Dupla_Sena);
			break;
		case 6:
			mTitle = getString(R.string.Row_Time_Mania);
			break;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		if (!mNavigationDrawerFragment.isDrawerOpen()) {

			getMenuInflater().inflate(R.menu.home, menu);
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		actionBar = getSupportActionBar();
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			try {
				fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container, new SobreNos()).commit();
				actionBar.setTitle(SOBRE);
			} catch (Exception e) {
				Log.e("HOME", "onOptionsItemSelected "+e.toString());
				Toast.makeText(this, "Ops!, temos um problema técnico!", Toast.LENGTH_LONG).show();
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
