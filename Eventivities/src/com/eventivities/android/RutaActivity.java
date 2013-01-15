package com.eventivities.android;



import com.eventivities.android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

/**
 * Clase que se encarga de  llamar al servicio de google para dibujar la ruta
 *  
 * 
 *  @author vimopre
 *  @param longActual y latActual
 *  
 */
public class RutaActivity extends Activity {
	
	private LocationListener miLocationListener;
	private double latActual;
	private double lonActual;
	private String latDestino;
	private String lonDestino;
	
	
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//forma de pasar parámetros al hacer un new activity
		Bundle bundle = getIntent().getExtras();
		latDestino = bundle.getString("LATITUD");
		lonDestino = bundle.getString("LONGITUD");
		
		
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

	
	private class MiLocationListener implements LocationListener{
		
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
        	
    	 latActual = loc.getLatitude();
		 lonActual = loc.getLongitude();
			
		 String uri = "http://maps.google.com/maps?saddr="+latActual+","+lonActual+"&daddr="+latDestino+","+lonDestino;
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
	


}// de la clase
