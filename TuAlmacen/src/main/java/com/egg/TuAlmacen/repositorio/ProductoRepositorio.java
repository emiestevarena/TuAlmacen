package com.egg.TuAlmacen.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.Producto;
import com.egg.TuAlmacen.enums.Rubro;
import java.util.List;


@Repository
public interface ProductoRepositorio extends JpaRepository<Producto,String>{

	
	@Query("SELECT c FROM Producto c WHERE c.nombre =:nombre")
	public Producto buscarPorNombre(@Param("nombre")String nombre);
        
        @Query("SELECT c FROM Producto c WHERE c.rubro LIKE :rubro")
	public List<Producto> buscarPorRubro(@Param("rubro") Rubro rubro);
        
        @Query("SELECT c FROM Producto c GROUP BY c.rubro")
	public List<Producto> buscarPrimeroRubro();
	
}
