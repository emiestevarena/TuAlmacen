package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.formato.Fecha;
import com.egg.TuAlmacen.formato.Ventas;
import com.egg.TuAlmacen.service.PedidoService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EstadisticasController {

    @Autowired
    private PedidoService pedidoService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/estadisticas")
    public String estadisticas(ModelMap modelo) throws ErrorService {
        modelo.put("masvendidos", pedidoService.masVendidos());
        return "estadisticas.html";
    }

    @PostMapping("/ganancias")
    public String ganancias(ModelMap modelo, @RequestParam String start, @RequestParam String end) throws ErrorService {
        Double ganancias = 0.0;
        Date s = Fecha.parseFechaGuiones(start);
        Date e = Fecha.parseFechaGuiones(end);
        if (s.before(e)) {
            ganancias = pedidoService.ganancias(s, e);
        }
        modelo.put("ganancias", ganancias);
        return this.estadisticas(modelo);
        
    }

}
