package com.eventivities.android.domain;

import java.util.Date;

/*Columna 	Tipo 	Nulo 	Predeterminado 	Comentarios
idLocal 	int(11) 	No 		
NombreLocal 	char(30) 	Sí 	NULL 	
Direccion 	char(30) 	No 		
Latitud 	char(30) 	Sí 	NULL 	
Longitud 	char(30) 	Sí 	NULL 	
idCategoria 	char(10) 	No 		
Ciudad 	char(30) 	No 		
Pais 	char(20) 	No 		
Telefono 	char(9) 	No 
Comentarios de la tabla: Imagen
			
Columna 	Tipo 	Nulo 	Predeterminado 	Comentarios
idImagen 	int(11) 	No
NombreImg 	char(100) 	No
FechaImg 	timestamp 	No 	CURRENT_TIMESTAMP*/




public class Local {
	private int idLocal=0;
	private String nombreLocal="";
	private String direccion="";
	private String latitud="";
	private String longitud="";
	private String idCategoria="";
	private String ciudad="";
	private String pais="";
	private String telefono="";
	private int idImagen;
	private String nombreImg;
	private Date fechaImg;
	
	public int getIdLocal() {
		return idLocal;
	}
	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}
	public String getNombreLocal() {
		return nombreLocal;
	}
	public void setNombreLocal(String nombreLocal) {
		this.nombreLocal = nombreLocal;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public int getIdImagen() {
		return idImagen;
	}
	public void setIdImagen(int idImagen) {
		this.idImagen = idImagen;
	}
	public String getNombreImg() {
		return nombreImg;
	}
	public void setNombreImg(String nombreImg) {
		this.nombreImg = nombreImg;
	}
	public Date getFechaImg() {
		return fechaImg;
	}
	public void setFechaImg(Date fechaImg) {
		this.fechaImg = fechaImg;
	}
	
	
}
