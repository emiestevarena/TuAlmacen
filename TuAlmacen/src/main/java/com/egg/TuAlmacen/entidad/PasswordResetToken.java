
package com.egg.TuAlmacen.entidad;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class PasswordResetToken {
    
    private static int EXPIRATION = 60 * 24;
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String token;

    @OneToOne
    private Usuario usuario;

    private Date expiryDate;
    
      public PasswordResetToken(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    /**
     * @return the EXPIRATION
     */
    public static int getEXPIRATION() {
        return EXPIRATION;
    }

    /**
     * @param aEXPIRATION the EXPIRATION to set
     */
    public static void setEXPIRATION(int aEXPIRATION) {
        EXPIRATION = aEXPIRATION;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the expiryDate
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    
}
