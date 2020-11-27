
package com.egg.TuAlmacen.formato;

import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.enums.Estado;
import com.egg.TuAlmacen.service.PedidoService;
import com.egg.TuAlmacen.service.ProductoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;



public class Ventas {
 	private Producto producto;
	private Integer vendidos;
        
        @Autowired
	private ProductoService productoservice;
	
        @Autowired
	private PedidoService pedidoservice;
                
                public Ventas(){}
	
	public void setProducto(Producto p){
		this.producto=p;
	}
	
	public Producto getProducto(){
		return producto;
	}
	
	public void setVendidos(Integer v){
		this.vendidos=v;
	}
	
	public Integer getVendidos(){
		return vendidos;
	}
        
        public  List<Ventas> masVendidos(){
	List<Ventas> masVendidos = new ArrayList<>();
	List<Producto> productos = productoservice.findAll();
	List<Pedido> confirmados = pedidoservice.listarPedidosPorEstado(Estado.CONFIRMADO);
	for(Producto p: productos){
		Ventas v = new Ventas();
		v.setProducto(p);
		v.setVendidos(0);
		for(Pedido pedido: confirmados){
			if(pedido.getProductos().contains(p)){
				v.setVendidos(v.getVendidos()+pedido.getCantidad().get(pedido.getProductos().indexOf(p)));
			}						
		}
		masVendidos.add(v);	
	}
	for(int i=0;i<masVendidos.size();i++){
		for(int j=i+1;j<masVendidos.size();j++){
			Ventas actual = masVendidos.get(i);
			Ventas siguiente = masVendidos.get(j); 
			if(actual.getVendidos()<siguiente.getVendidos()){
				masVendidos.set(i,siguiente);
				masVendidos.set(j,actual);
			}
		}
	}
	return masVendidos;
}

        
}

