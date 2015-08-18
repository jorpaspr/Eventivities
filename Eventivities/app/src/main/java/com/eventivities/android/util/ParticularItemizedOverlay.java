package com.eventivities.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;

import com.eventivities.android.R;
import com.eventivities.android.domain.Local;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

/**
 * Clase que se encarga de manejar la inserción de puntos en el mapa
 * Controla las capas que en el mapa se dibujan, como los puntos y la información contenida en ellos
 * 
 *  @author vimopre
 *  @param defaultMarker el marcador del punto a insertar
 *  @param context el contexto
 *  @return no retorna nada, solo dibuja
 */

public class ParticularItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	 private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	 private ArrayList<Local> locales = new ArrayList<Local>();
	 private Context mContext;
	 
	 public ParticularItemizedOverlay(Drawable defaultMarker, Context context) {
		 super(boundCenterBottom(defaultMarker));
		 mContext = context;
	 }
 
	 public void addOverlay(OverlayItem overlay) {
		 mOverlays.clear();
		 mOverlays.add(overlay);
		 populate();
	 }
	 
	 public void addOverlayEventos(OverlayItem overlay, Local loc){
		 mOverlays.add(overlay);
		 locales.add(loc);
		 populate();
	 }
 
	 protected OverlayItem createItem(int i) {
		 return mOverlays.get(i);
	 }
 
	 public int size() {
		 return mOverlays.size();
	 }
 
	 protected boolean onTap(int index) {
		
		 Local item = locales.get(index);

         AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

         dialog.setTitle(item.getNombreLocal());
         dialog.setIcon(R.drawable.info); 
         dialog.setMessage("Dirección: "+item.getDireccion()+ "\n"+"Teléfono: "+item.getTelefono());
         dialog.setPositiveButton(R.string.dialogoInfo_local_YES,  new OnClickListener(){
        	 
        	 public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
        	 }
        	 
         });
         dialog.show();         
         return true;
	 }

	 
	 public boolean onTap(GeoPoint p, MapView mapView) {  
		 
		 if(!super.onTap(p, mapView))
			return true;
		 return true;
			  
			
		 
	 }
}
