
package com.egg.TuAlmacen.controlador;



import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.TuAlmacen.entidad.Usuario;
import com.egg.TuAlmacen.error.ErrorService;
import com.egg.TuAlmacen.repositorio.UsuarioRepositorio;
import com.egg.TuAlmacen.service.NotificacionMail;
import com.egg.TuAlmacen.service.PasswordResetTokenService;
import com.egg.TuAlmacen.service.UsuarioService;


@Controller
public class PasswordResetTokenControlador {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioService usuarioServicio;
    
    @Autowired
    private NotificacionMail notificacionServicio;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/user/resetpassword")
    public String resetPassword(HttpServletRequest request,ModelMap modelo, @RequestParam("email") String userEmail) throws Error {

        Usuario empleado = usuarioRepositorio.buscarPorEmail(userEmail);
        if (empleado == null) {
            throw new Error("no se encontro el email");
        }
        String token = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForUser(empleado, token);
        notificacionServicio.constructResetTokenEmail("http://localhost:8080", request.getLocale(), token, empleado);
        //return new GenericResponse("message.resetPasswordEmail");
		modelo.put("mensaje", "mensaje enviado");
        return editar(modelo);
        
    }

    @GetMapping("/user/changePassword")
    public String editar(ModelMap modelo) {
         
        return "Cambiocontra.html";
    }

    @GetMapping("/user/changePassword/{id}")
    public String editar(ModelMap modelo, @PathVariable String id) {

        Usuario usuario = passwordResetTokenService.buscarToken(id);
        modelo.put("usuario", usuario);
        return "resetcontra.html";
    }
    @PostMapping("/resetcontra")
    public String npasword(ModelMap modelo, @RequestParam String clave, @RequestParam String clave2, @RequestParam String id) throws ErrorService {

        if (clave.equals(clave2)) {
            Usuario u= usuarioServicio.buscarPorId(id);
            if (u!= null) {  
            	
            	
            	String encriptada = new BCryptPasswordEncoder().encode(clave);
                u.setPassword(encriptada);               
                usuarioRepositorio.save(u);
                notificacionServicio.enviar("Felicidades su password cambio exitosamente", "Cambio de password", u.getEmail());
                modelo.put("titulo", "Felicidades");
                modelo.put("mensaje", "Su contraseña fue cambiada exitosamente");

                return this.editar(modelo, id);
            }

            modelo.put("titulo", "Algo Salio mal");
            modelo.put("error", "intente mas tarde");

            return this.editar(modelo, id);

        }
        modelo.put("titulo", "Algo Salio mal");
        modelo.put("error", "Las contraseñas no coinciden");
        return this.editar(modelo, id);
    }

    private static class GenericResponse {

        private String message;
        private String error;

        public GenericResponse(String message) {
            super();
            this.message = message;
        }

        public GenericResponse(String message, String error) {
            super();
            this.message = message;
            this.error = error;
        }
    }
}
