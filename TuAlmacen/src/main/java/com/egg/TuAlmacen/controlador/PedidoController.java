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
        }
        
        pedidoService.agregar(pedido, producto, cantidad);
        
        return "redirect:/inicio";
    }
    
    @GetMapping("/compra")
    public String compra(ModelMap modelo){
        
        return "compra.html";
    }
    
}
