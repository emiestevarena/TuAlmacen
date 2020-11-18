/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.TuAlmacen.controlador;

import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.service.PedidoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author emiliano
 */
@Controller
@RequestMapping("/")
public class AdminController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin(ModelMap modelo){
        List<Pedido> pendientes = pedidoService.pendientes();
        if(pendientes != null || !pendientes.isEmpty()) {modelo.put("pendientes",pendientes);}
        return "admin.html";
    }
}
