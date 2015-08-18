package com.eventivities.android.domain;

import java.io.Serializable;
import java.util.Date;

/*Tabla: Evento

Columna 	Tipo 	Nulo Predeterminado 
idEvento 	int(11) No 		
Nombre 		text 	No 		
Descripcion text 	No 		
FechaInicio timestamp 	No 		
FechaFin 	timestamp 	No 		
idLocal 	int(11)	No 		
idTipoEventoint(11)	No 		
Precio 		double 	No 	0 	
Director 	text 	No 		
Interpretes text 	Sí 	NULL
Duracion 	int(11)	Sí  NULL 	
HoraInicio 	time 	Sí 	NULL
El campo media es la media de las puntuaciones para ese evento

Tabla Imagen
Columna 	Tipo 	Nulo 	Predeterminado 	Comentarios
idImagen 	int(11) 	No
Nombre 	char(100) 	No
FECHA 	timestamp 	No 	CURRENT_TIMESTAMP*/

public class Evento implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8444706501016450603L;
	private int idEvento=0;
	private String nombre="";
	private String descripcion="";
	private Date fechaInicio;
	private Date fechaFin;
	private int idLocal=0;
	private int idTipoEvento=0;
	private double precio=0;
	private String director="";
	private String interpretes="";
	private double media=0; 
	private int duracion=0;
	private String horaInicio=null;
	private int idImagen;
	private String nombreImg;
	private Date fechaImg;
	
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
	public int getDuracion() {
		return duracion;
	}
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	
	public double getMedia() {
		return media;
	}
	public void setMedia(double media) {
		this.media = media;
	}
	public int getIdTipoEvento() {
		return idTipoEvento;
	}
	public void setIdTipoEvento(int idTipoEvento) {
		this.idTipoEvento = idTipoEvento;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getInterpretes() {
		return interpretes;
	}
	public void setInterpretes(String interpretes) {
		this.interpretes = interpretes;
	}
	public int getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public int getIdLocal() {
		return idLocal;
	}
	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}
}
