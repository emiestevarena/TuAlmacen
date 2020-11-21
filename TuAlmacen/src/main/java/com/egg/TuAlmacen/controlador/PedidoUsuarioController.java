package com.egg.TuAlmacen.controlador;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.enums.Estado;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.PedidoService;
import com.egg.TuAlmacen.service.ProductoService;



@Controller
public class PedidoUsuarioController {
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private PedidoService pedidoService;


	@PreAuthorize("hasRole('ROLE_USUARIO')")
	@GetMapping("/miscompras")
	public String miscompras(ModelMap modelo) throws ErrorService {
		

		List<Producto> productos = productoService.listarProducto();
		
	
		Set<Estado> estado = EnumSet.allOf(Estado.class);
		
        modelo.put("estados", estado);
		
		modelo.put("productos", productos);

               
                
		return "miscompras.html";
		
	}
	
	@PreAuthorize("hasRole('ROLE_USUARIO')")
	@GetMapping("/compra")
	public String comprar(ModelMap modelo) throws ErrorService {
		
		List<Producto> productos = productoService.listarProducto();
		
		modelo.put("productos", productos);	
		
		Set<Estado> estado = EnumSet.allOf(Estado.class);
		
                modelo.put("estado", estado);
 
		return "compra.html";
		
	}
	
//	
//	@PreAuthorize("hasRole('ROLE_USUARIO')")
//	@PostMapping("/comprar")
//	public String comprar(ModelMap modelo,
//			HttpSession session,
//			@RequestParam String id,
//			@RequestParam List<String>idproducto,
//			@RequestParam List<Integer>cantidad,
//			@RequestParam String estado) {	
//		
//		try {	
//			
//			List<Producto> producto= new ArrayList<>();
//			for(String i: idproducto) {
//				producto.add(productoService.buscarPorId(i));
//			
//			pedidoService.registrarPedido(productos, cantidades, estado, usuario);
//			
//	
//			modelo.put("mensaje", "Se ha eliminado el pedido exitosamente");
//			
//		}catch(ErrorService e) {
//			modelo.addAttribute("error", e.getMessage());
//			return "redirect:/miscompras";
//		}
//		return "redirect:/miscompras";
//		
//	}
//	
//
//	@PreAuthorize("hasRole('ROLE_USUARIO')")
//	@PostMapping("/anular")
//	public String anular(ModelMap modelo,
//			HttpSession session,
//			@RequestParam String id) {	
//		
//		try {			
//			pedidoService.eliminarPedido(id);
//			
//	
//			modelo.put("mensaje", "Se ha eliminado el pedido exitosamente");
//			
//		}catch(ErrorService e) {
//			modelo.addAttribute("error", e.getMessage());
//			return "redirect:/miscompras";
//		}
//		return "redirect:/miscompras";
//		
//	}
//	
	

}
