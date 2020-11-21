package com.egg.TuAlmacen.controlador;

/**
 *
 * @author octav
 */
import com.egg.TuAlmacen.entidad.Pedido;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.enums.Estado;
import com.egg.TuAlmacen.enums.Rubro;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.service.PedidoService;
import com.egg.TuAlmacen.service.ProductoService;
import com.egg.TuAlmacen.service.UsuarioService;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Controller;

@Controller
public class PedidoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HttpSession session;

    @PostMapping("/agregar")
    public String agregar(@RequestParam String idUsuario, @RequestParam String id, @RequestParam Integer cantidad) {

        System.out.println("AASDGFBQALWIEGASDFJHADF");

        Pedido pedido = pedidoService.carrito(idUsuario);
        System.out.println("BUSCO CARRITO");

        Producto producto = productoService.buscarPorId(id);

        if (pedido == null) {
            System.out.println("ADENTRO DEL IF NULL PEDIDO");
            pedidoService.miCarrito(usuarioService.buscarPorId(idUsuario), producto, cantidad);
        } else {

            System.out.println("ELSE DEL IF NULL PEDIDO");

            pedidoService.agregar(pedido, producto, cantidad);
        }

        return "redirect:/inicio";
    }

    @GetMapping("/compra")
    public String compra(ModelMap modelo) {

        Usuario u = (Usuario) session.getAttribute("usuariosession");
        Pedido pedido = pedidoService.carrito(u.getId());

        List<String[]> lista = new ArrayList();

        Double total = 0.0;

        for (int i = 0; i < pedido.getProductos().size(); i++) {

            String[] carrito = new String[4];

            carrito[0] = pedido.getProductos().get(i).getId();
            carrito[1] = pedido.getProductos().get(i).getNombre();
            carrito[2] = pedido.getCantidad().get(i).toString();

            Double precioFinal = pedido.getCantidad().get(i) * pedido.getProductos().get(i).getPrecioVenta();

            carrito[3] = precioFinal.toString();

            total += precioFinal;

            lista.add(carrito);

        }

        modelo.put("listacarrito", lista);
        modelo.put("total", total);

        return "compra.html";
    }

    @PostMapping("/quitar")
    public String quitar(@RequestParam String idUsuario, @RequestParam String id) {

        Pedido pedido = pedidoService.carrito(idUsuario);

        Producto producto = productoService.buscarPorId(id);

        pedidoService.quitar(pedido, producto);

        return "redirect:/compra";
    }

    @PostMapping("/confirmar")
    public String confirmar(@RequestParam String idUsuario, @RequestParam String total) {

        Pedido pedido = pedidoService.carrito(idUsuario);

        Double totalDouble = Double.parseDouble(total);

        pedidoService.confirmarCarrito(pedido, totalDouble);

        return "redirect:/inicio";
    }
    
    @PreAuthorize("hasRole('ROLE_USUARIO')")
	@GetMapping("/miscompras")
	public String miscompras(ModelMap modelo) throws ErrorService {
		

		List<Producto> productos = productoService.listarProducto();
		
	
		Set<Estado> estado = EnumSet.allOf(Estado.class);
		
        modelo.put("estados", estado);
		
		modelo.put("productos", productos);

               
                
		return "miscompras.html";
		
	}
}
