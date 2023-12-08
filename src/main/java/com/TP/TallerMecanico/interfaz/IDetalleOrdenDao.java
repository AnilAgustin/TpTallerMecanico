package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Orden;
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
