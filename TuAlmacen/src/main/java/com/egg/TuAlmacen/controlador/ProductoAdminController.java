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
public class ProductoAdminController {

    @Autowired
    private ProductoService productoService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/productos")
    public String productos(ModelMap modelo) throws ErrorService {

        List<Producto> productos = productoService.listarProducto();

        modelo.put("productos", productos);

        Set<Rubro> rubros = EnumSet.allOf(Rubro.class);
        modelo.put("rubros", rubros);

        return "productos.html";

    }

    @PostMapping("/altaproducto")
    public String altaproducto(ModelMap modelo,
            @RequestParam String nombre,
            @RequestParam String precioCompra,
            @RequestParam String precioVenta,
            @RequestParam String cantidad,
            @RequestParam String descripcion,
            @RequestParam String rubro,
            MultipartFile archivo) throws ErrorService {

        try {

            productoService.registrarProducto(nombre, Double.parseDouble(precioCompra),
                    Integer.parseInt(cantidad), Double.parseDouble(precioVenta),
                    descripcion, archivo, Rubro.valueOf(rubro));

        } catch (Exception e) {

            modelo.put("nombre", nombre);
            modelo.put("error", e.getMessage());
            modelo.put("precioCompra", precioCompra);
            modelo.put("precioVenta", precioVenta);
            modelo.put("cantidad", cantidad);
            modelo.put("descripcion", descripcion);
            modelo.put("rubro", rubro);

            return this.productos(modelo);
        }

        modelo.put("mensaje", "Has registrado el producto exitosamente :P");

        return this.productos(modelo);
    }

    @PostMapping("/modificarproducto")
    public String modificarproducto(ModelMap modelo,
            @RequestParam String id,
            @RequestParam(required=false) String nombre,
            @RequestParam(required=false) String precioCompra,
            @RequestParam(required=false) String precioVenta,
            @RequestParam(required=false) String cantidad,
            @RequestParam(required=false) String descripcion,
            @RequestParam(required=false) String rubro,
            MultipartFile archivo) throws ErrorService {

        try {
            
            Producto producto = productoService.buscarPorId(id);
            
            if(nombre!=null&&!nombre.isEmpty()){producto.setNombre(nombre);}
            if(precioCompra!=null&&!precioCompra.isEmpty()){producto.setPrecioCompra(Double.parseDouble(precioCompra));}
            if(precioVenta!=null&&!precioVenta.isEmpty()){producto.setPrecioVenta(Double.parseDouble(precioVenta));}
            if(cantidad!=null&&!cantidad.isEmpty()){producto.setCantidad(Integer.parseInt(cantidad));}
            if(descripcion!=null&&!descripcion.isEmpty()){producto.setDescripcion(descripcion);}
            if(rubro!=null&&!rubro.isEmpty()){producto.setRubro(Rubro.valueOf(rubro));}
            
            productoService.modificarProducto(producto, archivo);


        } catch (Exception e) {

            List<Producto> productos = productoService.listarProducto();

            modelo.put("productos", productos);

            modelo.put("id", id);
            modelo.put("nombre", nombre);
            modelo.put("error", e.getMessage());
            modelo.put("precioCompra", precioCompra);
            modelo.put("precioVenta", precioVenta);
            modelo.put("cantidad", cantidad);
            modelo.put("descripcion", descripcion);
            modelo.put("rubro", rubro);

            return this.productos(modelo);
        }
        
        modelo.put("mensaje", "Se ha modificado el producto exitosamente maquina ");

        return this.productos(modelo);
    }

    @PostMapping("/bajaproducto")
    public String bajaproducto(ModelMap modelo,
            HttpSession session,
            @RequestParam String id) throws ErrorService {

        try {

            productoService.eliminarProducto(id);


        } catch (ErrorService e) {
            modelo.addAttribute("error", e.getMessage());
            return this.productos(modelo);
        }
        
        modelo.put("mensaje", "Se ha eliminado el producto exitosamente");
            
        return this.productos(modelo);
    }

}
