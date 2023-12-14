package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Estado;
import com.TP.TallerMecanico.entidad.Orden;
import java.time.LocalDate;
import java.util.List;
import com.TP.TallerMecanico.entidad.Servicio;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IDetalleOrdenDao extends CrudRepository<DetalleOrden, Long>{

    @Modifying
    @Query("UPDATE DetalleOrden m SET m.estado = false WHERE m.idDetalleOrden = :idDetalleOrden") //Query para el Soft Delete
    void marcarComoEliminado(@Param("idDetalleOrden") Long idDetalleOrden);


    List<DetalleOrden> findByEstadoTrue(); //arma una consulta SQL personalizada con estado

    @Modifying
    @Query("UPDATE DetalleOrden m SET m.estado = true WHERE m.idDetalleOrden = :idDetalleOrden")  //Update a estado true
    void marcarComoActivo(@Param("idDetalleOrden") Long idDetalleOrden);

    List<DetalleOrden> findByOrdenAndEstadoTrue(Orden orden);

    DetalleOrden findByServicioAndOrden(Servicio servicio, Orden orden);

    DetalleOrden findByServicioAndOrdenAndEstadoTrue(Servicio servicio, Orden orden);
    DetalleOrden findByIdDetalleOrden(Long idDetalleOrden);
    DetalleOrden findByIdDetalleOrdenAndEstadoTrue(Long idDetalleOrden);

    
    @Query("SELECT s.nombre AS nombreServicio, SUM(CAST(d.cantidad AS INTEGER)) AS cantidadUtilizada, SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) AS montoRecaudado " +
    "FROM DetalleOrden d JOIN d.orden o JOIN d.servicio s " +
    "WHERE o.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId AND d.orden.estado = true AND d.orden.estadoActual = :estadoActual " +
    "GROUP BY s.nombre ")
    List<Object[]> obtenerIngresosPorServicio(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId, @Param ("estadoActual") Estado estadoActual);

    @Query("SELECT SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) FROM DetalleOrden d " +
    "JOIN d.orden o " +
    "WHERE o.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId AND d.orden.estado = true AND d.orden.estadoActual = :estadoActual")
    Double calcularMontoTotalEnPeriodo(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId, @Param ("estadoActual") Estado estadoActual);

    @Query("SELECT d.servicio.nombre AS servicio, SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) AS montoTotal " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId AND d.orden.estado = true AND d.orden.estadoActual = :estadoActual " +
    "GROUP BY d.servicio.nombre " +
    "ORDER BY montoTotal DESC " +
    "LIMIT 1")
    List<Object[]> findServicioMasRecaudo(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId, @Param ("estadoActual") Estado estadoActual); 

    @Query("SELECT d.servicio.nombre AS servicio, SUM(CAST(d.cantidad AS int)) AS cantidadTotal " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId AND d.orden.estado = true AND d.orden.estadoActual = :estadoActual " +
    "GROUP BY d.servicio.nombre " +
    "ORDER BY cantidadTotal DESC " +
    "LIMIT 1")
    List<Object[]> findServicioMasUtilizado(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId, @Param ("estadoActual") Estado estadoActual);

    @Query("SELECT SUM(CAST(d.cantidad AS INTEGER)) " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId " +
    "AND d.orden.estado = true " +
    "AND d.orden.estadoActual = :estadoActual")
    Integer obtenerCantidadTotalServicios(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId, @Param("estadoActual") Estado estadoActual);


    //Estadisticas sin filtrado
    @Query("SELECT s.nombre AS nombreServicio, SUM(CAST(d.cantidad AS INTEGER)) AS cantidadUtilizada, SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) AS montoRecaudado " +
    "FROM DetalleOrden d JOIN d.orden o JOIN d.servicio s " +
    "WHERE o.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.estado = true AND d.orden.estadoActual = :estadoActual " +
    "GROUP BY s.nombre ")
    List<Object[]> obtenerIngresosPorServicioSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param ("estadoActual") Estado estadoActual);

    @Query("SELECT SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) FROM DetalleOrden d " +
    "JOIN d.orden o " +
    "WHERE o.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.estado = true AND d.orden.estadoActual = :estadoActual ")
    Double calcularMontoTotalEnPeriodoSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param ("estadoActual") Estado estadoActual);

    @Query("SELECT d.servicio.nombre AS servicio, SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) AS montoTotal " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.estado = true AND d.orden.estadoActual = :estadoActual " +
    "GROUP BY d.servicio.nombre " +
    "ORDER BY montoTotal DESC " +
    "LIMIT 1")
    List<Object[]> findServicioMasRecaudoSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param ("estadoActual") Estado estadoActual); 

    @Query("SELECT d.servicio.nombre AS servicio, SUM(CAST(d.cantidad AS int)) AS cantidadTotal " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.estado = true AND d.orden.estadoActual = :estadoActual " +
    "GROUP BY d.servicio.nombre " +
    "ORDER BY cantidadTotal DESC " +
    "LIMIT 1")
    List<Object[]> findServicioMasUtilizadoSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param ("estadoActual") Estado estadoActual);
    
    @Query("SELECT SUM(CAST(d.cantidad AS INTEGER)) " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin " +
    "AND d.orden.estado = true " +
    "AND d.orden.estadoActual = :estadoActual")
    Integer obtenerCantidadTotalServiciosSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("estadoActual") Estado estadoActual);

    //Consulta para obtener el Id de una orden relacionada a un detalle de orden
    @Query("SELECT d FROM DetalleOrden d INNER JOIN d.orden o WHERE o.idOrden = :idOrden")
    List<DetalleOrden> findbyIdOrden(@Param("idOrden") Long idOrden);
   
    //Consulta personalizada para obtener el nombre del cliente relacionado a una orden
    @Query("SELECT DISTINCT c.nombre FROM DetalleOrden d JOIN d.orden o JOIN o.vehiculo v JOIN v.cliente c WHERE o.idOrden = :idOrden")
    String findNombreClienteByIdOrden(@Param("idOrden") Long idOrden);
    
    //Consulta personalizada para obtener el apellido del cliente relacionado a una orden
    @Query("SELECT DISTINCT c.apellido FROM DetalleOrden d JOIN d.orden o JOIN o.vehiculo v JOIN v.cliente c WHERE o.idOrden = :idOrden")
    String findApellidoClienteByIdOrden(@Param("idOrden") Long idOrden);
    
    //Consulta personalizada para obtener el impuesto agregado por marca
    @Query("SELECT DISTINCT marca.impuesto FROM DetalleOrden detalle JOIN detalle.orden o JOIN o.vehiculo v JOIN v.modelo m JOIN m.marca marca WHERE o.idOrden = :idOrden")
    Double findImpuestoMarcaByIdOrden(@Param("idOrden") Long idOrden);

}
