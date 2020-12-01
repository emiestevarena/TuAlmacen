
package com.egg.TuAlmacen.repositorio;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.PasswordResetToken;
import com.egg.TuAlmacen.entidad.Usuario;


@Repository
public interface PasswordResetTokenRepositorio extends JpaRepository<PasswordResetToken, String>  {
      @Query("SELECT u FROM PasswordResetToken t, IN(t.usuario) u WHERE t.token = :token")
      public Usuario buscarEmpleadoPorTokenl(@Param("token") String token);
    
}
