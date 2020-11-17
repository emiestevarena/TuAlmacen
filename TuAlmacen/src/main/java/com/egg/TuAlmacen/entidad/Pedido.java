package com.egg.TuAlmacen.entidad;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.egg.TuAlmacen.enums.Estado;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
@Entity
public class Pedido {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	private String id;
	
	@OneToMany
	private List<Producto> productos;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	private Double precioTotal;
	
	//@ManyToOne
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	private List<Integer>cantidad;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getPrecioTotal() {
		return precioTotal;
	}

	public void setPrecioTotal(Double precioTotal) {
		this.precioTotal = precioTotal;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Integer> getCantidad() {
		return cantidad;
	}

	public void setCantidad(List<Integer> cantidad) {
		this.cantidad = cantidad;
	}
	
	
	

}
