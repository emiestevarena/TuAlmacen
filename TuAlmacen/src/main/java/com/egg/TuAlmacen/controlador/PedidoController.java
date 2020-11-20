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
    public String agregar(@RequestParam String idUsuario, @RequestParam String id, @RequestParam Integer cantidad){
        
        Pedido pedido = pedidoService.carrito(idUsuario);
        
        Producto producto = productoService.buscarPorId(id);
        
        if(pedido==null){
            pedidoService.miCarrito(usuarioService.buscarPorId(idUsuario), producto, cantidad);
        }else{
            pedidoService.agregar(pedido, producto, cantidad);
        }
        
        return "redirect:/inicio";
    }
    
    @GetMapping("/compra")
    public String compra(ModelMap modelo){
        
        Usuario u = (Usuario) session.getAttribute("usuariosession");
        Pedido pedido = pedidoService.carrito(u.getId());
        
        List<String[]> lista = new ArrayList();
        
        
        for (int i = 0; i <pedido.getProductos().size(); i++) {
            
            String[] carrito = new String[4];
            
            carrito[0] = pedido.getProductos().get(i).getId();
            carrito[1] = pedido.getProductos().get(i).getNombre();
            carrito[2] = pedido.getCantidad().get(i).toString();
            
            Double precioFinal =  pedido.getCantidad().get(i) * pedido.getProductos().get(i).getPrecioVenta();
            
            carrito[3] = precioFinal.toString(); 
            
            lista.add(carrito);
            
        }
        
        modelo.put("listacarrito", lista);
        
        return "compra.html";
    }
    
    @PostMapping("/quitar")
    public String quitar(@RequestParam String idUsuario, @RequestParam String id){
        
        Pedido pedido = pedidoService.carrito(idUsuario);
        
        Producto producto = productoService.buscarPorId(id);

        pedidoService.quitar(pedido, producto);
        
        return "redirect:/compra";
    }
    
}
