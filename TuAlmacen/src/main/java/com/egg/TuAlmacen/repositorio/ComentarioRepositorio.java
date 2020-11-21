package com.egg.TuAlmacen.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.Comentario;
import com.egg.TuAlmacen.entidad.Producto;




@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario,String>{

	
	@Query("SELECT c FROM Comentario c, IN(c.producto) p WHERE p.id LIKE :id")
    public List<Comentario>comentarioporproducto(@Param("id") String id);
	
	
}

