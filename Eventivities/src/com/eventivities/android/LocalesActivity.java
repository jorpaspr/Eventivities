package com.eventivities.android;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.eventivities.android.adapters.LocalesAdapter;
import com.eventivities.android.domain.Local;
import com.eventivities.android.excepciones.ExcepcionAplicacion;
import com.eventivities.android.servicioweb.Conexion;

public class LocalesActivity extends SherlockActivity {

	private List<Local> locales = null;
	private String ciudad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}
    

    private OnItemClickListener itemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Local local = locales.get(arg2);

			Intent i = new Intent(LocalesActivity.this, EventosActivity.class);
			Bundle b = new Bundle();
			b.putInt(Param.LOCAL_ID.toString(), local.getIdLocal());
			b.putString(Param.LOCAL_NOMBRE.toString(), local.getNombreLocal());
			i.putExtras(b);

			startActivity(i);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.general, menu);
		menu.findItem(R.id.menu_refresh).setVisible(true);
		SharedPreferences prefs = getSharedPreferences("LogInPreferences", Context.MODE_PRIVATE);
		boolean login = prefs.getBoolean("logIn", false);
		if(login)
			menu.findItem(R.id.menu_login).setTitle(prefs.getString("usuarioActual", getString(R.string.menu_login).toUpperCase()));
		else 
			menu.findItem(R.id.menu_login).setTitle(getString(R.string.menu_login));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_login:
			startActivity(new Intent(LocalesActivity.this, MiPerfilActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.menu_refresh:
			new LocalesAsyncTask().execute();
			break;
		case R.id.menu_location:
			startActivity(new Intent(LocalesActivity.this, UbicacionActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private class LocalesAsyncTask extends AsyncTask<Void, Void, List<Local>> {

		@Override
		protected void onPreExecute() {
			getSherlock().setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}

		@Override
		protected List<Local> doInBackground(Void... params) {
			try {				
				locales = Conexion.obtenerLocalesCiudad(ciudad).getLocales();
			} catch (ExcepcionAplicacion e) {
				locales = null;
				e.printStackTrace();
			}
			return locales;
		}

		@Override
		protected void onPostExecute(List<Local> result) {
			if (result != null) {
				setContentView(R.layout.activity_locales);		        
		        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
				ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(LocalesActivity.this,
						R.array.tipo_local, android.R.layout.simple_spinner_item);
				arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(arrayAdapter);
				
				GridView gridView = (GridView) findViewById(R.id.GridViewLocales);
				LocalesAdapter adapter = new LocalesAdapter(getApplicationContext(), R.layout.item_local, result);
				gridView.setAdapter(adapter);
				gridView.setOnItemClickListener(itemClickListener);
			} else {
		        setContentView(R.layout.error_conexion);
			}
			
			getSherlock().setProgressBarIndeterminateVisibility(false);
			
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onResume() {
		SharedPreferences prefs = getSharedPreferences("UbicacionPreferences", Context.MODE_PRIVATE);
		int indice = prefs.getInt("ubicacionActual", UbicacionActivity.VALENCIA);
		String [] ciudades = getResources().getStringArray(R.array.ciudades);
		String ciudadPreferencias = ciudades[indice];
		
		boolean ciudadCambiada = !ciudadPreferencias.equals(ciudad);		
		ciudad = ciudadPreferencias;
		
		if (ciudadCambiada)			
			new LocalesAsyncTask().execute();
		
		setTitle(ciudad);
		
		invalidateOptionsMenu();
		
		super.onResume();
	}
	
}
