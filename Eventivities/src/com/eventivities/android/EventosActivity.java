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
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.eventivities.android.adapters.EventosAdapter;
import com.eventivities.android.domain.Evento;
import com.eventivities.android.excepciones.ExcepcionAplicacion;
import com.eventivities.android.servicioweb.Conexion;

public class EventosActivity extends SherlockActivity {
	
	private List<Evento> eventos = null;
	private int localId;
	private String nombreLocal ;  // se necesita para pasarle el nombre a votar

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setHomeButtonEnabled(true);
        
        Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			localId = extras.getInt(Param.LOCAL_ID.toString());
			nombreLocal=extras.getString(Param.LOCAL_NOMBRE.toString());
			setTitle(nombreLocal);
			//ORIGINAL setTitle(extras.getString(Param.LOCAL_NOMBRE.toString()));
		}
		
		new EventosAsyncTask().execute();
    }
	
	@Override
	protected void onResume(){
		invalidateOptionsMenu();
		super.onResume();
	}
	
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
		case android.R.id.home:
			startActivity(new Intent(EventosActivity.this, LocalesActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			break;
		case R.id.menu_login:
			startActivity(new Intent(EventosActivity.this, MiPerfilActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.menu_refresh:
			new EventosAsyncTask().execute();
			break;
		case R.id.menu_location:
			startActivity(new Intent(EventosActivity.this, UbicacionActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
    
    private OnItemClickListener itemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Evento evento = eventos.get(arg2);

			Intent i = new Intent(EventosActivity.this, DetalleEventoActivity.class);
			Bundle b = new Bundle();
			b.putSerializable(Param.EVENTO.toString(), evento); 
			b.putString(Param.LOCAL_NOMBRE.toString(), nombreLocal);
			i.putExtras(b);

			startActivity(i);
		}
	};
	
	private void abrirComentarios(View v){
		// de momento solo en el detalle de obra.
		
	}
	
	
	private class EventosAsyncTask extends AsyncTask<Void, Void, List<Evento>> {

		@Override
		protected void onPreExecute() {
			getSherlock().setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}

		@Override
		protected List<Evento> doInBackground(Void... params) {
			try {
				eventos = Conexion.obtenerEventosLocal(localId).getEventos();
			} catch (ExcepcionAplicacion e) {
				eventos = null;
				e.printStackTrace();
			}
			return eventos;
		}

		@Override
		protected void onPostExecute(List<Evento> result) {
			if (result != null) {
				setContentView(R.layout.activity_eventos);
				ListView listView = (ListView) findViewById(android.R.id.list);
				listView.setOnItemClickListener(itemClickListener);
				EventosAdapter adapter = new EventosAdapter(getApplicationContext(), R.layout.item_evento, eventos);
				listView.setAdapter(adapter);
			} else {
				setContentView(R.layout.error_conexion);
			}
			
			getSherlock().setProgressBarIndeterminateVisibility(false);
			
			super.onPostExecute(result);
		}
		
	}
}
