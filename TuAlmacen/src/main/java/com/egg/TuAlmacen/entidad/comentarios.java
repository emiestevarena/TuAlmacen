package com.egg.TuAlmacen.entidad;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class comentarios {
@Id
private String id;
@OneToOne
private Producto producto;
@OneToOne
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


}
