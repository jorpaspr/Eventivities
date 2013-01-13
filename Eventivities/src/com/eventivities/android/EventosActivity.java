package com.eventivities.android;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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
	//Vimop
	private LocationListener miLocationListener;
	private String longitudDestino;
	private String latitudDestino;
	//FinVimop
	private ImageButton btnRutas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setHomeButtonEnabled(true);
		btnRutas = (ImageButton) findViewById(R.id.imageButtonRuta);
        Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			localId = extras.getInt(Param.LOCAL_ID.toString());
			nombreLocal=extras.getString(Param.LOCAL_NOMBRE.toString());
			setTitle(nombreLocal);
			//Vimop
			longitudDestino= extras.getString("LONGITUD");
			latitudDestino= extras.getString("LATITUD");
			//FinVimop
			//ORIGINAL setTitle(extras.getString(Param.LOCAL_NOMBRE.toString()));
		}
		
		btnRutas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rutaMaps();			
			}
		});
		
		new EventosAsyncTask().execute();
    }
	
	@Override
	protected void onResume(){
		supportInvalidateOptionsMenu();
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
	
	
	private void rutaMaps(){
		
		
	LocationManager milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		 
   	 if (milocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			miLocationListener = new MiLocationListener();
			milocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, miLocationListener);
						
		}
   	 else if (milocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			miLocationListener = new MiLocationListener();
			milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, miLocationListener);
						
	}
	else{
				
			AlertDialog.Builder dialogoGps = new AlertDialog.Builder(this);  
	        dialogoGps.setTitle(R.string.mensaje_dialogo_gps);  
	        dialogoGps.setMessage(R.string.mensaje_dialogo_gps);            
	        dialogoGps.setCancelable(false); 
	        dialogoGps.setIcon(R.drawable.icongps); 
	        
	        dialogoGps.setPositiveButton(R.string.mensaje_dialogo_gps_btn_aceptar, new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialogoGps, int id) {  
	            	Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	 				startActivity(intent);
	            }  
	        });  
	        dialogoGps.setNegativeButton(R.string.mensaje_dialogo_gps_btn_cancelar, new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialogoGps, int id) {  
	                dialogoGps.dismiss();
	            }  
	        });            
	        dialogoGps.show();        
	    }//del else
	}
   	   	 
     
 private  class MiLocationListener implements LocationListener{
    			
			/**
			 * Método que se encarga de obtener las coordenadas gps y llamar
			 * al servicio de google para dibujar la ruta
			 *  
			 *  @author vimopre 
			 *  @param loc donde se encuentran las coordendas gps
			 */
			
	        public void onLocationChanged(Location loc){

	        /*
	         * Para obtener la ruta a pie, pasado por la web de google no hace falta formatear
	         * loc.getLXXX() * 1E6;  <- no hace falta.
	         */
	        	
	    	 double latActual = loc.getLatitude();
	    	 double lonActual = loc.getLongitude();
				
			 String uri = "http://maps.google.com/maps?saddr="+latActual+","+lonActual+"&daddr="+latitudDestino+","+longitudDestino;
	    	 Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
	    	 intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
	    	 startActivity(intent);

	        }
	        public void onProviderDisabled(String provider){
	        
	        }
	        public void onProviderEnabled(String provider){
	        
	        }
	        public void onStatusChanged(String provider, int status, Bundle extras){}
	    }
	 
	 //FinVimop
    
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
