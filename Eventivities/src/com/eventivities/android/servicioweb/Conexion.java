package com.eventivities.android.servicioweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import com.eventivities.android.domain.ListaEventos;
import com.eventivities.android.excepciones.ExcepcionAplicacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Clase encargada de las conexiones al servicio Web 
* 
* @author marcos
* 
* @see         Conexion
*/
public class Conexion {
	
	
	private final static String url="http://www.eventivitiesadm.eshost.es/servicioweb/";
	//private final static String url="http://10.0.2.2/www/";	
	
	/**
	 * Devuelve un listado de obras de un teatro
	* <p>
	* Si la búsqueda no produce ningún resultado, 
	* 
	*
	* @author marcos
	* @
	* @param  idTeatro el identificador único del teatro 
	* @return      la lista de obras
	* @see         Conexion
	*/
	public static ListaEventos obtenerEventosLocal(String idLocal) throws ExcepcionAplicacion
	{
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("idLocal", idLocal));	
		JSONObject json;
		ListaEventos respuesta = null;
		try {
			json = obtenerJsonDelServicio(pairs,"service.obtenereventoslocal.php");
			int exito=1;
			if(json!=null)
			{			
				if (json.has("exito"))
				{
					if(json.getString("exito").equalsIgnoreCase("1"))
					{
						GsonBuilder gsonBuilder = new GsonBuilder();
						gsonBuilder.setDateFormat("yyyy-MM-dd");
						Gson gson = gsonBuilder.create();				
						respuesta = gson.fromJson(json.toString(), ListaEventos.class);
					}
					else
					{
						exito=0;
					}
				}
				else
				{
					exito=0;
				}
				if (exito==0)
					throw new ExcepcionAplicacion("El servicio web no ha respondido con exito",ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
				
			}	
				
		} catch (ClientProtocolException c)
		{
			throw new ExcepcionAplicacion(c.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ExcepcionAplicacion(e.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new ExcepcionAplicacion(e.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ExcepcionAplicacion(e.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ExcepcionAplicacion(e.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		}
		return respuesta;
	}
	
	public static ListaEventos obtenerPuntuacionesEvento(String idEvento) throws ExcepcionAplicacion
	{
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("idEvento", idEvento));	
		JSONObject json;
		ListaEventos respuesta = null;
		try {
			json = obtenerJsonDelServicio(pairs,"service.obtenerpuntuacionesevento.php");
			int exito=1;
			if(json!=null)
			{			
				if (json.has("exito"))
				{
					if(json.getString("exito").equalsIgnoreCase("1"))
					{
						GsonBuilder gsonBuilder = new GsonBuilder();
						gsonBuilder.setDateFormat("yyyy-MM-dd");
						Gson gson = gsonBuilder.create();				
						respuesta = gson.fromJson(json.toString(), ListaEventos.class);
					}
					else
					{
						exito=0;
					}
				}
				else
				{
					exito=0;
				}
				if (exito==0)
					throw new ExcepcionAplicacion("El servicio web no ha respondido con exito",ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
				
			}	
				
		} catch (ClientProtocolException c)
		{
			throw new ExcepcionAplicacion(c.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ExcepcionAplicacion(e.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new ExcepcionAplicacion(e.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ExcepcionAplicacion(e.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ExcepcionAplicacion(e.getMessage(),ExcepcionAplicacion.EXCEPCION_CONEXION_SERVIDOR);
		}
		return respuesta;
	}
	
	private static JSONObject obtenerJsonDelServicio(List<NameValuePair> pairs, String servicio) throws ClientProtocolException, IOException, JSONException {
		HttpClient client = new DefaultHttpClient();		
		JSONObject json=null;		
				
		HttpPost request = new HttpPost(url+servicio);		
		request.setHeader("Accept","application/json");	
		request.setEntity(new UrlEncodedFormEntity(pairs));
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity(); 
		String responseString=null;			
		
		if (entity != null) { 
			InputStream stream = entity.getContent(); 
			BufferedReader reader = new BufferedReader( 
			new InputStreamReader(stream)); 
			StringBuilder sb = new StringBuilder();
			String line = null; 
			while ((line = reader.readLine()) != null) { 
				sb.append(line + "\n"); 
			}		 
			stream.close(); 
			responseString = sb.toString();								
			json = new JSONObject(responseString);						
		}			
		return json;
	
	}	
	
}
