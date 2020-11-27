
package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.formato.Ventas;
import com.egg.TuAlmacen.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class EstadisticasController {
    
    
    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping("/estadisticas")
    public String estadisticas(ModelMap modelo) throws ErrorService{
        modelo.put("masvendidos", pedidoService.masVendidos());
        return "estadisticas.html";
    }
    
    
    
}
