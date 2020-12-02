package com.egg.TuAlmacen.service;

import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.egg.TuAlmacen.entidad.Usuario;



@Service
public class NotificacionMail {

    @Autowired
    private JavaMailSender mailSender;

  
    
    @Async
    public void enviar(String cuerpo,String titulo, String mail){
         
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("tualmacenegg@gmail.com");
        mensaje.setTo(mail);
        mensaje.setSubject(titulo);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
    }
   
    @Async
    public void contactar(String mensaje, String asunto,Usuario u) {    	
    	String usuario="usuario: " + u.getUsuario() + "\n";
    	String email="Email: " + u.getEmail() + "\n";
    	String cuerpo= usuario + email+"Consulta: " + mensaje;
    	this.enviar(cuerpo, asunto, "tualmacenegg@gmail.com");
    	
  	
    }
    @Transactional
    public void reset(Usuario u) {  
        this.enviar("Felicidades su password cambio exitosamente", "Cambio de password", u.getEmail());

    }
    public void constructResetTokenEmail(String contextPath, Locale locale, String token, Usuario usuario) {
    	
        String url = contextPath + "/user/changePassword/" + token;
        String message = "Para cambiar su contrasena ingrese al siguiente link";
        
        this.enviar(message+" "+url, "Resetar contrasena", usuario.getEmail());   
        
        //return constructEmail("Reset Password", message + " \r\n" + url, usuario);
    }
      
}

