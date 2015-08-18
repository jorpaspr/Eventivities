package com.eventivities.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventivities.android.R;
import com.eventivities.android.domain.Evento;
import com.eventivities.android.util.ImageAsyncHelper;
import com.eventivities.android.util.ImageAsyncHelper.ImageAsyncHelperCallBack;
import com.eventivities.android.util.ViewUtil;

import java.util.List;

public class EventosAdapter extends ArrayAdapter<Evento> {
	
	private int layoutResourceId;

	public EventosAdapter(Context context, int layoutResourceId, List<Evento> objects) {
		super(context, layoutResourceId, objects);
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
	        view = inflater.inflate(layoutResourceId, parent, false);
	        
		}	

		Evento evento = this.getItem(position);

		if (evento != null) {			
			TextView textViewNombre = (TextView)view.findViewById(R.id.textViewNombre);
			if (textViewNombre != null)
				textViewNombre.setText(evento.getNombre());
			
			TextView textViewFecha = (TextView)view.findViewById(R.id.textViewFecha);
			if (textViewFecha != null) {
				String formatoRango = this.getContext().getString(R.string.formato_rango_fecha);
				String rangoFecha = ViewUtil.rangoFecha(formatoRango, evento.getFechaInicio(), evento.getFechaFin());
				textViewFecha.setText(rangoFecha);
			}
			
			TextView textViewPrecio = (TextView)view.findViewById(R.id.textViewPrecio);
			if (textViewFecha != null) {
				String format = this.getContext().getString(R.string.formato_precio);
				textViewPrecio.setText(String.format(format, evento.getPrecio()));
			}
			
			TextView textViewPuntEvento = (TextView)view.findViewById(R.id.textViewPuntuacion);
			if (textViewPuntEvento != null) {
				textViewPuntEvento.setText(ViewUtil.obtenerEstrellas(evento.getMedia()));
			}
			
			final ImageView imageViewEvento = (ImageView)view.findViewById(R.id.imageViewProducto);
			
			if (imageViewEvento != null) {
				ImageAsyncHelper imageAsyncHelper = new ImageAsyncHelper();
				
				Bitmap img = imageAsyncHelper.getBitmap(evento.getNombreImg(),
						new ImageAsyncHelperCallBack() {
					
					@Override
					public void onImageSyn(Bitmap img) {
						imageViewEvento.setImageBitmap(img);
					}
				}, null);
				
				if (img != null)
					imageViewEvento.setImageBitmap(img);
			}
		}
		
		return view;
	}

}
