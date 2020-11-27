
package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.formato.Ventas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class EstadisticasController {
    
//    @Autowired
    private Ventas ventas;
    
    @GetMapping("/estadisticas")
    public String estadisticas(ModelMap modelo){
        ventas = new Ventas();
        modelo.put("masvendidos", ventas.masVendidos());
        return "estadisticas.html";
    }
    
    
    
}
