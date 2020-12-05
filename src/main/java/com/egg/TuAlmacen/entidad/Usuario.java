package com.egg.TuAlmacen.entidad;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import org.hibernate.annotations.GenericGenerator;

import com.egg.TuAlmacen.enums.Rol;
@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	 private String id;
	 private String usuario;
	 private String password;
	 private String email;
	 
	 
	// @ManyToOne
	 @Enumerated(EnumType.STRING)
	 private Rol rol;
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	 
	 
}
