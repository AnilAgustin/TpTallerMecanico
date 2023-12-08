package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Orden;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.TP.TallerMecanico.entidad.Servicio;
import com.TP.TallerMecanico.entidad.Tecnico;

import jakarta.transaction.Transactional;

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
    "WHERE o.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId " +
    "GROUP BY s.nombre ")
    List<Object[]> obtenerIngresosPorServicio(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId);

    @Query("SELECT SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) FROM DetalleOrden d " +
    "JOIN d.orden o " +
    "WHERE o.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId")
    Double calcularMontoTotalEnPeriodo(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId);

    @Query("SELECT d.servicio.nombre AS servicio, SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) AS montoTotal " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId " +
    "GROUP BY d.servicio.nombre " +
    "ORDER BY montoTotal DESC " +
    "LIMIT 1")
    List<Object[]> findServicioMasRecaudo(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId); 

    @Query("SELECT d.servicio.nombre AS servicio, SUM(CAST(d.cantidad AS int)) AS cantidadTotal " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin AND d.orden.tecnico.id = :tecnicoId " +
    "GROUP BY d.servicio.nombre " +
    "ORDER BY cantidadTotal DESC " +
    "LIMIT 1")
    List<Object[]> findServicioMasUtilizado(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("tecnicoId") Long tecnicoId);


    //Estadisticas sin filtrado
    @Query("SELECT s.nombre AS nombreServicio, SUM(CAST(d.cantidad AS INTEGER)) AS cantidadUtilizada, SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) AS montoRecaudado " +
    "FROM DetalleOrden d JOIN d.orden o JOIN d.servicio s " +
    "WHERE o.fechaDocumento BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY s.nombre ")
    List<Object[]> obtenerIngresosPorServicioSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) FROM DetalleOrden d " +
    "JOIN d.orden o " +
    "WHERE o.fechaDocumento BETWEEN :fechaInicio AND :fechaFin ")
    Double calcularMontoTotalEnPeriodoSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT d.servicio.nombre AS servicio, SUM(d.subtotal * (1 + d.orden.vehiculo.modelo.marca.impuesto/100)) AS montoTotal " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY d.servicio.nombre " +
    "ORDER BY montoTotal DESC " +
    "LIMIT 1")
    List<Object[]> findServicioMasRecaudoSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin); 

    @Query("SELECT d.servicio.nombre AS servicio, SUM(CAST(d.cantidad AS int)) AS cantidadTotal " +
    "FROM DetalleOrden d " +
    "WHERE d.orden.fechaDocumento BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY d.servicio.nombre " +
    "ORDER BY cantidadTotal DESC " +
    "LIMIT 1")
    List<Object[]> findServicioMasUtilizadoSinFiltro(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);



}