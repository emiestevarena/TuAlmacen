
package com.egg.TuAlmacen.formato;

import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.enums.Estado;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.PedidoService;
import com.egg.TuAlmacen.service.ProductoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
public class Ventas {
 	private Producto producto;
	private Integer vendidos;
        
                
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
        
        

        
}

