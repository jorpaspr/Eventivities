package com.eventivities.android;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.view.Window;
import com.eventivities.android.adapters.ComentariosAdapter;
import com.eventivities.android.domain.Comentario;
import com.eventivities.android.domain.Evento;
import com.eventivities.android.excepciones.ExcepcionAplicacion;
import com.eventivities.android.servicioweb.Conexion;

public class VerComentariosActivity extends BaseActivity{

	private Evento evento;
	ListView listaComentarios;
	private List<Comentario> comentarios = null;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		
        Bundle extras = getIntent().getExtras();        
		if(extras != null ){
			evento = (Evento) extras.getSerializable(Param.EVENTO.toString());
			
			if (evento != null) {
				new ComentariosAsyncTask().execute();
			}			
		}
		
	}
	
	@Override
	protected void refresh() {
		super.refresh();
		new ComentariosAsyncTask().execute();
	}

	private class ComentariosAsyncTask extends AsyncTask<Void, Void, List<Comentario>> {

		@Override
		protected void onPreExecute() {
			getSherlock().setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}

		@Override
		protected List<Comentario> doInBackground(Void... params) {
			try {
				String n=String.valueOf(evento.getIdEvento());
				comentarios = Conexion.obtenerComentariosEvento(n).getComentarios();
			} catch (ExcepcionAplicacion e) {
				comentarios = null;
				e.printStackTrace();
			}
			return comentarios ;
		}

		@Override
		protected void onPostExecute(List<Comentario> result) {
			if (result != null) {
				setContentView(R.layout.activity_ver_comentarios);
				ListView listView = (ListView) findViewById(android.R.id.list);
				ComentariosAdapter adapter = new ComentariosAdapter(getApplicationContext(), R.layout.item_comentario, comentarios);
				listView.setAdapter(adapter);
			} else {
				setContentView(R.layout.error_conexion);
			}
		     
			getSherlock().setProgressBarIndeterminateVisibility(false);
			
			super.onPostExecute(result);
		}
		
	}

}
