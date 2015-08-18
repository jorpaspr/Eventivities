package com.eventivities.android.domain;

import java.util.List;

public class ListaComentarios {	

	private List<Comentario> comentarios;
	
	public List<Comentario> getComentarios() {
		return comentarios;
	}
	
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	
	//TNI añadido para poder poner un comentario si no hay comentarios en la BD o se produce un error
	public void anyadirComentario(Comentario comentario) {
		comentarios.add(comentario);
	}

	//TNI añadido para poder poner un comentario si no hay comentarios en la BD 
	public int numComentarios(){
		return comentarios.size();
	}
	

}
	

