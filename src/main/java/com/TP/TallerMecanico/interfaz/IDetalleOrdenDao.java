package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Orden;

import java.time.LocalDate;
import java.util.List;

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

    
    @Query("SELECT s.nombre AS nombreServicio, SUM(CAST(d.cantidad AS INTEGER)) AS cantidadUtilizada, SUM(d.subtotal) AS montoRecaudado " +
    "FROM DetalleOrden d JOIN d.orden o JOIN d.servicio s " +
    "WHERE o.fechaRegistro BETWEEN :fechaInicio AND :fechaFin " +
    "GROUP BY s.nombre ")
    List<Object[]> obtenerIngresosPorServicio(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin );
}