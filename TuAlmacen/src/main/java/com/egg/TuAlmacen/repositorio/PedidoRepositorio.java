package com.egg.TuAlmacen.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.Query;




@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido,String>{

    @Query("select p from pedido p where p.estado like 'PENDIENTE'")
    public List<Pedido> pendientes();
}
