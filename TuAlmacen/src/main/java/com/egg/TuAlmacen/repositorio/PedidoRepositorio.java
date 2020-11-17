package com.egg.TuAlmacen.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.Pedido;




@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido,String>{

}
