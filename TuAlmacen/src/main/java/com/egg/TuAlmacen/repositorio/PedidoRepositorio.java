package com.egg.TuAlmacen.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.entidad.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido,String>{

    @Query("SELECT p FROM Pedido p WHERE p.estado LIKE PENDIENTE")
    public List<Pedido> pendientes();
    
    @Query("SELECT p FROM Pedido p, IN(p.usuario) u WHERE p.estado LIKE 'CARRITO' AND u.id LIKE :id")
    public Pedido carrito(@Param("id") String id);
}
