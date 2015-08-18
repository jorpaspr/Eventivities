package com.eventivities.android.excepciones;

@SuppressWarnings("serial")
public class ExcepcionAplicacion extends Exception  {
	
	public static final int EXCEPCION_SQLITE=1;
	public static final int EXCEPCION_CONEXION_SERVIDOR=2;
	public static final int EXCEPCION_DATOS_ERRONEOS=3;
	
	int codExcepcion;

	public ExcepcionAplicacion(String detailMessage, int codExcepcion) {
		super(detailMessage);
		this.codExcepcion=codExcepcion;
	}
	
	public int obtenerCodigoDeExcepcion()
	{
		return codExcepcion;
	}

}
