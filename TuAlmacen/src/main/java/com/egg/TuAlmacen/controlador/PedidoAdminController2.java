package com.egg.TuAlmacen.controlador;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.enums.Estado;
import com.egg.TuAlmacen.enums.Rubro;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.PedidoService;
import com.egg.TuAlmacen.service.ProductoService;

import edu.egg.entidades.Autor;
import edu.egg.entidades.Editorial;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;
import org.springframework.stereotype.Controller;

@Controller
public class PedidoAdminController2 {

	@Autowired
	private ProductoService productoService;
	@Autowired
	private PedidoService pedidoService;


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/pedido")
	public String pedido(ModelMap modelo) throws ErrorService {
		

		List<Producto> productos = productoService.listarProducto();
		modelo.put("productos", productos);	
		Set<Estado> estado = EnumSet.allOf(Estado.class);
        modelo.put("estado", estado);
        
                
		return "pedido.html";
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/pedido")
	public String pedido(ModelMap modelo, @RequestParam String id, 
			@RequestParam List<Producto> producto,@RequestParam List<Integer> cantidades,@RequestParam String fecha,@RequestParam String precioTotal,@RequestParam String estado
			) throws ErrorService{
		
		
		try {
			Date w4= pedidoService.convertirStringADate(fecha);
			
			pedidoService.modificarPedido(id,producto, cantidades, w4, Double.parseDouble(precioTotal), Estado.valueOf(estado));
		
			
		}catch(Exception e) {
			List<Producto> productoo = productoService.listarProducto();
			
			Set<Estado> estadoo = EnumSet.allOf(Estado.class);
	        modelo.put("productos", productoo);
	        modelo.put("estados", estadoo);
			
			modelo.put("cantidad", cantidades);
			modelo.put("error",e.getMessage());
			modelo.put("fecha", fecha);
			modelo.put("precioTotal", precioTotal);
			
			
			
			return "redirect:/pedido";
		}
		
		modelo.put("mensaje", "Has modificado el pedido exitosamente :D");
		
		
		return "pedido.html";
	}
	
	
	
	
	
}
