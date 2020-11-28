package com.egg.TuAlmacen.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.TuAlmacen.entidad.Pedido;
import com.egg.TuAlmacen.enums.Estado;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, String> {

    @Query("SELECT p FROM Pedido p WHERE p.estado LIKE PENDIENTE")
    public List<Pedido> pendientes();

    @Query("SELECT p FROM Pedido p, IN(p.usuario) u WHERE p.estado LIKE 'CARRITO' AND u.id LIKE :id")
    public Pedido carrito(@Param("id") String id);

    @Query("SELECT p FROM Pedido p, IN(p.usuario) u WHERE u.id LIKE :id ORDER BY p.fecha")
    public List<Pedido> mispedidos(@Param("id") String idUsuario);

    @Query("SELECT p FROM Pedido p, IN(p.usuario) u WHERE u.id LIKE :id AND p.estado LIKE :estado ORDER BY p.fecha")
    public List<Pedido> pedidosPorEstado(@Param("id") String idUsuario, @Param("estado") Estado estado);

    @Query("SELECT p FROM Pedido p, IN(p.usuario) u WHERE p.estado LIKE :estado ORDER BY p.fecha")
    public List<Pedido> pedidosPorEstado(@Param("estado") Estado estado);

    @Query("SELECT u.nombre, COUNT(*) FROM Pedido p, IN(p.productos) u WHERE p.estado LIKE 'CONFIRMADO' ORDER BY COUNT(*)")
    public List<String[]> pedidoDeProductos();

    @Query("select p from Pedido p where p.estado LIKE 'CONFIRMADO' and p.fecha between :start and :end")
    public List<Pedido> ventasEfectivasPorPeriodo(@Param("start") Date start, @Param("end") Date end);

}
