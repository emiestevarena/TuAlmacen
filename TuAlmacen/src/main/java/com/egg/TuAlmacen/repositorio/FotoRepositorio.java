package com.egg.TuAlmacen.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.Foto;




@Repository
public interface FotoRepositorio extends JpaRepository<Foto,String>{

}
