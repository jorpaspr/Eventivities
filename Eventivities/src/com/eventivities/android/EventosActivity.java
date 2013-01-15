package com.eventivities.android;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import com.eventivities.android.adapters.EventosAdapter;
import com.eventivities.android.domain.Evento;
import com.eventivities.android.excepciones.ExcepcionAplicacion;
import com.eventivities.android.servicioweb.Conexion;

public class EventosActivity extends BaseActivity {
	
	private List<Evento> eventos = null;
	private int localId;
	private String nombreLocal ; 
	private LocationListener miLocationListener;
	private LocationManager milocManager;
	private String longitudDestino;
	private String latitudDestino;
	private Button btnRutas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		
        Bundle extras = getIntent().getExtras();
        
		if(extras != null)
		{
			localId = extras.getInt(Param.LOCAL_ID.toString());
			nombreLocal=extras.getString(Param.LOCAL_NOMBRE.toString());
			setTitle(nombreLocal);
			longitudDestino= extras.getString("LONGITUD");
			latitudDestino= extras.getString("LATITUD");

		}
		
		new EventosAsyncTask().execute();
    }
	
	@Override
	protected void onResume(){
		super.onResume();
		if (miLocationListener == null){
			super.onPause();
		}
		else{
			super.onPause();
			milocManager.removeUpdates(miLocationListener);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.menu_refresh).setVisible(true);
		return true;
	}
	
	@Override
	protected void refresh() {
		super.refresh();
		new EventosAsyncTask().execute();
	}

	private void rutaMaps(){
		
		milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			 
		if (milocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			miLocationListener = new MiLocationListener();
			milocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, miLocationListener);					
		}
	   	else if (milocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			miLocationListener = new MiLocationListener();
			milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, miLocationListener);
		}
		else
		{				
			AlertDialog.Builder dialogoGps = new AlertDialog.Builder(this);  
	        dialogoGps.setTitle(R.string.Titulo_dialogo_gps);  
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
		 * M�todo que se encarga de obtener las coordenadas gps y llamar
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
				btnRutas = (Button) findViewById(R.id.buttonRuta);
				btnRutas.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						rutaMaps();			
					}
				});
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
