package com.eventivities.android.util;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

public class TnUtil {

	
	/**
    * Devuelve un entero desde un string
	*
	* @author Toni
	* @
	* @param  String a convertir a entero 
	* @return un entero o cero si no se ha podido convertir
	* @see    utilidades
	*/	public static int queNumero(String s){
		try{
			return Integer.parseInt(s);
		}catch( NumberFormatException e){
			return 0;
		}
	}
	
	/**
	    * Produce una vibracion corta
		*
		* @author Toni
		* @
		* @param  el contexto donde se va a utilizar 
		* @return nada
		* @see    utilidades
	*/
	public static void  vibrar(Context c){
		
		try{
			Vibrator v = (Vibrator) c.getSystemService(c.VIBRATOR_SERVICE);
			v.vibrate(30);
		}catch ( Exception e){
			escribeLog("VIBRAR","Error al intentar vibrar "+e.toString());
		}
	}
	
	/**
	    * Produce un sonido corto
		*
		*<p> ahora solo vibra
		*
		* @author Toni
		* @
		* @param  el contexto donde se va a utilizar 
		* @return nada
		* @see    utilidades
	*/
	public static void  suena(Context c){
		vibrar(c);
		/*
		try {
			android.media.ToneGenerator tg=new ToneGenerator(AudioManager.STREAM_NOTIFICATION,60);
			tg.startTone(ToneGenerator.TONE_DTMF_S);
			
		}catch( ExceptionInInitializerError e){
			DebugActivity.escribe("Error al intentar inicializar sonido "+e.toString());
		}catch (Exception e2){
			DebugActivity.escribe("Error al intentar sonar "+e2.toString());
		}
		*/
	}
	
	/**
	    * Devuelve un boolean desde un string 
		*
		* @author Toni
		* @
		* @param  String a convertir a boolean 
		* @return un boolean o false si no se ha podido convertir
		* @see    utilidades
	*/

	public static boolean queBoolean(String s){
		try{
			return Boolean.parseBoolean(s);
		}catch( NumberFormatException e){
			return false;
		}
	}
	/**
	    * Escribe en el Log.d 
		*
		* @author Toni
		* @
		* @param  Que: Etiqueta idef¡ntificativa, txt:texto a escribir 
		* @return nada
		* @see    utilidades
     */	
	
   public static void escribeLog(String Que,String txt){
	   
	   Log.d(Que,txt);
   }

	/**
    * Escribe en el Log.d, con etiqueta=Eventivities 
	*
	* @author Toni
	* @
	* @param  txt:texto a escribir 
	* @return nada
	* @see    utilidades
   */	
   
   public static void escribeLog(String txt){
	   
	   escribeLog("Activities",txt);
   }
	

}

