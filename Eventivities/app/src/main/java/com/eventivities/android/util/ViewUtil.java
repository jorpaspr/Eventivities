package com.eventivities.android.util;

import java.util.Date;

/**
 * @author jorge
 *
 */
public class ViewUtil {

	public static final String FORMATO_FECHA = "%1$te/%1$tm/%1$tY";
	public static final String FORMATO_FECHA_HORA = "%1$te/%1$tm/%1$tY %1$tk:%1$tM";
	
	/**
	 * Formatea un rango de fechas a partir de dos fechas y el formato escogido.
	 * 
	 * @param formatoRango
	 * @param inicio
	 * @param fin
	 * @return Cadena de texto con el rango de fechas.
	 */
	public static String rangoFecha(String formatoRango, Date inicio, Date fin)
	{
		String fechaInicio = String.format(FORMATO_FECHA, inicio);
		String fechaFin = String.format(FORMATO_FECHA, fin);
		
		return String.format(formatoRango, fechaInicio, fechaFin);
	}

	/**
	 * Genera una cadena con 5 estrellas a partir del valor de la puntuación. 
	 * 
	 * @param puntuacion
	 * @return Cadena de texto con las estrellas rellenadas.
	 */
	public static String obtenerEstrellas(double puntuacion)
	{
		int estrellas = (int) Math.round(puntuacion);
		
		switch (estrellas) {
		case 1: return "★☆☆☆☆";
		case 2: return "★★☆☆☆";
		case 3: return "★★★☆☆";
		case 4: return "★★★★☆";
		case 5: return "★★★★★";
		default: return "☆☆☆☆☆";
		}
	}
}
