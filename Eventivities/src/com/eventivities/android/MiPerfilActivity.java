package com.eventivities.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Window;
import com.eventivities.android.excepciones.ExcepcionAplicacion;
import com.eventivities.android.servicioweb.Conexion;

public class MiPerfilActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mi_perfil);
		getSherlock().setProgressBarIndeterminateVisibility(false);
		
		Button btnAceptar = (Button) findViewById(R.id.buttonLogIn);
		btnAceptar.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Boolean logueado = comprobarUsuarioLogueado();
				if(!logueado){
					TextView textUser = (TextView) findViewById(R.id.editTextUserName);
					TextView textPass = (TextView) findViewById(R.id.editTextUserPassword);
					String user = textUser.getText().toString();
					String pass = textPass.getText().toString();										
					
					LogInAsyncTask task = new LogInAsyncTask();
					task.setUser(user);
					task.setPassword(pass);
					task.setContext(MiPerfilActivity.this);
					task.execute();
				}else{					
					guardarPreferenciasLogOut();
					String textoBoton = getResources().getString(R.string.textButtonLogIn);
					actualizarTextoBotonLogIn(textoBoton);
					actualizarElementosInterfaz(true);
					
					supportInvalidateOptionsMenu();
				}
			}	
		});
		btnAceptar.setEnabled(false);
		
		final Button btnRegistrar = (Button) findViewById(R.id.buttonRegistro);
		btnRegistrar.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				TextView textUser = (TextView) findViewById(R.id.editTextUserName);
				TextView textPass = (TextView) findViewById(R.id.editTextUserPassword);
				
				final String user = textUser.getText().toString();
				final String pass = textPass.getText().toString();
				
				Context context = (Context) MiPerfilActivity.this;
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(getResources().getString(R.string.textoCabeceraDialogo));
				
				String mensaje = getResources().getString(R.string.textoMensajeDialogo);				
				mensaje += getResources().getString(R.string.textUserName)+" "+user;
				mensaje += "\n";
				mensaje += getResources().getString(R.string.textUserPassword)+" "+pass;
				
				builder.setMessage(mensaje);
				builder.setPositiveButton(R.string.textoBotonContinuar, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						RegistroAsyncTask task = new RegistroAsyncTask();
						task.setContext(MiPerfilActivity.this);
						task.setUser(user);
						task.setPassword(pass);
						task.execute();
					}
				});
				
				builder.setNegativeButton(R.string.textoBotonCancelar, new DialogInterface.OnClickListener(){
					public void  onClick(DialogInterface dialog, int id){
						
					}					
				});				
				builder.show();
			}			
		});
		btnRegistrar.setEnabled(false);
		
		EditText editTextUser = (EditText) findViewById(R.id.editTextUserName);
		editTextUser.addTextChangedListener(new TextWatcher(){
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
				Button btnAceptar = (Button) findViewById(R.id.buttonLogIn); 
				boolean camposCompletos = comprobarCamposCompletos();
				if(camposCompletos){
					btnAceptar.setEnabled(true);
					btnRegistrar.setEnabled(true);
				}
				else{
					btnAceptar.setEnabled(false);
					btnRegistrar.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

		});
		
		EditText editTextPass = (EditText) findViewById(R.id.editTextUserPassword);
		editTextPass.addTextChangedListener(new TextWatcher(){
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
				Button btnAceptar = (Button) findViewById(R.id.buttonLogIn); 
				boolean camposCompletos = comprobarCamposCompletos();
				if(camposCompletos){
					btnAceptar.setEnabled(true);
					btnRegistrar.setEnabled(true);
				}
				else{
					btnAceptar.setEnabled(false);
					btnRegistrar.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {		
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

		});
	}

	@Override
	protected void onResume() {
		cargarPreferencias();
		super.onResume();
	}
	
	/**
	 * Método que guarda las preferencias cuando un usuario hace LogIn
	 * en la aplicación. Se encarga de guardar los datos 'username' y 'password' e iDusuario
	 * además de actualizar el campo 'logIn' a true.
	 * 
	 * @author emilio //Modificada por Marcos
	 * @param idUsuario
	 * @return No retorna ningún valor. Se crea o actualiza el fichero de preferencias
	 * @see SharedPreferences
	 * */
	public void guardarPreferenciasLogIn(int idUsuario){
		SharedPreferences prefs = getSharedPreferences("LogInPreferences", Context.MODE_PRIVATE);
		EditText userEditText = (EditText) findViewById(R.id.editTextUserName);
		EditText passEditText = (EditText) findViewById(R.id.editTextUserPassword);
		
		Editor editor = prefs.edit();
		editor.putString("usuarioActual", userEditText.getText().toString());
		editor.putString("passActual", passEditText.getText().toString());
		editor.putString("idUsuario", String.valueOf(idUsuario));
		editor.putBoolean("logIn", true);
		editor.commit();
	}
	
	/**
	 * Método que guarda las preferencias cuando un usuario hace LogOut
	 * en la aplicación. Se encarga de cambiar a false el valor del campo
	 * 'logIn' del fichero.
	 * 
	 * @author emilio
	 * @param No necesita parámetros de entrada
	 * @return No retorna ningún valor. Se actualiza el fichero de preferencias
	 * @see SharedPreferences, guardarPreferenciasLogIn
	 * */
	public void guardarPreferenciasLogOut(){
		SharedPreferences prefs = getSharedPreferences("LogInPreferences", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("logIn", false);
		editor.commit();
	}
	
	/**
	 * Método que carga información relativa al usuario logueado en la aplicación.
	 * También configura elementos de la interfaz gráfica.
	 * 
	 * @author emilio
	 * @param No necesita parámetros
	 * @return No retorna ningún valor. Actualiza la interfaz gráfica
	 * @see 
	 * */
	public void cargarPreferencias(){
		SharedPreferences prefs = getSharedPreferences("LogInPreferences", Context.MODE_PRIVATE);
		EditText editTextUser = (EditText) findViewById(R.id.editTextUserName);
		EditText editTextPass = (EditText) findViewById(R.id.editTextUserPassword);
		if(prefs != null && prefs.getBoolean("logIn", false)){
			editTextUser.setText(prefs.getString("usuarioActual", " "));
			editTextPass.setText(prefs.getString("passActual", " "));
			actualizarTextoBotonLogIn(this.getResources().getString(R.string.textButtonLogOut));
			actualizarElementosInterfaz(false);
			
		}
	}
	
	/**
	 * Método encargado de modificar el texto del boton de LogIn cuando el usuario hace
	 * login y logout.
	 * 
	 * @author emilio
	 * @param texto
	 * @return No retorna ningún valor directamente.
	 * @see
	 * 
	 * */
	public void actualizarTextoBotonLogIn(String texto){
		Button botonLogIn = (Button) findViewById(R.id.buttonLogIn);
		botonLogIn.setText(texto);
	}
	
	/**
	 * Método que habilita y deshabilita los campos de texto de la interfaz gráfica
	 * cuando el usuario hace login y logout en la aplicación. Al hacer login se deshabilitarán
	 * para así conservar los datos introducidos. Una vez se haga logout se habilitarán para que puedan
	 * ser utilizados de nuevo.
	 * 
	 * @author emilio
	 * @param No se necesitan parámetros
	 * @return No retorna ningún valor. Tan sólo actualizará la IGU
	 * @see
	 * */
	public void actualizarElementosInterfaz(Boolean nuevoEstado){
		EditText eTextUser = (EditText) findViewById(R.id.editTextUserName);
		eTextUser.setEnabled(nuevoEstado);
		eTextUser.clearFocus();
		EditText eTextPass = (EditText) findViewById(R.id.editTextUserPassword);
		eTextPass.setEnabled(nuevoEstado);
		eTextPass.clearFocus();
		Button btnRegistrar = (Button) findViewById(R.id.buttonRegistro);
		btnRegistrar.setEnabled(nuevoEstado);
	}
	
	/**
	 * Método que comprueba si el usuario está logueado en la apliación.
	 * 
	 * @author emilio
	 * @param No necesita parámetros. Consulta el fichero de preferencias
	 * @return Retorna true si el usuario ha hecho LogIn y false en caso contrario.
	 * @see 
	 * */
	public Boolean comprobarUsuarioLogueado(){
		SharedPreferences prefs = getSharedPreferences("LogInPreferences", Context.MODE_PRIVATE);
		Boolean logueado = prefs.getBoolean("logIn", false);
		return logueado;
	}
	
	/**
	 * 
	 * Método que comprueba si los campos Usuario y Contraseña de la interfaz tienen texto.
	 * Si ambos campos tienen texto se habilitará el botón Aceptar. En caso contrario permanecerá deshabilitado.
	 *
	 * @author emilio
	 * @param No necesita parámetros.
	 * @return True si ambos campos de texto están completos. False en caso contrario
	 * 
	 * */
	public boolean comprobarCamposCompletos(){
		boolean completos;
		EditText editTextUser = (EditText) findViewById(R.id.editTextUserName);
		EditText editTextPass = (EditText) findViewById(R.id.editTextUserPassword);
		if(editTextUser.getText().toString().equals("") || editTextPass.getText().toString().equals(""))
			completos = false;
		else 
			completos = true;
		return completos;
	}
	
	/**
	 * Clase encargada de realizar las labores de la tarea asíncrona que gestiona
	 * el logIn del usuario en la aplicación
	 * 
	 * @author emilio
	 * @param 
	 * @result No retorna ningún valor. Se encarga de invocar a los métodos necesarios
	 * 		   para actualizar el fichero de preferencias y la interfaz de la aplicación
	 * @see 
	 * */
	private class LogInAsyncTask extends AsyncTask <Void, Void, Integer>{

		private String user, pass;
		private Context context;
		
		@Override
		protected void onPreExecute(){
			getSherlock().setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			getSherlock().setProgressBarIndeterminateVisibility(false);
			String mensaje;
			//result vale -1 si la identificación es errónea, un -2 si hay un error con el servidor
			//y el idusuario en el resto de casos
			if(result!=-1 && result!=-2){
				guardarPreferenciasLogIn(result);
				String textoBoton = getResources().getString(R.string.textButtonLogOut);
				actualizarTextoBotonLogIn(textoBoton);
				actualizarElementosInterfaz(false);
				supportInvalidateOptionsMenu();
				mensaje=context.getString(R.string.textLoginOk);				
				finish();
			}
			else			
			{
				if(result==-1)
					mensaje=context.getString(R.string.textDatosErroneos);
				else 
					mensaje=context.getString(R.string.textLoginServerError);				
			}
			Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
			super.onPostExecute(result);			
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			Integer resultado = 0;
			try{
				String hashPass = String.valueOf(pass.hashCode());
				resultado=Conexion.identificarse(user, hashPass);				
			} catch (ExcepcionAplicacion e){
				e.printStackTrace();
				resultado=-2;
			}
			return resultado;
		}
		
		/*Atributos necesarios para conectar con el servidor*/
		public void setContext(Context context){
			this.context = context;
		}
		public void setUser(String user){
			this.user = user;
		}
		public void setPassword(String pass){
			this.pass = pass;
		}
	}
	
	/**
	 * Clase que representa la tarea asíncrona que se utilizará para realizar el registro del usuario cuando 
	 * éste complete los campos necesarios y pulse el botón Registro.
	 * 
	 * @author emilio
	 * @param Los parámetros que necesita los recibe a través de los métodos set.
	 * @return No retorna ningún valor. En caso de que no se haya podido realizar el registro se mostrará un mensaje
	 * para informar al usuario.
	 * @see 
	 * */
	private class RegistroAsyncTask extends AsyncTask <Void, Void, Integer>{
		
		private String user, pass;
		private Context context;
		
		@Override
		protected Integer doInBackground(Void... params){
			Integer resultado=0;
			try{
				String hashPass = String.valueOf(pass.hashCode());
				if (Conexion.registrarUsuario(user, hashPass))
						resultado=1;
			}catch(ExcepcionAplicacion e){
				e.printStackTrace();
				resultado=3;
			}
			return resultado;
		}
		
		@Override
		protected void onPreExecute(){
			getSherlock().setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Integer result){
			getSherlock().setProgressBarIndeterminateVisibility(false);
			String mensaje = "";
			if(result==1)
				mensaje = context.getString(R.string.textRegistroOk);
			else
			{
				if(result==0)
					mensaje = context.getString(R.string.textAliasDuplicado);
				else
					mensaje = context.getString(R.string.textErrorRegistro);
			}
			Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
		}
		
		public void setUser(String user){
			this.user = user;
		}
		
		public void setPassword(String pass){
			this.pass = pass;
		}
		
		public void setContext(Context context){
			this.context = context;
		}
	}
}
