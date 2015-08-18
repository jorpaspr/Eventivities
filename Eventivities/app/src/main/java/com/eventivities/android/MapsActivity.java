package com.eventivities.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.eventivities.android.domain.Local;
import com.eventivities.android.excepciones.ExcepcionAplicacion;
import com.eventivities.android.servicioweb.Conexion;
import com.eventivities.android.util.ParticularItemizedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Clase que se encarga de  crear el mapa, conectar con el gps, obtener la coordenadas, formatearlas
 * y dibujar en el mapa la posici�n
 *  
 * 
 *  @author vimopre
 * 
 */

public class MapsActivity extends /*MapActivity*/SherlockMapActivity{
	
	private MapView mapView;
    private MapController mc;
    private LocationListener miLocationListener;
    private ParticularItemizedOverlay itemizedoverlay;
    private ParticularItemizedOverlay itemizedoverlayLocales;
    private Geocoder geoCoder;
    private GeoPoint pointCentrar;
    private List<Local> locales;
    private List<Local> localesM;
    private List<Local> localesB;
    private LocationManager milocManager;
    
    public void onCreate(Bundle savedInstanceState){
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa); 
		getSupportActionBar().setHomeButtonEnabled(true);
        pointCentrar = null;
        
        //control del proveedor GPS
    	
    	 milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	 
    	 if (milocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
 			miLocationListener = new MiLocationListener();
 			milocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 500, miLocationListener);
 						
 		}
    	 else if (milocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
 			miLocationListener = new MiLocationListener();
 			milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 500, miLocationListener);
 						
 		}
 		else{
 				
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
 	    	 
    	 mapView = (MapView) findViewById(R.id.mapa);
         mc = mapView.getController();
         mapView.setBuiltInZoomControls(true);
         
         Drawable drawable = this.getResources().getDrawable(R.drawable.marcador_google_maps);
         itemizedoverlay = new ParticularItemizedOverlay(drawable,this);
         
         Drawable drawableMuseos = this.getResources().getDrawable(R.drawable.icono_museo); 
         itemizedoverlayLocales = new ParticularItemizedOverlay(drawableMuseos,this);
         
         
    }


	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	/**
	 * Clase que se encarga hacer la conexion con el gps y escucha las actualizciones de poscici�n
	 * 
	 *  @author vimopre
	 */
	
	private class MiLocationListener implements LocationListener{
		
		/**
		 * m�todo que escucha el GPS
		 * 
		 *  @author vimopre
		 *  @param loc variable done la clase deja las coordenadas gps
		 */
		
        public void onLocationChanged(Location loc){
        	double lat = loc.getLatitude() * 1E6;
			double lon = loc.getLongitude() * 1E6;

		

			//creamos un geoPunto necesario para dibujar en el mapa
			GeoPoint point = new GeoPoint((int) (lat),(int) (lon));
			GeoPoint pointDir = new GeoPoint((int) (lat),(int) (lon));
			mc.animateTo(point);
			mc.setZoom(17);
			String address="";
			
			GeocoderLocal (pointDir);
			
			
			
			List<Overlay> mapOverlays = mapView.getOverlays();//a�adimos la nueva capa
			OverlayItem overlayitem = new OverlayItem(point, "", address);
			itemizedoverlay.addOverlay(overlayitem);
			 
			mapOverlays.add(itemizedoverlay);

			mapView.postInvalidate();//actualizamos la capa y el mapa
			
			new LocalesAsyncTask().execute();
			
        }
        
        private class LocalesAsyncTask extends AsyncTask<Void, Void, List<Local>> {

        	protected void onPreExecute() {
    			getSherlock().setProgressBarIndeterminateVisibility(true);
    			super.onPreExecute();
    		}

        	
			protected List<Local> doInBackground(Void... arg0) {
				locales = null;
				try {
					SharedPreferences prefs = getSharedPreferences("UbicacionPreferences", Context.MODE_PRIVATE);
					int indice = prefs.getInt("ubicacionActual", UbicacionActivity.VALENCIA);
					String [] ciudades = getResources().getStringArray(R.array.ciudades);
					String ciudad = ciudades[indice];
					locales = Conexion.obtenerLocalesCiudad(ciudad).getLocales();
				} catch (ExcepcionAplicacion e) {
					e.printStackTrace();
				}
				return locales;
			}
			
			protected void onPostExecute(List<Local> result) {
				if(result != null){
					Iterator<Local> i = result.iterator();
					
					List<Overlay> mapOverlays = mapView.getOverlays();//a�adimos la nueva capa
					
					
					while (i.hasNext()){
						
						Local loc = i.next();
						
						double lat = Double.valueOf(loc.getLatitud()) * 1E6;
						double lon = Double.valueOf(loc.getLongitud())* 1E6;
						GeoPoint point = new GeoPoint((int) (lat),(int) (lon));
						
						OverlayItem overlayitem = new OverlayItem(point, "", null);//creamos el punto
						itemizedoverlayLocales.addOverlayEventos(overlayitem, loc);//a�adimos puntos a la capa
					}
					mapOverlays.add(itemizedoverlayLocales);//a�adimos la capa
    				mapView.postInvalidate();//redibujamos la capa
					
				}
				else{
					AlertDialog.Builder dialogoErrorConexion = new AlertDialog.Builder(getBaseContext());
					dialogoErrorConexion.setTitle(R.string.dialogoErrorConexion_error);
					dialogoErrorConexion.setMessage(R.string.dialogoErrorConexion_mensaje);
					dialogoErrorConexion.setCancelable(false);
				    
					dialogoErrorConexion.setNegativeButton(R.string.error_conexion, new DialogInterface.OnClickListener() {  
		 	            public void onClick(DialogInterface dialogoGps, int id) {  
		 	                dialogoGps.dismiss();
		 	            }  
		 	        });     
				}
				
				getSherlock().setProgressBarIndeterminateVisibility(false);
				super.onPostExecute(result);	
			}
        	
        }//de Asyntasck
        public void onProviderDisabled(String provider){
        
        }
        public void onProviderEnabled(String provider){

        }
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }
	
	/**
	 * Metodo que se encarga de localizar la direcci�n de la posici�n del usuario                                                      
	 * 
	 *  @author vimopre
	 *  @param GeoPoint pointDir
	 *  @return void
	 */
	
	private void GeocoderLocal (GeoPoint pointDir){
		if (pointDir != null){
		
		Geocoder geoCoder = new Geocoder(
                getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(
                	pointDir.getLatitudeE6()  / 1E6, 
                	pointDir.getLongitudeE6() / 1E6, 1);
                
                
                
                String add = "";
                if (addresses.size() > 0) 
                {
                    for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
                         i++)
                       add += addresses.get(0).getAddressLine(i) + "\n";
                }

                Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
               
            }
            catch (IOException e) {                
                e.printStackTrace();
            }
	}
	else
		Toast.makeText(getBaseContext(),"No hay direcci�n cargada",Toast.LENGTH_LONG).show();
	}


	@Override
	protected void onPause() {
		if (miLocationListener == null){
			super.onPause();
		}
		else{
				super.onPause();
				milocManager.removeUpdates(miLocationListener);
		}
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
			startActivity(new Intent(MapsActivity.this, LocalesActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			break;
		case R.id.menu_login:
			startActivity(new Intent(MapsActivity.this, MiPerfilActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.menu_location:
			startActivity(new Intent(MapsActivity.this, UbicacionActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}
