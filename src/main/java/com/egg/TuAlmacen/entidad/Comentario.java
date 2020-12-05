package com.egg.TuAlmacen.entidad;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


import org.hibernate.annotations.GenericGenerator;
	
	@Entity
	public class Comentario {
		
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	private String id;
	
	private String comentario;
	
	@ManyToOne
	private Producto producto;
	
	@ManyToOne
	private Usuario usuario;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
}
