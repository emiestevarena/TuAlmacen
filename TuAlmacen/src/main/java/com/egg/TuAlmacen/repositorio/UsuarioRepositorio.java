package com.egg.TuAlmacen.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.Usuario;




@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,String>{

	@Query("SELECT c FROM Usuario c WHERE c.usuario =:usuario")
	public Usuario buscarPorUsuario(@Param("usuario")String usuario);
	@Query("SELECT c FROM Usuario c WHERE c.email =:email")
	public Usuario buscarPorEmail(@Param("email")String email);
	
}
