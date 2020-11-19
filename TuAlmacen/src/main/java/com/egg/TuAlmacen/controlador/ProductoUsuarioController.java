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
import com.egg.TuAlmacen.enums.Rubro;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.ProductoService;
import java.util.EnumSet;
import java.util.Set;
import org.springframework.stereotype.Controller;

@Controller
public class ProductoUsuarioController {

	@Autowired
	private ProductoService productoService;


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/productos")
	public String altaproducto(ModelMap modelo) throws ErrorService {
		

		List<Producto> productos = productoService.listarProducto();
		
		modelo.put("productos", productos);

                Set<Rubro> rubros = EnumSet.allOf(Rubro.class);
                modelo.put("rubros", rubros);
                
		return "productos.html";
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/comprar")
	public String altaproducto(ModelMap modelo,  
			@RequestParam String nombre,
			@RequestParam String precioCompra,
			@RequestParam String precioVenta,
			@RequestParam String cantidad,
			@RequestParam String descripcion,
			@RequestParam String rubro,
			MultipartFile archivo) throws ErrorService{
		
		
		try {
			
			productoService.registrarProducto(nombre,Double.parseDouble(precioCompra),
					Integer.parseInt(cantidad),Double.parseDouble(precioVenta),
					descripcion, archivo,Rubro.valueOf(rubro));
		
		
		}catch(Exception e) {
	
			
			modelo.put("nombre", nombre);
			modelo.put("error",e.getMessage());
			modelo.put("precioCompra", precioCompra);
			modelo.put("precioVenta", precioVenta);
			modelo.put("cantidad", cantidad);
			modelo.put("descripcion", descripcion);
			modelo.put("rubro", rubro);
			
			
			return "redirect:/altaproducto";
		}
		
		modelo.put("mensaje", "Has registrado el producto exitosamente :P");
		
		
		return "productos.html";
	}
	
	
	
	
}
