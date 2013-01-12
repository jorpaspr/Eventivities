package com.eventivities.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eventivities.android.R;
import com.eventivities.android.domain.Comentario;
import com.eventivities.android.domain.Evento;
import com.eventivities.android.util.TnUtil;
import com.eventivities.android.util.ViewUtil;

public class ComentariosAdapter extends ArrayAdapter<Comentario> {
	
	private int layoutResourceId;

	public ComentariosAdapter(Context context, int layoutResourceId, List<Comentario> objects) {
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

		Comentario comentario= this.getItem(position);

		if (comentario != null) {			
			TextView tComentario = (TextView)view.findViewById(R.id.comentario_item_comentario);
			String tDato=String.valueOf(comentario.getComentario());
			if (tDato != null)
				tComentario.setText(tDato);
			else
				tComentario.setText("¿ no hay comentario ?");
				
			TextView tUser= (TextView)view.findViewById(R.id.comentario_item_usuario);
			tDato=String.valueOf(comentario.getAlias());
			if (tDato != null)
				tUser.setText(tDato);
			else
				tUser.setText("-");
			
			TextView textViewFecha = (TextView)view.findViewById(R.id.textViewFecha_itemComentario);
			textViewFecha.setText(String.format(ViewUtil.FORMATO_FECHA_HORA, comentario.getFechaComentario()));
			
			TextView textViewPuntTextView = (TextView)view.findViewById(R.id.textViewPuntuacion_itemComentario);
			textViewPuntTextView.setText(ViewUtil.obtenerEstrellas(comentario.getPuntuacion()));
		}
		return view;
	}

}
