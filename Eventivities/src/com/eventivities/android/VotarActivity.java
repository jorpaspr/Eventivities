package com.eventivities.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eventivities.android.domain.Evento;
import com.eventivities.android.excepciones.ExcepcionAplicacion;
import com.eventivities.android.servicioweb.Conexion;
import com.eventivities.android.util.ImageAsyncHelper;
import com.eventivities.android.util.ImageAsyncHelper.ImageAsyncHelperCallBack;
import com.eventivities.android.util.TnUtil;
import com.eventivities.android.util.ViewUtil;
/**
 * Actividad encargada del Voto y compartir 
 * 
 * @author Toni
 * @param 
 * @return 
 * @see Actividades
 * */

public class VotarActivity extends BaseActivity {
	private boolean heVotado=false;
	private Evento evento;
	private String nombreLocal;
	
	private boolean noSePuedeVotar=false; // error no se puede votar aunque se este logeado
	private boolean login =false;   // si esta a true puede votar
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_votar);
		
		TextView mTxt=(TextView) findViewById(R.id.votar_cuantasLetrasQuedan);
		mTxt.setText("");

        Bundle extras = getIntent().getExtras();
        
		TextView nomL=(TextView) findViewById(R.id.votar_nombreTeatro);
		TextView nomE=(TextView) findViewById(R.id.votar_nombreEvento);
		if(extras != null ){
			evento = (Evento) extras.getSerializable(Param.EVENTO.toString());
			nombreLocal = (String) extras.getString(Param.LOCAL_NOMBRE.toString());
			nomL.setText(nombreLocal);
			nomE.setText(evento.getNombre());
			final ImageView imageViewEvento = (ImageView)findViewById(R.id.votar_imagenEvento);
			
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
		}else{
			nomL.setText("--");
			nomE.setText("--");
			
			noSePuedeVotar=true;   // existe un error no hay evento o teatro  o algo ha ido mal
			TnUtil.escribeLog("No han llegado datos desde Ver Comentarios");
			TnUtil.escribeLog("No se puede Votar");
		}
		
		
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onResume(){
		super.onResume();
		
		leerPrefVotar();
		if (heVotado){
			EditText mTxt=(EditText) findViewById(R.id.votar_comentario);
			RatingBar mRat=(RatingBar) findViewById(R.id.votar_ratingBar);
			Button bVotar = (Button) findViewById(R.id.votar_botonVotar);
			mTxt.setEnabled(false);
			mRat.setEnabled(false);
			bVotar.setEnabled(false);
		}		
	}
	
	@Override
	protected void onPause() {
		guardarPrefVotar();
		super.onPause();
	}

	@Override
	protected void onDestroy() {

		if ( isFinishing())
			//si es el usuario el que cierra la actividad
			iniciarPrefVotar();
		
		super.onDestroy();
	}

	private void iniciarMiPerfil(){
		startActivity(new Intent(VotarActivity.this, MiPerfilActivity.class)
		.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
	}
	

	/**
	 * Método que guarda si se ha votado o no, ya que los objetos de la pantalla
	 * se deshabilitan una vez se ha votado por dos razones
	 * 1 - evitar un posible doble-click
	 * 2- que el usuario sea consciente que se esta haciendo una accion
	 * 
	 * @author Toni
	 * @param No necesita parámetros de entrada
	 * @return No retorna ningún valor. Se actualiza el fichero de preferencias de esta actividad
	 * @see Preferences, guardarPrefVotar, leerPrefVotar, inicializarPrefVotar
	 * */
	private void guardarPrefVotar(){
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		Editor editor = prefs.edit();
		Button bVotar = (Button) findViewById(R.id.votar_botonVotar);
		
        if (! bVotar.isEnabled())
        	heVotado=true;
        else
        	heVotado=false;

        editor.putBoolean("votado", heVotado);
		editor.commit();				
	}

	/**
	 * Método que inicializa (pone a false) la accion de votar
	 * debe llamase exclusivamente en el onDestroy ya que en el onCreate al girar la pantalla
	 * el sistema mata la actividad y la vuelve a crear pasando por el onCreate que habilitaria
	 * el voto si ya se ha votado antes de girar la pantalla 
	 * 
	 * 
	 * @author Toni
	 * @param No necesita parámetros de entrada
	 * @return No retorna ningún valor. Se actualiza el fichero de preferencias de esta actividad
	 * @see Preferences, guardarPrefVotar, leerPrefVotar, inicializarPrefVotar
	 * */
	private void iniciarPrefVotar(){
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		Editor editor = prefs.edit();
		heVotado=false;
        editor.putBoolean("votado", heVotado);
		editor.commit();				
	}	
	/**
	 * Método que lee de las preferencias si se ha votado, ya que los objetos de la pantalla
	 * se deshabilitan una vez se ha votado por dos razones
	 * 1 - evitar un posible doble-click
	 * 2- que el usuario sea consciente que se esta haciendo una accion
	 * 
	 * 
	 * @author Toni
	 * @param No necesita parámetros de entrada
	 * @return No retorna ningún valor. Se actualiza el fichero de preferencias de esta actividad
	 * @see Preferences, guardarPrefVotar, leerPrefVotar, inicializarPrefVotar
	 * */
	private void leerPrefVotar(){
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		heVotado =prefs.getBoolean("votado", heVotado);
	}
	
	public void accionVotar(View v){
		
		if (noSePuedeVotar){
			mensajeNoSePuedeVotar();
		}
		/// QUeria hacerlo asi asi para no duplicar codigo y utilizar el de Emilio pero Explota
		//MiPerfilActivity mP=new MiPerfilActivity ();
		//login= mP.comprobarUsuarioLogueado();
		SharedPreferences prefs = getSharedPreferences("LogInPreferences", Context.MODE_PRIVATE);
		login = prefs.getBoolean("logIn", false);
		
		if (! login) {
			TnUtil.escribeLog("usuario no regsitradp / dado de alta");
			new AlertDialog.Builder(this)
		    .setTitle(getString(R.string.votar_noEstaLoginDialogTitulo))
		    .setMessage(getString(R.string.votar_noEstaLoginDialogTitulo_texto))
		    .setPositiveButton(getString(R.string.votar_noEstaLoginDialogTitulo_botonSi), new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            
		        	TnUtil.escribeLog("votar logearse");
		            iniciarMiPerfil();
		        	
		        }
		     })
		    .setNegativeButton(getString(R.string.votar_noEstaLoginDialogTitulo_botonNo), new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	TnUtil.escribeLog("votar NO logearse");
		            // do nothing
		        }
		     })
		     .show();
		}else{
			new VotarAsyncTask().execute();
			TnUtil.escribeLog("esta login");
		}
	}
	
	
	private String dameComentario(){
		EditText mTxt=(EditText) findViewById(R.id.votar_comentario);
    	return mTxt.getText().toString();
	}
	
	private int damePuntuacion(){
		RatingBar mRat=(RatingBar) findViewById(R.id.votar_ratingBar);
    	return (int) mRat.getRating();
	}

	public void accionCompartir(View v){
    	Intent sharingIntent = new Intent(Intent.ACTION_SEND);

    	    //definimos el tipo de dato que vamos a compartir
        	sharingIntent.setType("text/plain");
        	
            //obtenemos el nombre del Local y el Nombre del Evento
        	String mTeatro=nombreLocal;       //"Espacio Inestable";
        	String mObra=evento.getNombre();  //"Yo nunca sere una estrella del Rock";
        	
        	//recopilamos la información para el comentario
        	//EditText mTxt=(EditText) findViewById(R.id.votar_comentario);
        	String txt=dameComentario();
        	if ( txt.trim().equals(""))
        		txt=getString(R.string.votar_compartir_sinComentario);
        	
            //obtenemos el voto y seleccionamos una frase
        	int i=damePuntuacion();
        	
        	String tRat="★★★☆☆";   //valor por defecto=3 estrellas
        	String tRatTxt=getString(R.string.votar_compartir_tresEstrella);

        	
        	tRat=ViewUtil.obtenerEstrellas(i);
        	switch (i){
        		case 0: 
        			tRatTxt=getString(R.string.votar_compartir_ceroEstrella);
        			break;
    	        case 1:
    	        	tRatTxt=getString(R.string.votar_compartir_unaEstrella);
    	        	break;
        		case 2:
        			tRatTxt=getString(R.string.votar_compartir_dosEstrella);
        			break;
        		case 4:
        			tRatTxt=getString(R.string.votar_compartir_cuatroEstrella);
	        		break;
        		case 5:
        			tRatTxt=getString(R.string.votar_compartir_cincoEstrella);
	        		break;
                //no hay default ya que esta determinado como 3 antes del switch 
        	}        		

        	
        	//Componer el mensaje a compartir
        	txt=getString(R.string.votar_compartir_heEstado)+" \n" +
        			"["+mTeatro+"] "+getString(R.string.votar_compartir_viendo)+ " \"" +
        			   mObra+"\"\n  "+
        			   tRatTxt+" ("+tRat+") : " +txt;
        	
        	//continuamos la accion
        	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, txt);
        	startActivity(Intent.createChooser(sharingIntent,"Share using"));
        	sharingIntent = new Intent(Intent.ACTION_SEND);
        	
        	
        	TnUtil.escribeLog("se ha compartido evento \n"+txt);
	}

	
	public void mensajeNoSePuedeVotar(){
		TnUtil.escribeLog("No se puede Votar");
		new AlertDialog.Builder(this)
	    .setTitle(getString(R.string.votar_noEstaLoginDialogNoSePuedeVotarTitulo))
	    .setMessage(getString(R.string.votar_noEstaLoginDialogNoSePuedeVotarTexto))
	    .setPositiveButton(getString(R.string.votar_noEstaLoginDialogTitulo_botonSi), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	TnUtil.escribeLog("No se puede Votar boton pulsado");
	        }
	     })
	     .show();

	}

	public void mensajeErrorAlVotar(){
		TnUtil.escribeLog(" Error");
		new AlertDialog.Builder(this)
	    .setTitle(getString(R.string.votar_ErrorAlVotarTitulo))
	    .setMessage(getString(R.string.votar_ErrorAlVotarTxt))
	    .setPositiveButton(getString(R.string.votar_ErrorAlVotarBoton), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	TnUtil.escribeLog("Boton  Dialogo de notificacion de error de conexion ");
	        }
	     })
	     .show();

	}

	public void mensajeYaVotaste(){
		TnUtil.escribeLog("Error","vota por segunda vez\n"+evento.getNombre());
		new AlertDialog.Builder(this)
	    .setTitle(getString(R.string.votar_soloSePuedeVotarUnaVez))
	    .setMessage(getString(R.string.votar_tuYaHasVotado))
	    .setPositiveButton(getString(R.string.votar_ErrorAlVotarBoton), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	TnUtil.escribeLog("Boton dialogo de voto ya emitido ");
	        }
	     })
	     .show();

	}
	
	private int queidUsusario(){
		SharedPreferences prefs = getSharedPreferences("LogInPreferences", Context.MODE_PRIVATE);
		return TnUtil.queNumero(prefs.getString("idUsuario","-4"));
	}
	
	private class VotarAsyncTask extends AsyncTask<Void,Void,Boolean> {
		private boolean errorBD=false; 
		
		EditText mTxt=(EditText) findViewById(R.id.votar_comentario);
		RatingBar mRat=(RatingBar) findViewById(R.id.votar_ratingBar);
		Button bVotar = (Button) findViewById(R.id.votar_botonVotar);
		
		int idUsuario=queidUsusario();
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			getSherlock().setProgressBarIndeterminateVisibility(true);
			mTxt.setEnabled(false);
			mRat.setEnabled(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean res=false;
			if (idUsuario<0){
				errorBD=true;
				return false;
			}
			try {
	        	String comentario=dameComentario();
	        	int puntuacion=damePuntuacion();
				res = Conexion.registrarComentarioYPuntuacion(
												idUsuario,
												evento.getIdEvento(), 
												puntuacion,
												comentario);
			} catch (ExcepcionAplicacion e) {
				//e.printStackTrace();
				errorBD=true;
				
				TnUtil.escribeLog("ERROR", "conexion al votar ");
			}
			return res ;
			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if ( !errorBD) bVotar.setEnabled(false);
			
			if (errorBD){
				mensajeErrorAlVotar();
			}else{
				if (!result && !errorBD) {
					mensajeYaVotaste();
				}
				if (result && !errorBD) {
					Toast.makeText(getBaseContext(),getString(R.string.votar_ExitoAlVotar),Toast.LENGTH_SHORT).show();
					TextView mTxt=(TextView) findViewById(R.id.votar_cuantasLetrasQuedan);
					mTxt.setText(getString(R.string.votar_VotoOk));
				}
			}
		     
			getSherlock().setProgressBarIndeterminateVisibility(false);

			//			no existe
			super.onPostExecute(result);
		}
		
	}
	
}
